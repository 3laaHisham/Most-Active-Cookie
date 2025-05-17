package com.quantcast.parser;

import com.quantcast.observer.EventObserver;
import com.quantcast.utils.DateRange;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvCookieLogParser implements CookieLogParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final EventObserver listener;

    public CsvCookieLogParser(EventObserver listener) {
        this.listener = listener;
    }

    public Map<String, Integer> countFrequencies(
            String filePath,
            List<DateRange> ranges
    ) throws IOException {
        listener.parsingStarted(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            skipHeader(reader);

            Map<String,Integer> freqMap = new HashMap<>();
            long totalLines     = 0;
            long malformedLines = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                if (processLine(line, ranges)) {
                    String[] parts = line.split(",", 2);
                    String cookie = parts[0];
                    freqMap.put(cookie, freqMap.getOrDefault(cookie, 0) + 1);
                } else {
                    malformedLines++;
                }

                maybeReportProgress(totalLines);
            }

            listener.parsingCompleted(totalLines, malformedLines);
            return freqMap;
        }
    }

    // --- private helpers --- //

    /** Skip the first header line. */
    private void skipHeader(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    /**
     * Parse, filter, and count a single line.
     * @return true if successfully counted; false if malformed or out-of-range
     */
    private boolean processLine(
            String line,
            List<DateRange> ranges
    ) {
        String[] parts = line.split(",", 2);
        if (parts.length < 2) {
            listener.lineMalformed(line);
            return false;
        }

        try {
            OffsetDateTime ts = OffsetDateTime.parse(parts[1], FORMATTER);
            if (inAnyRange(ts.toLocalDate(), ranges)) {
                return true;
            }
        } catch (Exception e) {
            listener.lineMalformed(line);
            return false;
        }

        return false;
    }

    /** Returns true if date falls in any of the given ranges. */
    private boolean inAnyRange(
            java.time.LocalDate date,
            List<DateRange> ranges
    ) {
        for (DateRange r : ranges) {
            if (!date.isBefore(r.start()) && !date.isAfter(r.end())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Optionally report progress. Could be based on line count or bytes read.
     * Here we simply call every 10_000 lines as an example.
     */
    private void maybeReportProgress(long totalLines) {
        if (totalLines % 10_000 == 0) {
            listener.parsingProgress(totalLines);
        }
    }
}
