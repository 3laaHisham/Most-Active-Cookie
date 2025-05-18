package com.quantcast.observer;

public class ConsoleListener implements EventListener {
    @Override
    public void info(String message) {
        System.out.println(message);
    }

    @Override
    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }

    @Override
    public void error(String message) {
        System.err.println("[ERROR] " + message);
    }

    @Override
    public void exception(Throwable t) {
        System.err.println("[EXCEPTION] " + t.getMessage());

    }
}
