package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
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
            String inputFilePath = parameters[1];

            int parsedKey = Integer.parseInt(parameters[2]);
            int key = parsedKey % ALPHABET.length();

            if (!CryptoUtils.validateKey(key)) {
                logger.error(KEY_IS_ZERO);
                return new Result(ResultCode.ERROR, new ApplicationException(KEY_IS_ZERO));
            }

            logger.info(OPERATION_STARTED, "decode", parameters[1]);
            logger.info(USING_KEY, "decode", key);

            Path inputPath = Path.of(inputFilePath);
            String original = Files.readString(inputPath);
            String decoded = CryptoUtils.shiftText(original, -key, ALPHABET);

            Path outputPath = getOutputPath(inputPath, DECRYPTED);
            logger.info(SAVING_RESULT_TO_FILE, "decode", getOutputPath(inputPath, DECRYPTED));
            Files.writeString(outputPath, decoded);

            return new Result(OK);
        } catch (Exception e) {
            logger.error(OPERATION_FAILED, e);
            return new Result(ERROR, new ApplicationException(OPERATION_FAILED, e));
        }
    }
}

