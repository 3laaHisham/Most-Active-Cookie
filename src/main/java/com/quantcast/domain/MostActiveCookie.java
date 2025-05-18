package com.quantcast.domain;

import com.quantcast.analyzer.CookieAnalyzer;
import com.quantcast.cli.Cli;
import com.quantcast.observer.ConsoleListener;
import com.quantcast.observer.EventObserver;
import com.quantcast.observer.LoggerListener;
import com.quantcast.parser.CookieLogParser;
import com.quantcast.utils.DateRange;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

// TODO: EXCEPTION HANDLING
public class MostActiveCookie {

    private final Cli cli;
    private final EventObserver observer;
    private final CookieLogParser logParser;
    private final CookieAnalyzer analyzer;

    public MostActiveCookie(Cli cli, EventObserver observer, CookieLogParser logParser, CookieAnalyzer analyzer) {
        this.cli = cli;
        this.observer = observer;
        this.logParser = logParser;
        this.analyzer = analyzer;
    }

    public void run(String[] args) throws ParseException, IOException, IllegalArgumentException {
        var config = cli.parse(args);

        String file = config.filePath();
        DateRange dateRange = config.range();
        int topNRanks = config.topNRanks();

        if (file == null || dateRange == null)
            throw new ParseException("Both -f and -d are required");
        if (topNRanks < 1)
            throw new ParseException("Top N must be greater than 0");

        // setup and parse with listener
        var cookiesFrequency = logParser.countFrequencies(file, dateRange);

        // analyze
        var topCookies = analyzer.findMostActiveCookies(cookiesFrequency, topNRanks);

        // print
        observer.addListener(new ConsoleListener());
        observer.resultReady(topCookies);
    }


}
