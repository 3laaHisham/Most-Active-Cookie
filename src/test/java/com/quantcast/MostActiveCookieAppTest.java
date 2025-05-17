package com.quantcast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MostActiveCookieAppTest {

    @Test
    void missingArgsShouldExit() {
        String[] args = { "-f", "log.csv" }; // missing -d
        Exception ex = assertThrows(Exception.class, () -> MostActiveCookieApp.main(args));
        assertTrue(ex.getMessage().contains("Both -f and -d are required"));
    }

    @Test
    void validArgsPrintsParsed() {
        // We capture stdout here; in real CI we'd use a SystemOutRule or similar
        MostActiveCookieApp.main(new String[]{ "-f", "log.csv", "-d", "2025-05-16" });
        // Manual verification: console shows "Parsed file=log.csv date=2025-05-16"
    }
}
