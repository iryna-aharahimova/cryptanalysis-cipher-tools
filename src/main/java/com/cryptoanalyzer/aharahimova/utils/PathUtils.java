package com.cryptoanalyzer.aharahimova.utils;

import com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants;

import java.nio.file.Path;

public class PathUtils {

    public static Path getOutputPath(Path inputPath, String newSuffix) {
        String fileName = inputPath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');

        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);

        String[] suffixes = {
                FileSuffixesConstants.ENCRYPTED,
                FileSuffixesConstants.DECRYPTED,
                FileSuffixesConstants.BRUTE_FORCE
        };

        for (String suffix : suffixes) {
            if (baseName.endsWith(suffix)) {
                baseName = baseName.substring(0, baseName.length() - suffix.length());
                break;
            }
        }

        String outputFileName = baseName + newSuffix + extension;
        return inputPath.getParent().resolve(outputFileName);
    }
}