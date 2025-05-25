package com.quantcast.utils;

public record CookieResult(String name) {
    public CookieResult {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Cookie name cannot be null or blank");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
