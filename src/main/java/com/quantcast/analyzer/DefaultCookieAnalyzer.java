package com.quantcast.analyzer;

import com.quantcast.parser.CookieLogEntry;
import com.quantcast.utils.DateRange;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultCookieAnalyzer implements CookieAnalyzer {

    @Override
    public List<String> findMostActiveCookies(Map<String, Integer> cookiesFrequency, int topN) {
        if (cookiesFrequency.isEmpty()) return List.of();

        // Find the maximum top N frequency

        return cookiesFrequency.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey).
                toList();
    }
}
