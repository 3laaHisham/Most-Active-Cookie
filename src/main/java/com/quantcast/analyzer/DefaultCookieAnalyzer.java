package com.quantcast.analyzer;

import com.quantcast.observer.EventObserver;

import java.util.*;

public class DefaultCookieAnalyzer implements CookieAnalyzer {
    private final EventObserver observer;

    public DefaultCookieAnalyzer(EventObserver observer) {
        this.observer = observer;
    }

    @Override
    public List<String> findMostActiveCookies(Map<String, Integer> cookiesFrequency, int topNRanks) {
        if (cookiesFrequency.isEmpty()) return List.of();

        // Find the maximum top N frequency
        return cookiesFrequency.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topNRanks)
                .map(Map.Entry::getKey).
                toList();
    }
}
