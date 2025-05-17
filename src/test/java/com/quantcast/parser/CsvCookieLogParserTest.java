package com.quantcast.parser;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvCookieLogParserTest {

    @Test
    void parseValidCsvShouldReturnEntries() throws Exception {
        Path temp = Files.createTempFile("cookies", ".csv");
        try (FileWriter w = new FileWriter(temp.toFile())) {
            w.write("cookie,timestamp\n");
            w.write("a,2021-07-01T10:00:00+00:00\n");
            w.write("b,2021-07-01T11:30:00+00:00\n");
        }

        CookieLogParser parser = new CsvCookieLogParser();
        List<CookieLogEntry> entries = parser.parse(temp.toString());

        assertEquals(2, entries.size());
        assertEquals("a", entries.get(0).getCookie());
        assertEquals("b", entries.get(1).getCookie());
    }

    @Test
    void parseCsvWithMalformedLinesShouldSkipThem() throws Exception {
        Path temp = Files.createTempFile("cookies", ".csv");
        try (FileWriter w = new FileWriter(temp.toFile())) {
            w.write("cookie,timestamp\n");
            w.write("good,2021-07-02T09:00:00+00:00\n");
            w.write("bad_timestamp,not-a-timestamp\n");
            w.write("incomplete_line_only_cookie\n");
            w.write("another,2021-07-02T10:00:00+00:00\n");
        }

        CookieLogParser parser = new CsvCookieLogParser();
        List<CookieLogEntry> entries = parser.parse(temp.toString());

        assertEquals(2, entries.size());
        assertTrue(entries.stream().anyMatch(e -> e.getCookie().equals("good")));
        assertTrue(entries.stream().anyMatch(e -> e.getCookie().equals("another")));
    }
}
