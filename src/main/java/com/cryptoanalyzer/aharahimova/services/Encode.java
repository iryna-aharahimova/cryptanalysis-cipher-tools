package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;

public class Encode implements Function {
    @Override
    public Result execute(String[] parameters) {
        try {
            Path inputPath = Path.of(parameters[1]);
            int key = Integer.parseInt(parameters[2]) % ALPHABET.length();

            String original = Files.readString(inputPath, StandardCharsets.UTF_8);
            String encrypted = encode(original, key);

            Path outputPath = generateOutputPath(inputPath, "[ENCRYPTED]");
            Files.writeString(outputPath, encrypted, StandardCharsets.UTF_8);

            return new Result(ResultCode.OK);
        } catch (Exception e) {
            return new Result(ResultCode.ERROR, new ApplicationException("Encryption failed", e));
        }

    }

    private String encode(String text, int key) {
        StringBuilder sb = new StringBuilder();
        for (char ch : text.toCharArray()) {
            int index = ALPHABET.indexOf(ch);
            if (index == -1) {
                sb.append(ch);
            } else {
                int newIndex = (index + key + ALPHABET.length()) % ALPHABET.length();
                sb.append(ALPHABET.charAt(newIndex));
            }
        }
        return sb.toString();
    }

    private Path generateOutputPath(Path inputPath, String suffix) {
        String fileName = inputPath.getFileName().toString();
        String baseName = fileName.replace(".txt", "");
        return inputPath.resolveSibling(baseName + suffix + ".txt");
    }
}
