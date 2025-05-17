package com.quantcast;

import com.quantcast.analyzer.CookieAnalyzer;
import com.quantcast.analyzer.DefaultCookieAnalyzer;
import com.quantcast.cli.CliParser;
import com.quantcast.observer.ConsoleListener;
import com.quantcast.observer.CookieAppObserver;
import com.quantcast.observer.LoggerListener;
import com.quantcast.parser.CookieLogEntry;
import com.quantcast.parser.CsvCookieLogParser;
import com.quantcast.utils.DateRange;
import org.apache.commons.cli.*;

import java.util.List;

public class MostActiveCookieApp {
    public static void main(String[] args) {
        var cli = new CliParser();
        var config = cli.parse(args);

        try {
            String file = config.filePath();
            List<DateRange> dateRanges = config.ranges();
            int topN = config.topN();
            if (file == null || dateRanges == null) {
                throw new ParseException("Both -f and -d are required");
            }

            // setup and parse with listener
            CookieAppObserver observer = new CookieAppObserver();
            observer.addListener(new ConsoleListener());
            observer.addListener(new LoggerListener());

            CsvCookieLogParser logParser = new CsvCookieLogParser(observer);
            var cookiesFrequency = logParser.countFrequencies(file, dateRanges);

            // analyze
            CookieAnalyzer analyzer = new DefaultCookieAnalyzer();
            List<String> topCookies = analyzer.findMostActiveCookies(cookiesFrequency, topN);

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
