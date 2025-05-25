package com.quantcast.observer;

import com.quantcast.utils.CookieResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dispatches CookieAppEvents to one or more EventListener implementations.
 */
public class CookieAppObserver implements EventObserver {
    private final List<EventListener> listeners = new ArrayList<>();

    public CookieAppObserver(EventListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    @Override
    public void addListener(EventListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void parsingStarted(String filePath) {
        for (var l : listeners) l.info("▶️ Starting parse: " + filePath);
    }

    @Override
    public void parsingProgress(double pct) {
        for (var l : listeners) l.info(String.format("🔄 Progress: %.0f%%", pct));
    }

    @Override
    public void lineMalformed(String raw) {
        for (var l : listeners) l.warn("⚠️ Malformed line #" + raw);
    }

    @Override
    public void parsingCompleted(long total, long malformed) {
        for (var l : listeners)
            l.info("✅ Parsed " + total + " date matching lines, " + malformed + " malformed");
    }

    @Override
    public void analysisStarted(int entries) {
        for (var l : listeners) l.info("▶️ Analyzing " + entries + " entries");
    }

    @Override
    public void analysisCompleted(int unique, int windowSize) {
        for (var l : listeners)
            l.info("✅ Analysis: " + unique + " matching cookies, result size: " + windowSize);
    }

    @Override
    public void resultReady(List<CookieResult> topCookies) {
        for (var l : listeners) {
            if (topCookies.isEmpty()) {
                l.info("No cookies found for given criteria.\n");
            } else {
                l.info(topCookies.stream().map(CookieResult::toString)
                        .collect(Collectors.joining("\n")));
            }
        }
    }

    @Override
    public void handleException(Throwable t) {
        for (var l : listeners) l.exception(t);
    }
}
