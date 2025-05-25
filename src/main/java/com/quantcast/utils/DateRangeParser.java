package com.quantcast.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses a single date or date-range string and returns a DateRange.
 * Input formats supported:
 * - "YYYY-MM-DD" (single date)
 * - "YYYY-MM-DD:YYYY-MM-DD" (date range)
 * If the end date is omitted or empty, it defaults to the start date.
 */
public class DateRangeParser {

    /**
     * Parses a raw string representing exactly one date or date-range.
     *
     * @param raw the raw input string
     * @return the parsed DateRange
     * @throws IllegalArgumentException if the format is invalid or parsing fails
     */
    public static DateRange parse(String raw) throws IllegalArgumentException {
        String[] parts = extractParts(raw);

        LocalDate start = getStartDate(parts);

        LocalDate end = getEndDate(parts, start);

        return new DateRange(start, end);
    }


    private static String[] extractParts(String raw) throws IllegalArgumentException {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Input date string is null or empty");
        }

        String trimmed = raw.trim();
        String[] parts = trimmed.split(":", -1);
        if (parts.length < 1 || parts.length > 2) {
            throw new IllegalArgumentException("Invalid date range format: " + raw);
        }

        return parts;
    }

    private static LocalDate getStartDate(String[] parts) {
        LocalDate start;
        try {
            start = LocalDate.parse(parts[0]);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid start date format: " + parts[0], e);
        }
        return start;
    }

    private static LocalDate getEndDate(String[] parts, LocalDate start) {
        LocalDate end;
        if (parts.length == 1 || parts[1].isEmpty()) {
            end = start;
        } else {
            try {
                end = LocalDate.parse(parts[1]);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid end date format: " + parts[1], e);
            }
        }
        return end;
    }

}
