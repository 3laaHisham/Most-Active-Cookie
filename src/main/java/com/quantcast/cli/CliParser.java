package com.quantcast.cli;

import com.quantcast.utils.DateRange;
import org.apache.commons.cli.*;

import java.util.List;

public class CliParser {

    public record Config(String filePath, List<DateRange> ranges, int topN) {}

    private final Options options;
    private final HelpFormatter help;

    public CliParser() {
        this.options = defineOptions();
        this.help = new HelpFormatter();
    }

    public Config parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h") || !cmd.hasOption("f") || !cmd.hasOption("d")) {
                printAndExit(0);
            }

            String filePath = cmd.getOptionValue("f");
            List<DateRange> ranges = DateRange.parse(cmd.getOptionValue("d"));
            int topN = Integer.parseInt(cmd.getOptionValue("t", "1"));

            return new Config(filePath, ranges, topN);

        } catch (ParseException | IllegalArgumentException e) {
            System.err.println("Argument error: " + e.getMessage());

            printAndExit(1);

            return null;
        }

    }


    private void printAndExit(int exitCode) {
        help.printHelp("most-active-cookie", options);
        System.exit(exitCode);
    }

    private Options defineOptions() {
        Options opts = new Options();
        opts.addOption("f", "file",  true, "Path to cookie log CSV (required)");
        opts.addOption("d", "dates", true, "Dates or ranges, e.g. 2025-05-16,2025-05-10:2025-05-12");
        opts.addOption("t", "top",   true, "Top N cookies (default 1)");
        opts.addOption("h", "help",  false, "Show help");
        return opts;
    }
}
