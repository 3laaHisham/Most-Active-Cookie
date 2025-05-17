package com.quantcast.output;

import java.util.List;

/**
 * Handles printing of the analysis results.
 */
public interface ResultPrinter {
    /**
     * Print the list of top cookies.
     *
     * @param cookies the cookies to print
     */
    void print(List<String> cookies);
}
