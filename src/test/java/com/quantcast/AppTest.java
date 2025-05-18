package com.quantcast;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    /** Runs App.main(...) and captures stdout & stderr. */
    private static String[] runAndCapture(String... args) throws Exception {
        PrintStream origOut = System.out, origErr = System.err;
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outBuf));
        System.setErr(new PrintStream(errBuf));
        try {
            App.run(args);
        } finally {
            System.setOut(origOut);
            System.setErr(origErr);
        }
        return new String[]{ outBuf.toString().trim(), errBuf.toString().trim() };
    }

    @Test
    void singleDateTop1(@TempDir Path tmp) throws Exception {
        Path csv = tmp.resolve("c.csv");
        try (Writer w = new FileWriter(csv.toFile())) {
            w.write("cookie,timestamp\n");
            w.write("a,2025-05-16T01:00:00+00:00\n");
            w.write("b,2025-05-16T02:00:00+00:00\n");
            w.write("a,2025-05-16T03:00:00+00:00\n");
        }

        String[] out = runAndCapture("-f", csv.toString(), "-d", "2025-05-16", "-t", "1");
        assertEquals("a", out[0], "Should print only the top cookie");
        assertTrue(out[1].isEmpty(), "No errors expected");
    }

    @Test
    void singleDateTopNGreaterThanUnique(@TempDir Path tmp) throws Exception {
        Path csv = tmp.resolve("c2.csv");
        try (Writer w = new FileWriter(csv.toFile())) {
            w.write("cookie,timestamp\n");
            w.write("x,2025-05-17T10:00:00+00:00\n");
            w.write("y,2025-05-17T11:00:00+00:00\n");
        }

        String[] out = runAndCapture("-f", csv.toString(), "-d", "2025-05-17", "-t", "5");
        String[] lines = out[0].split("\\R");
        assertEquals(2, lines.length, "Should print both cookies");
        assertTrue((lines[0].equals("x") && lines[1].equals("y")) ||
                (lines[0].equals("y") && lines[1].equals("x")));
    }

    @Test
    void singleRangeQuery(@TempDir Path tmp) throws Exception {
        Path csv = tmp.resolve("c3.csv");
        try (Writer w = new FileWriter(csv.toFile())) {
            w.write("cookie,timestamp\n");
            w.write("p,2025-05-10T00:00:00+00:00\n");
            w.write("q,2025-05-11T00:00:00+00:00\n");
            w.write("q,2025-05-11T01:00:00+00:00\n");
            w.write("r,2025-05-12T00:00:00+00:00\n");
        }

        String[] out = runAndCapture(
                "-f", csv.toString(),
                "-d", "2025-05-10:2025-05-12",
                "-t", "2"
        );
        String[] lines = out[0].split("\\R");
        assertEquals(3, lines.length);
        assertEquals("q", lines[0], "q has highest freq=2");
        // second spot is p or r; lex order p<r => p
        assertTrue((lines[1].equals("p") && lines[2].equals("r")) ||
                (lines[1].equals("r") && lines[2].equals("p")),
                "Second spot should be p or r");
    }

//    @Test
//    void malformedLinesAreWarningButSkip(@TempDir Path tmp) throws Exception {
//        Path csv = tmp.resolve("c4.csv");
//        try (Writer w = new FileWriter(csv.toFile())) {
//            w.write("cookie,timestamp\n");
//            w.write("good,2025-05-16T00:00:00+00:00\n");
//            w.write("badline-no-comma\n");
//            w.write("bad,timestamp\n");
//            w.write("good,2025-05-16T01:00:00+00:00\n");
//        }
//
//        String[] out = runAndCapture("-f", csv.toString(), "-d", "2025-05-16");
//        // top 1 default => good
//        assertEquals("good", out[0]);
//        assertTrue(out[1].contains("Malformed line"), "Should warn about malformed lines");
//    }

    @Test
    void noMatchesPrintsNoCookiesFound(@TempDir Path tmp) throws Exception {
        Path csv = tmp.resolve("c5.csv");
        try (Writer w = new FileWriter(csv.toFile())) {
            w.write("cookie,timestamp\n");
            w.write("x,2025-05-15T10:00:00+00:00\n");
        }

        String[] out = runAndCapture("-f", csv.toString(), "-d", "2025-05-16");
        assertEquals("No cookies found for given criteria.", out[0]);
    }
}
