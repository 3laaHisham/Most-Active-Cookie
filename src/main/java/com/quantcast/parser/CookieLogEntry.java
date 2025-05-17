package com.quantcast.parser;

import java.time.OffsetDateTime;

public class CookieLogEntry {
    private final String cookie;
    private final OffsetDateTime timestamp;

    public CookieLogEntry(String cookie, OffsetDateTime timestamp) {
        this.cookie = cookie;
        this.timestamp = timestamp;
    }

    public String getCookie() {
        return cookie;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "CookieLogEntry{" +
                "cookie='" + cookie + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
