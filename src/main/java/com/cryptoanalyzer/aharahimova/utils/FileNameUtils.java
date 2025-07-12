package com.cryptoanalyzer.aharahimova.utils;

import com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants;

import java.nio.file.Path;

public final class FileNameUtils {

    private FileNameUtils() {
    }

    public static Path getOutputPath(Path inputPath, String newSuffix) {
        String fileName = inputPath.getFileName().toString();
        String extension = extractExtension(fileName);
        String baseName = extractBaseName(fileName);
        String cleanedBaseName = removeKnownSuffix(baseName);

        String newFileName = cleanedBaseName + newSuffix + extension;
        return inputPath.getParent().resolve(newFileName);
    }

    public static String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    public static String extractBaseName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    public static String removeKnownSuffix(String baseName) {
        String[] suffixes = {
                FileSuffixesConstants.ENCRYPTED,
                FileSuffixesConstants.DECRYPTED,
                FileSuffixesConstants.BRUTE_FORCE
        };

        for (String suffix : suffixes) {
            if (baseName.endsWith(suffix)) {
                return baseName.substring(0, baseName.length() - suffix.length());
            }
        }
        return baseName;
    }
}