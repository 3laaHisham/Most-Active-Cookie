package com.quantcast.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs parse events via SLF4J.
 */
public class LoggingListener implements ParsingListener {
    private static final Logger log = LoggerFactory.getLogger(LoggingListener.class);

    @Override
    public void onStart(String filePath) {
        log.info("▶️ Starting parse of {}", filePath);
    }

    @Override
    public void onMalformedLine(long lineNumber, String rawLine) {
        log.warn("⚠️ Skipped malformed line #{}: {}", lineNumber, rawLine);
    }

    @Override
    public void onComplete(long totalLines, long malformedLines) {
        log.info("✅ Completed parse: {} lines, {} malformed", totalLines, malformedLines);
    }
}
