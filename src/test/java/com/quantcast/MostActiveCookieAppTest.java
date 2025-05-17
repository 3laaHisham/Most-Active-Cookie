package com.quantcast;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MostActiveCookieAppOutputTest {

    private String captureStdout(Runnable runnable) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        try {
            runnable.run();
        } finally {
            System.setOut(originalOut);
        }
        return outContent.toString().trim();
    }

    @Test
    void testAppPrintsMostActiveCookie() throws Exception {
        Path temp = Files.createTempFile("log", ".csv");
        try (FileWriter writer = new FileWriter(temp.toFile())) {
            writer.write("cookie,timestamp\n");
            writer.write("abc,2025-05-16T10:00:00+00:00\n");
            writer.write("xyz,2025-05-16T11:00:00+00:00\n");
            writer.write("abc,2025-05-16T12:00:00+00:00\n");
        }

        String output = captureStdout(() -> MostActiveCookieApp.main(new String[]{
                "-f", temp.toString(),
                "-d", "2025-05-16"
        }));

        assertEquals("abc", output);
    }

    @Test
    void testAppPrintsMultipleCookiesOnTie() throws Exception {
        Path temp = Files.createTempFile("log", ".csv");
        try (FileWriter writer = new FileWriter(temp.toFile())) {
            writer.write("cookie,timestamp\n");
            writer.write("a,2025-05-16T10:00:00+00:00\n");
            writer.write("b,2025-05-16T11:00:00+00:00\n");
        }

        String output = captureStdout(() -> MostActiveCookieApp.main(new String[]{
                "-f", temp.toString(),
                "-d", "2025-05-16"
        }));

        assertTrue(output.contains("a"));
        assertTrue(output.contains("b"));
        assertEquals(2, output.split("\\R").length); // two lines
    }

    @Test
    void testAppPrintsNoCookiesFound() throws Exception {
        Path temp = Files.createTempFile("log", ".csv");
        try (FileWriter writer = new FileWriter(temp.toFile())) {
            writer.write("cookie,timestamp\n");
            writer.write("x,2025-05-15T10:00:00+00:00\n");
        }

        String output = captureStdout(() -> MostActiveCookieApp.main(new String[]{
                "-f", temp.toString(),
                "-d", "2025-05-16"
        }));

        assertEquals("No cookies found for the specified criteria.", output);
    }
}
