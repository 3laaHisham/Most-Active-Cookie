package com.quantcast.parser;

import com.quantcast.utils.DateRange;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CookieLogParser {

    /**
     * Streams through the CSV, counting cookie frequencies only for timestamps
     * that fall within any of the provided date ranges.
     *
     * @param filePath path to the CSV file
     * @param range    date range to filter by
     * @return map of cookie -> count
     */
    public Map<String, Integer> countFrequencies(
            String filePath,
            DateRange range
    ) throws IOException;
}
