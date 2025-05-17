package com.quantcast.analyzer;

import com.quantcast.parser.CookieLogEntry;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for analyzing cookie activity.
 */
public interface CookieAnalyzer {

    /**
     * Returns the most active cookie(s) on the given date.
     *
     * @param entries list of parsed cookie entries
     * @param date    the date to filter by
     * @return list of cookie values with the highest frequency
     */
    List<String> findMostActiveCookies(List<CookieLogEntry> entries, LocalDate date);
}
