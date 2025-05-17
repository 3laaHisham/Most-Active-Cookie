//package com.quantcast.analyzer;
//
//import com.quantcast.parser.CookieLogEntry;
//import org.junit.jupiter.api.Test;
//
//import java.time.OffsetDateTime;
//import java.util.List;
//
//import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class DefaultCookieAnalyzerTest {
//
//    private final CookieAnalyzer analyzer = new DefaultCookieAnalyzer();
//
//    private CookieLogEntry entry(String cookie, String isoDateTime) {
//        return new CookieLogEntry(cookie, OffsetDateTime.parse(isoDateTime, ISO_OFFSET_DATE_TIME));
//    }
//
//    @Test
//    void returnsSingleMostActiveCookie() {
//        List<CookieLogEntry> entries = List.of(
//                entry("a", "2025-05-16T08:00:00+00:00"),
//                entry("a", "2025-05-16T10:00:00+00:00"),
//                entry("b", "2025-05-16T11:00:00+00:00")
//        );
//
//        List<String> result = analyzer.findMostActiveCookies(entries, java.time.LocalDate.parse("2025-05-16"));
//        assertEquals(List.of("a"), result);
//    }
//
//    @Test
//    void returnsMultipleCookiesOnTie() {
//        List<CookieLogEntry> entries = List.of(
//                entry("x", "2025-05-16T08:00:00+00:00"),
//                entry("x", "2025-05-16T08:00:00+00:00"),
//                entry("y", "2025-05-16T09:00:00+00:00"),
//                entry("y", "2025-05-16T10:00:00+00:00")
//        );
//
//        List<String> result = analyzer.findMostActiveCookies(entries, java.time.LocalDate.parse("2025-05-16"));
//        assertEquals(List.of("x", "y"), result);
//    }
//
//    @Test
//    void returnsEmptyListWhenNoMatch() {
//        List<CookieLogEntry> entries = List.of(
//                entry("a", "2025-05-14T08:00:00+00:00")
//        );
//
//        List<String> result = analyzer.findMostActiveCookies(entries, java.time.LocalDate.parse("2025-05-16"));
//        assertTrue(result.isEmpty());
//    }
//}
