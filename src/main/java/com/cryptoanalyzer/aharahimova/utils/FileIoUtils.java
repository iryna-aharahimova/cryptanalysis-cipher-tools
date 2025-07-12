package com.cryptoanalyzer.aharahimova.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileIoUtils {

    private FileIoUtils() {}

    public static String readFile(Path path) throws IOException {
        return Files.readString(path);
    }

    public static void writeFile(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }
}