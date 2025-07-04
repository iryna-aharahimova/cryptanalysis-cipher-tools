package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants.DECRYPTED;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.*;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.ERROR;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.OK;
import static com.cryptoanalyzer.aharahimova.utils.PathUtils.getOutputPath;

public class Decode implements Function {

    private static final Logger logger = LoggerFactory.getLogger(Decode.class);

    @Override
    public Result execute(String[] parameters) {
        try {
            logger.info(OPERATION_STARTED, "decode", parameters[1]);
            String inputFilePath = parameters[1];

            int key = Integer.parseInt(parameters[2]) % ALPHABET.length();
            logger.info(USING_KEY, "decode", key);

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

            logger.info(SAVING_RESULT_TO_FILE, "decode", getOutputPath(inputPath, DECRYPTED));
            Path outputPath = getOutputPath(inputPath, DECRYPTED);
            Files.writeString(outputPath, decoded.toString());

            return new Result(OK);
        } catch (Exception e) {
            logger.error(OPERATION_FAILED, e);
            return new Result(ERROR, new ApplicationException(OPERATION_FAILED, e));
        }
    }
}

