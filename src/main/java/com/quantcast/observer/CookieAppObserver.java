package com.quantcast.observer;

import java.util.Arrays;
import java.util.List;

/**
 * Dispatches CookieAppEvents to one or more EventListener implementations.
 */
public class CookieAppObserver implements EventObserver {
    private final List<EventListener> listeners;

    public CookieAppObserver(EventListener... listeners) {
        this.listeners = Arrays.asList(listeners);
    }

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
    public void lineMalformed(long lineNo, String raw) {
        for (var l : listeners) l.warn("⚠️ Malformed line #" + lineNo);
    }

    @Override
    public void parsingCompleted(long total, long malformed) {
        for (var l : listeners)
            l.info("✅ Parsed " + total + " lines, " + malformed + " malformed");
    }

    @Override
    public void analysisStarted(int entries) {
        for (var l : listeners) l.info("▶️ Analyzing " + entries + " entries");
    }

    @Override
    public void analysisCompleted(int unique, int windowSize) {
        for (var l : listeners)
            l.info("✅ Analysis: " + unique + " unique cookies over " + windowSize + " entries");
    }

    @Override
    public void resultReady(List<String> topCookies) {
        for (var l : listeners) {
            if (topCookies.isEmpty()) {
                l.info("No cookies found for given criteria.");
            } else {
                l.info("Top cookies: " + String.join(", ", topCookies));
            }
        }
    }

    @Override
    public void handleException(Throwable t) {
        for (var l : listeners) l.exception(t);
    }
}
