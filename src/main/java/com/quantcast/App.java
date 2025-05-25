package com.quantcast;

import com.quantcast.cli.Cli;
import com.quantcast.domain.MostActiveCookie;
import com.quantcast.observer.*;
import com.quantcast.parser.CookieLogParser;
import com.quantcast.analyzer.CookieAnalyzer;
import com.quantcast.analyzer.HeapCookieAnalyzer;
import com.quantcast.parser.FileCookieLogParser;

public class App {
    public static void main(String[] args) {
        System.exit(run(args));
    }

    /**
     * Executes the application logic and returns an exit code.
     * 0 = success, non-zero = error.
     */
    public static int run(String[] args) {
        Cli cli = new Cli();
        EventObserver observer = getCookieAppObserver();
        CookieLogParser parser = new FileCookieLogParser(observer);
        CookieAnalyzer analyzer = new HeapCookieAnalyzer(observer);

        try {
            MostActiveCookie mostActive = new MostActiveCookie(cli, observer, parser, analyzer);
            mostActive.run(args);

            return 0;
        } catch (Exception e) {
            observer.addListener(new ConsoleListener());
            observer.handleException(e);

            return 2;
        }
    }

    private static CookieAppObserver getCookieAppObserver() {
        CookieAppObserver obs = new CookieAppObserver();
        obs.addListener(new LoggerListener());

        return obs;
    }
}
