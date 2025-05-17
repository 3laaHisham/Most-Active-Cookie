package com.quantcast.analyzer;

import com.quantcast.parser.CookieLogEntry;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultCookieAnalyzer implements CookieAnalyzer {

    @Override
    public List<String> findMostActiveCookies(List<CookieLogEntry> entries, LocalDate date) {
        Map<String, Integer> counts = new HashMap<>();

        for (CookieLogEntry entry : entries) {
            if (entry.getTimestamp().toLocalDate().equals(date)) {
                counts.merge(entry.getCookie(), 1, Integer::sum);
            }
        }

        if (counts.isEmpty()) return List.of();

        int maxCount = Collections.max(counts.values());

        return counts.entrySet().stream()
                .filter(e -> e.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }
}
