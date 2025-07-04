package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import java.nio.file.Files;
import java.nio.file.Path;
import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.ERROR;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.OK;

public class Decode implements Function {
    @Override
    public Result execute(String[] parameters) {
        try {
            String inputFilePath = parameters[1];
            int key = Integer.parseInt(parameters[2]) % ALPHABET.length();
            Path inputPath = Path.of(inputFilePath);
            String originalContent = Files.readString(inputPath);

            StringBuilder decoded = new StringBuilder();
            for (char ch : originalContent.toCharArray()) {
                int index = ALPHABET.indexOf(ch);
                if (index != -1) {
                    int newIndex = (index - key + ALPHABET.length()) % ALPHABET.length();
                    decoded.append(ALPHABET.charAt(newIndex));
                } else {
                    decoded.append(ch);
                }
            }

            Path outputPath = getOutputPath(inputPath, "[DECRYPTED]");
            Files.writeString(outputPath, decoded.toString());

            return new Result(OK);
        } catch (Exception e) {
            return new Result(ERROR, new ApplicationException("Decode operation finished with exception ", e));
        }
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

