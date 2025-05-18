package com.quantcast.analyzer;

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
     * @param topNRanks number of top cookies to return
     * @return list of cookie values with the highest frequency
     */
    List<String> findMostActiveCookies(Map<String, Integer> cookiesFrequency, int topNRanks);
}
