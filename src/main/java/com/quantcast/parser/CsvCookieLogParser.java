package com.quantcast.parser;

import com.quantcast.observer.EventObserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvCookieLogParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final EventObserver listener;

    public CsvCookieLogParser(EventObserver listener) {
        this.listener = listener;
    }

    public List<CookieLogEntry> parse(String filePath) throws IOException {
        listener.parsingStarted(filePath);
        List<CookieLogEntry> entries = new ArrayList<>();
        long total = 0, malformed = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                total++;
                String[] parts = line.split(",", 2);
                if (parts.length < 2) {
                    malformed++;
                    listener.lineMalformed(total, line);
                    continue;
                }
                try {
                    entries.add(new CookieLogEntry(parts[0],
                            OffsetDateTime.parse(parts[1], FORMATTER)));
                } catch (Exception e) {
                    malformed++;
                }
            }
        }
        listener.parsingCompleted(total, malformed);
        return entries;
    }
}
