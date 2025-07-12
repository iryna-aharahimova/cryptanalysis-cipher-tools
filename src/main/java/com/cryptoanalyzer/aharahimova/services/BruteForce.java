package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import com.cryptoanalyzer.aharahimova.utils.CryptoUtils;
import com.cryptoanalyzer.aharahimova.utils.FileIoUtils;
import com.cryptoanalyzer.aharahimova.utils.FileNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants.BRUTE_FORCE;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.*;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.ERROR;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.OK;

public class BruteForce implements Function {

    private static final Logger logger = LoggerFactory.getLogger(BruteForce.class);

    private record DecryptionResult(int key, String decoded, Set<String> words, int score) {
    }

    private static final Set<String> COMMON_WORDS = Set.of(
            "the", "and", "that", "have", "for", "not", "with", "you",
            "this", "but", "his", "from", "they", "say", "her", "she",
            "will", "would", "there", "their", "about", "which", "when",
            "make", "can", "like", "time", "just", "know", "take", "into",
            "your", "good", "some", "could", "them", "see", "other", "than",
            "too", "do", "at", "same", "otherwise", "so", "any", "why", "get",
            "set", "off", "last", "first", "on", "a", "an", "under", "it"
    );

    @Override
    public Result execute(String[] parameters) {
        try {
            Path inputPath = Path.of(parameters[1]);
            logger.info(OPERATION_STARTED, "brute force", inputPath);

            String encryptedContent = FileIoUtils.readFile(inputPath);
            DecryptionResult result = findBestDecryption(encryptedContent);

            if (result.score() > 0) {
                logger.info(BRUTE_FORCE_FOUND_WORDS, result.words());
                logger.info(BRUTE_FORCE_SUCCEEDED_KEY, result.key());

                Path outputPath = FileNameUtils.getOutputPath(inputPath, BRUTE_FORCE);
                logger.info(SAVING_RESULT_TO_FILE, "brute force", outputPath);

                FileIoUtils.writeFile(outputPath, result.decoded());
                return new Result(OK);
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
        for (String word : text.split("[\\s.,!?:\"'«»]+")) {
            if (COMMON_WORDS.contains(word.toLowerCase())) {
                found.add(word);
            }
        }
        return found;
    }

    private DecryptionResult findBestDecryption(String encryptedText) {
        int bestKey = -1;
        int maxMatches = 0;
        String bestDecoded = null;
        Set<String> bestWords = Set.of();

        for (int key = 1; key < ALPHABET.length(); key++) {
            String decoded = CryptoUtils.shiftText(encryptedText, -key, ALPHABET);
            Set<String> foundWords = findCommonWords(decoded);

            if (foundWords.size() > maxMatches) {
                bestKey = key;
                maxMatches = foundWords.size();
                bestDecoded = decoded;
                bestWords = foundWords;
            }
        }

        return new DecryptionResult(bestKey, bestDecoded, bestWords, maxMatches);
    }
}