package com.quantcast.observer;

import java.util.List;

/**
 * Defines all application‑level events your cookie tool can fire.
 */
public interface EventObserver {
    // parsing
    void parsingStarted(String filePath);
    void parsingProgress(double pct);
    void lineMalformed(long lineNo, String rawLine);
    void parsingCompleted(long totalLines, long malformed);

    // analysis
    void analysisStarted(int entryCount);
    void analysisCompleted(int uniqueCookies, int windowSize);

    // results
    void resultReady(List<String> topCookies);

    // errors
    void handleException(Throwable t);
}
