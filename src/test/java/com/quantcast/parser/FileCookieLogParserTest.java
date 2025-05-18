package com.quantcast.parser;

import com.quantcast.observer.EventObserver;
import com.quantcast.utils.DateRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileCookieLogParserTest {

    @Mock
    private EventObserver observer;

    private FileCookieLogParser parser;
    private static final String TEST_PATH = "dummy-path.csv";

    @BeforeEach
    void setUp() {
        parser = new FileCookieLogParser(observer);
    }

    @Test
    void testConstructor_nullObserverThrows() {
        assertThrows(NullPointerException.class,
                () -> new FileCookieLogParser(null));
    }

    @Test
    void testFileNotFound_throwsAndStartsParsing() {
        String badPath = "nonexistent-file.csv";
        DateRange range = new DateRange(LocalDate.now(), LocalDate.now());
        // Expect FileNotFoundException when path doesn't exist
        assertThrows(FileNotFoundException.class,
                () -> parser.countFrequencies(badPath, range));
        // parsingStarted should still be called before exception
        verify(observer).parsingStarted(badPath);
        verify(observer, never()).parsingCompleted(anyLong(), anyLong());
    }

    @Test
    void testNoHeader_readsNoLines() throws IOException {
        DateRange range = new DateRange(LocalDate.of(2025,5,16), LocalDate.of(2025,5,18));
        // Header missing (readLine returns null immediately)
        try (MockedConstruction<FileReader> frMock = Mockito.mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> brMock = Mockito.mockConstruction(BufferedReader.class,
                     (mock, ctx) -> when(mock.readLine()).thenReturn(null))) {
            Map<String, Integer> freq = parser.countFrequencies(TEST_PATH, range);
            // No entries processed
            assertTrue(freq.isEmpty());
            verify(observer).parsingStarted(TEST_PATH);
            verify(observer).parsingCompleted(0L, 0L);
        }
    }

    @Test
    void testMalformedAfterHeader_recordsMalformed() throws IOException {
        String header = "cookie,timestamp";
        String broken1 = "badline";
        String broken2 = "also,broken,two";
        DateRange range = new DateRange(LocalDate.of(2025,5,1), LocalDate.of(2025,5,31));

        try (MockedConstruction<FileReader> frMock = Mockito.mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> brMock = Mockito.mockConstruction(BufferedReader.class,
                     (mock, ctx) -> when(mock.readLine())
                             .thenReturn(header)
                             .thenReturn(broken1)
                             .thenReturn(broken2)
                             .thenReturn(null))) {
            Map<String, Integer> freq = parser.countFrequencies(TEST_PATH, range);
            assertTrue(freq.isEmpty());
            // Two malformed lines reported
            verify(observer, times(1)).lineMalformed(broken1);
            verify(observer, times(1)).lineMalformed(broken2);
            verify(observer).parsingCompleted(0L, 2L);
        }
    }

    @Test
    void testCountFrequencies_withValidMalformedAndOutOfRange() throws IOException {
        String header = "cookie,timestamp";
        String valid1 = "cookieA,2025-05-17T10:15:30+00:00";
        String malformed = "noCommaLine";
        String badTimestamp = "cookieB,not-a-timestamp";
        String afterRange = "cookieC,2025-05-20T00:00:00+00:00";
        String valid2 = "cookieA,2025-05-18T12:00:00+00:00";
        String beforeRange = "cookieD,2025-05-15T23:59:59+00:00";

        DateRange range = new DateRange(LocalDate.of(2025,5,16), LocalDate.of(2025,5,18));

        try (MockedConstruction<FileReader> frMock = Mockito.mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> brMock = Mockito.mockConstruction(BufferedReader.class,
                     (mock, ctx) -> when(mock.readLine())
                             .thenReturn(header)
                             .thenReturn(valid1)
                             .thenReturn(malformed)
                             .thenReturn(badTimestamp)
                             .thenReturn(afterRange)
                             .thenReturn(valid2)
                             .thenReturn(beforeRange)
                             .thenReturn(null))) {

            Map<String, Integer> freq = parser.countFrequencies(TEST_PATH, range);

            assertEquals(2, freq.get("cookieA"));
            assertFalse(freq.containsKey("cookieB"));
            assertFalse(freq.containsKey("cookieC"));

            InOrder inOrder = inOrder(observer);
            inOrder.verify(observer).parsingStarted(TEST_PATH);
            inOrder.verify(observer).lineMalformed(malformed);
            inOrder.verify(observer).lineMalformed(badTimestamp);
            inOrder.verify(observer).parsingCompleted(2L, 2L);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Test
    void testCountFrequencies_allValid() throws IOException {
        String header = "cookie,timestamp";
        String line1 = "A,2025-05-18T00:00:00+00:00";
        String line2 = "B,2025-05-17T23:59:59+00:00";
        String line3 = "A,2025-05-16T12:00:00+00:00";

        DateRange range = new DateRange(LocalDate.of(2025,5,16), LocalDate.of(2025,5,18));

        try (MockedConstruction<FileReader> frMock = Mockito.mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> brMock = Mockito.mockConstruction(BufferedReader.class,
                     (mock, ctx) -> when(mock.readLine())
                             .thenReturn(header)
                             .thenReturn(line1)
                             .thenReturn(line2)
                             .thenReturn(line3)
                             .thenReturn(null))) {
            Map<String, Integer> freq = parser.countFrequencies(TEST_PATH, range);

            assertEquals(2, freq.get("A"));
            assertEquals(1, freq.get("B"));

            verify(observer).parsingStarted(TEST_PATH);
            verify(observer).parsingCompleted(3L, 0L);
            verify(observer, never()).lineMalformed(any());
        }
    }

    @Test
    void testCountFrequencies_emptyFile() throws IOException {
        String header = "cookie,timestamp";
        DateRange range = new DateRange(LocalDate.of(2025,5,16), LocalDate.of(2025,5,18));

        try (MockedConstruction<FileReader> frMock = Mockito.mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> brMock = Mockito.mockConstruction(BufferedReader.class,
                     (mock, ctx) -> when(mock.readLine()).thenReturn(header).thenReturn(null))) {

            Map<String, Integer> freq = parser.countFrequencies(TEST_PATH, range);
            assertTrue(freq.isEmpty());

            verify(observer).parsingStarted(TEST_PATH);
            verify(observer).parsingCompleted(0L, 0L);
            verify(observer, never()).lineMalformed(any());
        }
    }

    @Test
    void testCountFrequencies_boundaryDatesInclusive() throws IOException {
        String header = "cookie,timestamp";
        String atStart = "X,2025-05-16T00:00:00+00:00";
        String atEnd = "Y,2025-05-18T23:59:59+00:00";
        DateRange range = new DateRange(LocalDate.of(2025,5,16), LocalDate.of(2025,5,18));

        try (MockedConstruction<FileReader> frMock = Mockito.mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> brMock = Mockito.mockConstruction(BufferedReader.class,
                     (mock, ctx) -> when(mock.readLine())
                             .thenReturn(header)
                             .thenReturn(atStart)
                             .thenReturn(atEnd)
                             .thenReturn(null))) {

            Map<String, Integer> freq = parser.countFrequencies(TEST_PATH, range);
            assertEquals(1, freq.get("X"));
            assertEquals(1, freq.get("Y"));
            verify(observer).parsingCompleted(2L, 0L);
        }
    }

    @Test
    void testCountFrequencies_allAfterRange() throws IOException {
        String header = "cookie,timestamp";
        String after1 = "A,2025-05-19T00:00:00+00:00";
        String after2 = "B,2025-06-01T12:00:00+00:00";
        DateRange range = new DateRange(LocalDate.of(2025,5,16), LocalDate.of(2025,5,18));

        try (MockedConstruction<FileReader> frMock = Mockito.mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> brMock = Mockito.mockConstruction(BufferedReader.class,
                     (mock, ctx) -> when(mock.readLine())
                             .thenReturn(header)
                             .thenReturn(after1)
                             .thenReturn(after2)
                             .thenReturn(null))) {

            Map<String, Integer> freq = parser.countFrequencies(TEST_PATH, range);
            assertTrue(freq.isEmpty());
            verify(observer).parsingCompleted(0L, 0L);
        }
    }

    @Test
    void testCountFrequencies_firstLineBeforeRangeBreaks() throws IOException {
        String header = "cookie,timestamp";
        String before = "C,2025-05-15T10:00:00+00:00";
        String later = "D,2025-05-17T10:00:00+00:00";
        DateRange range = new DateRange(LocalDate.of(2025,5,16), LocalDate.of(2025,5,18));

        try (MockedConstruction<FileReader> frMock = Mockito.mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> brMock = Mockito.mockConstruction(BufferedReader.class,
                     (mock, ctx) -> when(mock.readLine())
                             .thenReturn(header)
                             .thenReturn(before)
                             .thenReturn(later)
                             .thenReturn(null))) {

            Map<String, Integer> freq = parser.countFrequencies(TEST_PATH, range);
            assertTrue(freq.isEmpty(), "Should break on first out-of-range before start");
            verify(observer).parsingCompleted(0L, 0L);
        }
    }

    @Test
    void testCountFrequencies_lineWithExtraCommaTreatedAsMalformed() throws IOException {
        String header = "cookie,timestamp";
        String extra = "E,2025-05-17T10:00:00+00:00,EXTRA";
        DateRange range = new DateRange(LocalDate.of(2025,5,16), LocalDate.of(2025,5,18));

        try (MockedConstruction<FileReader> frMock = Mockito.mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> brMock = Mockito.mockConstruction(BufferedReader.class,
                     (mock, ctx) -> when(mock.readLine())
                             .thenReturn(header)
                             .thenReturn(extra)
                             .thenReturn(null))) {

            Map<String, Integer> freq = parser.countFrequencies(TEST_PATH, range);
            assertTrue(freq.isEmpty());
            verify(observer).lineMalformed(extra);
            verify(observer).parsingCompleted(0L, 1L);
        }
    }
}
