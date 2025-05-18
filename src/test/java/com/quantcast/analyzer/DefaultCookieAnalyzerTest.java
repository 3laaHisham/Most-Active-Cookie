package com.quantcast.analyzer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.List;

class DefaultCookieAnalyzerTest {

    @Test
    void handlesNullCookiesFrequency() {
        DefaultCookieAnalyzer analyzer = new DefaultCookieAnalyzer(null);
        Map<String, Integer> cookiesFrequency = null;
        assertThrows(NullPointerException.class, () -> analyzer.findMostActiveCookies(cookiesFrequency, 3));
    }

    @Test
    void handlesZeroTopNRanks() {
        DefaultCookieAnalyzer analyzer = new DefaultCookieAnalyzer(null);
        Map<String, Integer> cookiesFrequency = Map.of(
                "cookie1", 5,
                "cookie2", 10
        );
        List<String> result = analyzer.findMostActiveCookies(cookiesFrequency, 0);
        assertTrue(result.isEmpty());
    }

    @Test
    void handlesNegativeTopNRanks() {
        DefaultCookieAnalyzer analyzer = new DefaultCookieAnalyzer(null);
        Map<String, Integer> cookiesFrequency = Map.of(
                "cookie1", 5,
                "cookie2", 10
        );
        // Expecting exception
        assertThrows(IllegalArgumentException.class, () -> analyzer.findMostActiveCookies(cookiesFrequency, -1));
    }

    @Test
    void handlesEmptyMapWithPositiveTopNRanks() {
        DefaultCookieAnalyzer analyzer = new DefaultCookieAnalyzer(null);
        Map<String, Integer> cookiesFrequency = Map.of();
        List<String> result = analyzer.findMostActiveCookies(cookiesFrequency, 3);
        assertTrue(result.isEmpty());
    }

    @Test
    void handlesSingleCookieInMap() {
        DefaultCookieAnalyzer analyzer = new DefaultCookieAnalyzer(null);
        Map<String, Integer> cookiesFrequency = Map.of(
                "cookie1", 5
        );
        List<String> result = analyzer.findMostActiveCookies(cookiesFrequency, 3);
        assertEquals(List.of("cookie1"), result);
    }
}