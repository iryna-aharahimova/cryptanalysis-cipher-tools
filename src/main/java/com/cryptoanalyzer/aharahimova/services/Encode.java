package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import com.cryptoanalyzer.aharahimova.utils.CryptoUtils;
import com.cryptoanalyzer.aharahimova.utils.FileIoUtils;
import com.cryptoanalyzer.aharahimova.utils.FileNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants.ENCRYPTED;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.*;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.ERROR;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.OK;

public class Encode implements Function {

    private static final Logger logger = LoggerFactory.getLogger(Encode.class);

    @Override
    public Result execute(String[] parameters) {
        try {
            Path inputPath = Path.of(parameters[1]);

            int parsedKey = Integer.parseInt(parameters[2]);
            int key = parsedKey % ALPHABET.length();

            if (!CryptoUtils.validateKey(key)) {
                logger.error(KEY_IS_ZERO);
                return new Result(ERROR, new ApplicationException(KEY_IS_ZERO));
            }

            logger.info(OPERATION_STARTED, "encode", inputPath);
            logger.info(USING_KEY, "encode", key);

            String original = FileIoUtils.readFile(inputPath);
            String encoded = CryptoUtils.shiftText(original, key, ALPHABET);

            Path outputPath = FileNameUtils.getOutputPath(inputPath, ENCRYPTED);
            logger.info(SAVING_RESULT_TO_FILE, "encode", outputPath);

            FileIoUtils.writeFile(outputPath, encoded);
            return new Result(OK);

        } catch (Exception e) {
            logger.error(OPERATION_FAILED, e);
            return new Result(ERROR, new ApplicationException(OPERATION_FAILED, e));
        }
    }
}
