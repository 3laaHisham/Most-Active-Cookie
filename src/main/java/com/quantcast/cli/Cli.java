package com.quantcast.cli;

import com.quantcast.utils.DateRange;
import com.quantcast.utils.DateRangeParser;
import org.apache.commons.cli.*;

public class Cli {

    private final Options options;

    public Cli() {
        this.options = defineOptions();
    }

    public Config parse(String[] args) throws ParseException, IllegalArgumentException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        String filePath = cmd.getOptionValue("f");
        DateRange dateRange = DateRangeParser.parse(cmd.getOptionValue("d"));
        int topNRanks = Integer.parseInt(cmd.getOptionValue("t", "1"));

        return new Config(filePath, dateRange, topNRanks);

    }

    private Options defineOptions() {
        Options opts = new Options();

        opts.addRequiredOption("f", "file", true, "Path to cookie log CSV (required)");
        opts.addRequiredOption("d", "date", true, "Single or range date, e.g. 2025-05-16 or 2025-05-10:2025-05-12");
        opts.addOption("t", "top", true, "Top N ranks (default 1)");
        opts.addOption("h", "help", false, "Show help");

        return opts;
    }

    public record Config(String filePath, DateRange range, int topNRanks) {
    }
}
