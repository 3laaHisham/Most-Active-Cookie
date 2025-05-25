package com.quantcast.analyzer;

import com.quantcast.observer.EventObserver;
import com.quantcast.utils.CookieResult;

import java.util.*;

public class DefaultCookieAnalyzer implements CookieAnalyzer {
    private final EventObserver observer;

    public DefaultCookieAnalyzer(EventObserver observer) {
        this.observer = observer;
    }

    @Override
    public List<CookieResult> findMostActiveCookies(Map<String, Integer> cookiesFrequency, int topNRanks) {
        int numUniqueElements = cookiesFrequency.size();
        observer.analysisStarted(numUniqueElements);

        // Find the maximum top N frequency
        return cookiesFrequency.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topNRanks)
                .map(entry -> new CookieResult(entry.getKey())).
                toList();
    }
}
