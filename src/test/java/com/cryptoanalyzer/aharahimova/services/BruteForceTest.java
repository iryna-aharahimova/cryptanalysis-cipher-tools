package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import com.cryptoanalyzer.aharahimova.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static org.junit.jupiter.api.Assertions.*;

class BruteForceTest {

    private BruteForce bruteForce;
    int randomKey = TestUtils.getRandomKey();

    @BeforeEach
    void setUp() {
        bruteForce = new BruteForce();
    }

    @Test
    void testSuccessfulBruteForceDecoding() throws IOException {
        String originalText = "you are good";

        String encrypted = shiftText(originalText, randomKey);

        Path tempFile = Files.createTempFile("brute-encrypted-", ".txt");
        Files.writeString(tempFile, encrypted);

        Result result = bruteForce.execute(new String[]{"brute", tempFile.toString()});

        assertEquals(ResultCode.OK, result.getResultCode(), "Should decode successfully");

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testNoCommonWordsFound() throws IOException {
        String gibberish = "zzzqqqxxxrrr";
        Path tempFile = Files.createTempFile("brute-encrypted-", ".txt");
        Files.writeString(tempFile, gibberish);

        Result result = bruteForce.execute(new String[]{"brute", tempFile.toString()});

        assertEquals(ResultCode.ERROR, result.getResultCode(), "Should return error when no common words found");
        assertTrue(result.getApplicationException().getMessage().contains("No common words"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testMissingFile() {
        String fakePath = "nonexistent-" + UUID.randomUUID() + ".txt";
        Result result = bruteForce.execute(new String[]{"brute", fakePath});

        assertEquals(ResultCode.ERROR, result.getResultCode(), "Should return error for missing file");
        assertNotNull(result.getApplicationException());
    }

    private String shiftText(String text, int key) {
        StringBuilder encrypted = new StringBuilder();
        for (char ch : text.toLowerCase().toCharArray()) {
            int idx = ALPHABET.indexOf(ch);
            encrypted.append(idx == -1 ? ch : ALPHABET.charAt((idx + key) % ALPHABET.length()));
        }
        return encrypted.toString();
    }
}