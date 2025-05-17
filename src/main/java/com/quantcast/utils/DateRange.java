package com.quantcast.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record DateRange(LocalDate start, LocalDate end) {
    public DateRange {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
    }

    public static List<DateRange> parse(String raw) {
        String[] parts = raw.split(",");

        List<DateRange> ranges = new ArrayList<>();
        for (String part : parts) {
            String[] dates = part.split(":");

            LocalDate startDate = LocalDate.parse(dates[0]);
            LocalDate endDate = dates.length > 1 ? LocalDate.parse(dates[1]) : startDate;

            ranges.add(new DateRange(startDate, endDate));
        }

        ranges.sort(Comparator.comparing((DateRange r) -> r.start).thenComparing(r -> r.end));

        // Merge overlapping ranges
        List<DateRange> mergedRanges = new ArrayList<>();
        DateRange lastRange = null;
        for (DateRange range : ranges) {
            if (lastRange == null || !lastRange.end.isAfter(range.start)) {
                mergedRanges.add(range);
                lastRange = range;
            } else {
                lastRange = new DateRange(lastRange.start, lastRange.end.isAfter(range.end) ? lastRange.end : range.end);
                mergedRanges.set(mergedRanges.size() - 1, lastRange);
            }
        }

        return ranges;
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(start) && !date.isAfter(end);
    }

    @Override
    public String toString() {
        return start + (start.equals(end) ? "" : ":" + end);
    }
}
