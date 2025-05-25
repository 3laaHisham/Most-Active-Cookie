package com.quantcast.analyzer;

import com.quantcast.observer.EventObserver;
import com.quantcast.utils.CookieResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.Map;
import java.util.List;

class DefaultCookieAnalyzerTest {

    @Test
    void handlesNullCookiesFrequency() {
        EventObserver observer = mock(EventObserver.class);
        DefaultCookieAnalyzer analyzer = new DefaultCookieAnalyzer(observer);

        Map<String, Integer> cookiesFrequency = null;
        assertThrows(NullPointerException.class, () -> analyzer.findMostActiveCookies(cookiesFrequency, 3));
    }

    @Test
    void handlesZeroTopNRanks() {
        EventObserver observer = mock(EventObserver.class);
        DefaultCookieAnalyzer analyzer = new DefaultCookieAnalyzer(observer);

        Map<String, Integer> cookiesFrequency = Map.of(
                "cookie1", 5,
                "cookie2", 10
        );
        List<CookieResult> result = analyzer.findMostActiveCookies(cookiesFrequency, 0);
        assertTrue(result.isEmpty());
    }

    @Test
    void handlesNegativeTopNRanks() {
        EventObserver observer = mock(EventObserver.class);
        DefaultCookieAnalyzer analyzer = new DefaultCookieAnalyzer(observer);

        Map<String, Integer> cookiesFrequency = Map.of(
                "cookie1", 5,
                "cookie2", 10
        );
        // Expecting exception
        assertThrows(IllegalArgumentException.class, () -> analyzer.findMostActiveCookies(cookiesFrequency, -1));
    }

    @Test
    void handlesEmptyMapWithPositiveTopNRanks() {
        EventObserver observer = mock(EventObserver.class);
        HeapCookieAnalyzer analyzer = new HeapCookieAnalyzer(observer);

        Map<String, Integer> cookiesFrequency = Map.of();
        List<CookieResult> result = analyzer.findMostActiveCookies(cookiesFrequency, 3);
        assertTrue(result.isEmpty());
    }

    @Test
    void handlesSingleCookieInMap() {
        EventObserver observer = mock(EventObserver.class);
        HeapCookieAnalyzer analyzer = new HeapCookieAnalyzer(observer);

        Map<String, Integer> cookiesFrequency = Map.of(
                "cookie1", 5
        );
        List<CookieResult> result = analyzer.findMostActiveCookies(cookiesFrequency, 3);
        assertEquals(List.of( new CookieResult("cookie1")), result);
    }
}