package com.quantcast.parser;

import java.io.IOException;
import java.util.List;

public interface CookieLogParser {

    /**
     * Reads and parses the cookie log from the given file path.
     *
     * @param filePath path to the CSV log file
     * @return list of parsed CookieLogEntry objects
     * @throws IOException if the file cannot be read
     */
    List<CookieLogEntry> parse(String filePath) throws IOException;
}
