package com.quantcast.logging;

/**
 * Callback interface for parsing lifecycle events.
 */
public interface ParsingListener {
    /** Called once, when parsing starts. */
    void onStart(String filePath);

    /** Called for each malformed line. */
    void onMalformedLine(long lineNumber, String rawLine);

    /** Called once, when parsing completes. */
    void onComplete(long totalLines, long malformedLines);
}
