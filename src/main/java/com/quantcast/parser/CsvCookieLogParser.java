package com.quantcast.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvCookieLogParser implements CookieLogParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public List<CookieLogEntry> parse(String filePath) throws IOException {
        List<CookieLogEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                String[] parts = line.split(",", 2);
                if (parts.length < 2) {
                    // skip malformed
                    continue;
                }
                try {
                    String cookie = parts[0];
                    OffsetDateTime ts = OffsetDateTime.parse(parts[1], FORMATTER);
                    entries.add(new CookieLogEntry(cookie, ts));
                } catch (Exception e) {
                    // skip malformed timestamp
                }
            }
        }
        return entries;
    }
}
