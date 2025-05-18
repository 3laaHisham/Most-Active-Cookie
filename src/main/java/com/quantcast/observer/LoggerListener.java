package com.quantcast.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerListener implements EventListener {
    private static final Logger log = LoggerFactory.getLogger("CookieApp");

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }

    @Override
    public void exception(Throwable t) {
        log.error("Exception occurred", t);
        if (t.getCause() != null) {
            log.error("Caused by: ", t.getCause());
        }
        if (t.getMessage() != null) {
            log.error("Message: ", t.getMessage());
        }
        // empty line
        log.error("");
    }
}
