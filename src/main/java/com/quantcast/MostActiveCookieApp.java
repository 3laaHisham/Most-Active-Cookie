package com.quantcast;

import com.quantcast.analyzer.CookieAnalyzer;
import com.quantcast.analyzer.DefaultCookieAnalyzer;
import com.quantcast.output.ResultPrinter;
import com.quantcast.output.StdoutResultPrinter;
import com.quantcast.parser.CookieLogEntry;
import com.quantcast.parser.CsvCookieLogParser;
import com.quantcast.parser.CookieLogParser;
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

            // parse
            CookieLogParser logParser = new CsvCookieLogParser();
            List<CookieLogEntry> entries = logParser.parse(file);

            // analyze
            CookieAnalyzer analyzer = new DefaultCookieAnalyzer();
            List<String> topCookies = analyzer.findMostActiveCookies(entries, date);

            // print
            ResultPrinter printer = new StdoutResultPrinter();
            printer.print(topCookies);

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
