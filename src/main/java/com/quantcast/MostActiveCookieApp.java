package com.quantcast;

import com.quantcast.analyzer.CookieAnalyzer;
import com.quantcast.analyzer.DefaultCookieAnalyzer;
import com.quantcast.observer.ConsoleListener;
import com.quantcast.observer.CookieAppObserver;
import com.quantcast.observer.LoggerListener;
import com.quantcast.parser.CookieLogEntry;
import com.quantcast.parser.CsvCookieLogParser;
import org.apache.commons.cli.*;

import java.time.LocalDate;
import java.util.List;

public class MostActiveCookieApp {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("f", "file", true, "Path to cookie log CSV");
        options.addOption("d", "date", true, "Target date in YYYY-MM-DD");
        options.addOption("h", "help", false, "Show help");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter help = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) {
                help.printHelp("most-active-cookie", options);
                return;
            }

            String file = cmd.getOptionValue("f");
            String dateStr = cmd.getOptionValue("d");
            if (file == null || dateStr == null) {
                throw new ParseException("Both -f and -d are required");
            }

            LocalDate date = LocalDate.parse(dateStr);

            // setup and parse with listener
            CookieAppObserver observer = new CookieAppObserver();
            observer.addListener(new ConsoleListener());
            observer.addListener(new LoggerListener());

            CsvCookieLogParser logParser = new CsvCookieLogParser(observer);
            List<CookieLogEntry> entries = logParser.parse(file);

            // analyze
            CookieAnalyzer analyzer = new DefaultCookieAnalyzer();
            List<String> topCookies = analyzer.findMostActiveCookies(entries, date);

            // print
            observer.resultReady(topCookies);

        } catch (ParseException e) {
            System.err.println("Argument error: " + e.getMessage());
            help.printHelp("most-active-cookie", options);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        }
    }
}
