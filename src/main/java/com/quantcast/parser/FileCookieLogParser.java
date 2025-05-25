package com.quantcast.parser;

import com.quantcast.observer.EventObserver;
import com.quantcast.utils.DateRange;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Parses a CSV cookie log and counts cookie occurrences within a given date range.
 */
public class FileCookieLogParser implements CookieLogParser {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final int COOKIE_NAME_INDEX = 0;
    private final int TIMESTAMP_INDEX = 1;

    private final EventObserver observer;

    public FileCookieLogParser(EventObserver observer) {
        this.observer = Objects.requireNonNull(observer, "listener cannot be null");
    }

    @Override
    public Map<String, Integer> countFrequencies(String filePath, DateRange range) throws IOException {
        observer.parsingStarted(filePath);

        Map<String, Integer> freqMap = new HashMap<>();

        long totalLines = 0;
        long malformedLines = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip header

            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",", 2);
                if (parts.length != 2) {
                    observer.lineMalformed(line);
                    malformedLines++;

                    continue;
                }

                try {
                    LocalDate lineDate = OffsetDateTime.parse(parts[TIMESTAMP_INDEX], TIMESTAMP_FORMATTER)
                            .toLocalDate();

                    if (lineDate.isAfter(range.end()))
                        continue;
                    if (lineDate.isBefore(range.start()))
                        break;

                } catch (DateTimeParseException e) {
                    observer.lineMalformed(line);
                    malformedLines++;

                    continue;
                }

                totalLines++;

                String cookie = parts[COOKIE_NAME_INDEX];
                freqMap.merge(cookie, 1, Integer::sum);
            }
        }

        observer.parsingCompleted(totalLines, malformedLines);

        return freqMap;
    }

}
