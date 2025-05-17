package com.quantcast;

import org.apache.commons.cli.*;

public class MostActiveCookieApp {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("f", "file", true, "Path to cookie log CSV");
        options.addOption("d", "date", true, "Target date in YYYY-MM-DD");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter help = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            String file = cmd.getOptionValue("f");
            String date = cmd.getOptionValue("d");
            if (file == null || date == null) {
                throw new ParseException("Both -f and -d are required");
            }
            System.out.printf("Parsed file=%s date=%s%n", file, date);
            // TODO: invoke parsing & analysis
        } catch (ParseException e) {
            System.err.println("Argument error: " + e.getMessage());
            help.printHelp("most-active-cookie", options);
            System.exit(1);
        }
    }
}
