package com.cryptoanalyzer.aharahimova.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class TestUtils {
    private static Random random = new Random();
    private static final int MIN_KEY = 1;
    private static final int MAX_KEY = 70;

    public static int getRandomKey() {
        return random.nextInt(MIN_KEY, MAX_KEY);
    }

    public static Path createTempFileWithContent(String prefix, String suffix, String content) throws IOException {
        Path tempFile = Files.createTempFile(prefix, suffix);
        Files.writeString(tempFile, content);
        return tempFile;
    }

    public static void deleteIfExists(Path... paths) throws IOException {
        for (Path path : paths) {
            if (path != null) Files.deleteIfExists(path);
        }
    }
}
