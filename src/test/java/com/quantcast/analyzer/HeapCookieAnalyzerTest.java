package com.quantcast.analyzer;

import com.quantcast.observer.EventObserver;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Map;
import java.util.List;
import java.util.PriorityQueue;

class HeapCookieAnalyzerTest {

    @Test
    void returnsEmptyListWhenFrequencyMapIsEmpty() {
        EventObserver observer = mock(EventObserver.class);
        HeapCookieAnalyzer analyzer = new HeapCookieAnalyzer(observer);
        Map<String, Integer> freqMap = Map.of();
        List<String> result = analyzer.findMostActiveCookies(freqMap, 3);
        assertTrue(result.isEmpty());
        verify(observer).analysisStarted(0);
        verify(observer).analysisCompleted(0, 0);
    }

    @Test
    void returnsTopNMostFrequentCookies() {
        EventObserver observer = mock(EventObserver.class);
        HeapCookieAnalyzer analyzer = new HeapCookieAnalyzer(observer);
        Map<String, Integer> freqMap = Map.of(
                "cookie1", 5,
                "cookie2", 10,
                "cookie3", 7,
                "cookie4", 3
        );
        List<String> result = analyzer.findMostActiveCookies(freqMap, 2);
        assertEquals(List.of("cookie2", "cookie3"), result);
        verify(observer).analysisStarted(4);
        verify(observer).analysisCompleted(4, 2);
    }

    @Test
    void handlesCookiesWithSameFrequency() {
        EventObserver observer = mock(EventObserver.class);
        HeapCookieAnalyzer analyzer = new HeapCookieAnalyzer(observer);
        Map<String, Integer> freqMap = Map.of(
                "cookie1", 5,
                "cookie2", 5,
                "cookie3", 7
        );
        List<String> result = analyzer.findMostActiveCookies(freqMap, 2);
        assertEquals(List.of("cookie3", "cookie1", "cookie2"), result);
        verify(observer).analysisStarted(3);
        verify(observer).analysisCompleted(3, 3);
    }

    @Test
    void handlesNegativeTopNRanksGracefully() {
        EventObserver observer = mock(EventObserver.class);
        HeapCookieAnalyzer analyzer = new HeapCookieAnalyzer(observer);
        Map<String, Integer> freqMap = Map.of(
                "cookie1", 5,
                "cookie2", 10
        );
        List<String> result = analyzer.findMostActiveCookies(freqMap, -1);
        assertTrue(result.isEmpty());
        verify(observer).analysisStarted(2);
        verify(observer).analysisCompleted(2, 0);
    }

    @Test
    void handlesSingleCookieInMap() {
        EventObserver observer = mock(EventObserver.class);
        HeapCookieAnalyzer analyzer = new HeapCookieAnalyzer(observer);
        Map<String, Integer> freqMap = Map.of(
                "cookie1", 5
        );
        List<String> result = analyzer.findMostActiveCookies(freqMap, 3);
        assertEquals(List.of("cookie1"), result);
        verify(observer).analysisStarted(1);
        verify(observer).analysisCompleted(1, 1);
    }
}