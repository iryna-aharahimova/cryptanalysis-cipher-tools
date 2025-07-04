package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.ERROR;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.OK;

public class BruteForce implements Function {

    private static final List<String> COMMON_WORDS = Arrays.asList(
            "the", "and", "that", "have", "for", "not", "with", "you",
            "this", "but", "his", "from", "they", "say", "her", "she",
            "will", "would", "there", "their", "about", "which", "when",
            "make", "can", "like", "time", "just", "know", "take", "into",
            "your", "good", "some", "could", "them", "see", "other", "than"
    );

    @Override
    public Result execute(String[] parameters) {
        try {
            String inputFilePath = parameters[1];
            Path inputPath = Path.of(inputFilePath);
            String encryptedContent = Files.readString(inputPath);

            for (int key = 1; key < ALPHABET.length(); key++) {
                StringBuilder decoded = getStringBuilder(encryptedContent, key);

                String resultText = decoded.toString().toLowerCase();
                if (containsCommonWords(resultText)) {
                    System.out.println("Brute force succeeded with key: " + key);
                    Path outputPath = getOutputPath(inputPath, "[BRUTE_FORCE]");
                    Files.writeString(outputPath, decoded.toString());
                    return new Result(OK);
                }
            }

            return new Result(ERROR, new ApplicationException("Brute-force failed: no suitable key found"));

        } catch (Exception e) {
            return new Result(ERROR, new ApplicationException("Brute-force operation failed", e));
        }
    }

    private static StringBuilder getStringBuilder(String encryptedContent, int key) {
        StringBuilder decoded = new StringBuilder();
        for (char ch : encryptedContent.toCharArray()) {
            int index = ALPHABET.indexOf(ch);
            if (index != -1) {
                int newIndex = (index - key + ALPHABET.length()) % ALPHABET.length();
                decoded.append(ALPHABET.charAt(newIndex));
            } else {
                decoded.append(ch);
            }
        }
        return decoded;
    }

    private boolean containsCommonWords(String text) {
        String[] words = text.split("[\\s.,!?:\"'«»]+");
        for (String word : words) {
            if (COMMON_WORDS.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private Path getOutputPath(Path inputPath, String suffix) {
        String fileName = inputPath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);
        String outputFileName = baseName + suffix + extension;
        return inputPath.getParent().resolve(outputFileName);
    }
}