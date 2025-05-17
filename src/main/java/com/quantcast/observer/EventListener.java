package com.quantcast.observer;

/**
 * A very simple API for all reporting:
 *   - info-level events
 *   - warning-level events
 *   - error-level events
 *   - exceptions
 */
public interface EventListener {
    void info(String message);
    void warn(String message);
    void error(String message);
    void exception(Throwable t);
}
