package com.quantcast.analyzer;

import com.quantcast.parser.CookieLogEntry;
import com.quantcast.utils.DateRange;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for analyzing cookie activity.
 */
public interface CookieAnalyzer {

    /**
     * Returns the most active cookie(s) on the given date.
     *
     * @param cookiesFrequency map of cookie -> frequency
     * @param topN number of top cookies to return
     * @return list of cookie values with the highest frequency
     */
    List<String> findMostActiveCookies(Map<String, Integer> cookiesFrequency, int topN);
}
