package com.cryptoanalyzer.aharahimova.utils;

import java.util.Random;

public class TestUtils {
    private static Random random = new Random();
    private static final int MIN_KEY = 1;
    private static final int MAX_KEY = 70;

    public static int getRandomKey() {
        return random.nextInt(MIN_KEY, MAX_KEY);
    }
}
