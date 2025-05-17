package com.quantcast.output;

import java.util.List;

/**
 * Prints results to standard output, one per line.
 */
public class StdoutResultPrinter implements ResultPrinter {
    @Override
    public void print(List<String> cookies) {
        if (cookies.isEmpty()) {
            System.out.println("No cookies found for the specified criteria.");
        } else {
            for (String cookie : cookies) {
                System.out.println(cookie);
            }
        }
    }
}
