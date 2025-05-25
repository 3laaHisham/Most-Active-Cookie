package com.quantcast.analyzer;

import com.quantcast.observer.EventObserver;
import com.quantcast.utils.CookieResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Map;
import java.util.List;

class HeapCookieAnalyzerTest {

    @Test
    void returnsEmptyListWhenFrequencyMapIsEmpty() {
        EventObserver observer = mock(EventObserver.class);
        HeapCookieAnalyzer analyzer = new HeapCookieAnalyzer(observer);
        Map<String, Integer> freqMap = Map.of();
        List<CookieResult> result = analyzer.findMostActiveCookies(freqMap, 3);
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
        List<CookieResult> result = analyzer.findMostActiveCookies(freqMap, 2);
        assertEquals(List.of(new CookieResult("cookie2"), new CookieResult("cookie3")), result);
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
        List<CookieResult> result = analyzer.findMostActiveCookies(freqMap, 2);

        // The order of cookies with the same frequency is not guaranteed, so assert equal one of them to be true
        // [cookie3, cookie2, cookie1] or [cookie3, cookie1, cookie2] and cookie3 should always be first
        assertEquals(new CookieResult("cookie3"), result.getFirst());
        assertTrue(result.contains(new CookieResult("cookie1")));
        assertTrue(result.contains(new CookieResult("cookie2")));

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
        List<CookieResult> result = analyzer.findMostActiveCookies(freqMap, -1);
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
        List<CookieResult> result = analyzer.findMostActiveCookies(freqMap, 3);
        assertEquals(List.of(new CookieResult("cookie1")), result);
        verify(observer).analysisStarted(1);
        verify(observer).analysisCompleted(1, 1);
    }
}