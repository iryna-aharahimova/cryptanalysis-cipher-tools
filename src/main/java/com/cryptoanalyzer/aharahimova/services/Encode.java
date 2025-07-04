package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants.ENCRYPTED;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.*;
import static com.cryptoanalyzer.aharahimova.utils.PathUtils.getOutputPath;

public class Encode implements Function {

    private static final Logger logger = LoggerFactory.getLogger(Encode.class);

    @Override
    public Result execute(String[] parameters) {
        try {
            logger.info(OPERATION_STARTED, "encode", parameters[1]);
            Path inputPath = Path.of(parameters[1]);

            int key = Integer.parseInt(parameters[2]) % ALPHABET.length();
            logger.info(USING_KEY, "encode", key);

            String original = Files.readString(inputPath, StandardCharsets.UTF_8);
            String encrypted = encode(original, key);

            logger.info(SAVING_RESULT_TO_FILE, "encode", getOutputPath(inputPath, ENCRYPTED));
            Path outputPath = getOutputPath(inputPath, ENCRYPTED);
            Files.writeString(outputPath, encrypted, StandardCharsets.UTF_8);

            return new Result(ResultCode.OK);
        } catch (Exception e) {
            logger.error(OPERATION_FAILED, e);
            return new Result(ResultCode.ERROR, new ApplicationException(OPERATION_FAILED, e));
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
}
