package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cryptoanalyzer.aharahimova.utils.CryptoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants.BRUTE_FORCE;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.*;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.ERROR;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.OK;
import static com.cryptoanalyzer.aharahimova.utils.PathUtils.getOutputPath;

public class BruteForce implements Function {

    private static final List<String> COMMON_WORDS = Arrays.asList(
            "the", "and", "that", "have", "for", "not", "with", "you",
            "this", "but", "his", "from", "they", "say", "her", "she",
            "will", "would", "there", "their", "about", "which", "when",
            "make", "can", "like", "time", "just", "know", "take", "into",
            "your", "good", "some", "could", "them", "see", "other", "than",
            "too", "do", "at", "same", "otherwise", "so", "any", "why", "get",
            "set", "off", "last", "first", "on", "a", "an", "under", "it"
    );

    private static final Logger logger = LoggerFactory.getLogger(BruteForce.class);

    @Override
    public Result execute(String[] parameters) {
        try {
            logger.info(OPERATION_STARTED, "brute force", parameters[1]);
            String inputFilePath = parameters[1];
            Path inputPath = Path.of(inputFilePath);
            String encryptedContent = Files.readString(inputPath);

            for (int key = 1; key < ALPHABET.length(); key++) {
                String decoded = CryptoUtils.shiftText(encryptedContent, -key, ALPHABET).toLowerCase();
                Set<String> found = findCommonWords(decoded);

                if (!found.isEmpty()) {
                    logger.info(BRUTE_FORCE_FOUND_WORDS, found);
                    logger.info(BRUTE_FORCE_SUCCEEDED_KEY, key);
                    logger.info(SAVING_RESULT_TO_FILE, "brute force", getOutputPath(inputPath, BRUTE_FORCE));
                    Files.writeString(getOutputPath(inputPath, BRUTE_FORCE), decoded);
                    return new Result(OK);
                }
            }
            logger.warn(BRUTE_FORCE_NO_COMMON_WORDS);
            return new Result(OK, new ApplicationException(BRUTE_FORCE_NO_COMMON_WORDS));

        } catch (Exception e) {
            logger.error(OPERATION_FAILED, e);
            return new Result(ERROR, new ApplicationException(OPERATION_FAILED, e));
        }
    }

    private Set<String> findCommonWords(String text) {
        Set<String> found = new HashSet<>();
        for (String w : text.split("[\\s.,!?:\"'«»]+")) {
            if (COMMON_WORDS.contains(w)) found.add(w);
        }
        return found;
    }
}