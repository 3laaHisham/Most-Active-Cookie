package com.quantcast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    @Test
    public void testMainRuns() {
        // Just ensure main doesn't throw
        App.main(new String[]{});
    }
}
