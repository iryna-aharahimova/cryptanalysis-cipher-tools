package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import com.cryptoanalyzer.aharahimova.utils.CryptoUtils;
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
            String original = Files.readString(inputPath);
            String decoded = CryptoUtils.shiftText(original, -key, ALPHABET);

            logger.info(SAVING_RESULT_TO_FILE, "decode", getOutputPath(inputPath, DECRYPTED));
            Path outputPath = getOutputPath(inputPath, DECRYPTED);
            Files.writeString(outputPath, decoded);

            return new Result(OK);
        } catch (Exception e) {
            logger.error(OPERATION_FAILED, e);
            return new Result(ERROR, new ApplicationException(OPERATION_FAILED, e));
        }
    }
}

