package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import com.cryptoanalyzer.aharahimova.utils.CryptoUtils;
import com.cryptoanalyzer.aharahimova.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
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
    private Path tempFile;
    private int randomKey;

    @BeforeEach
    void setUp() throws IOException {
        bruteForce = new BruteForce();
        tempFile = Files.createTempFile("brute-encrypted-", ".txt");
        randomKey = TestUtils.getRandomKey();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testSuccessfulBruteForceDecoding() throws IOException {
        String original = "you are good";

        String encoded = CryptoUtils.shiftText(original, randomKey, ALPHABET);

        Files.writeString(tempFile, encoded);

        Result decoded = bruteForce.execute(new String[]{"brute", tempFile.toString()});

        assertEquals(ResultCode.OK, decoded.getResultCode(), "Should decode successfully");
    }

    @Test
    void testNoCommonWordsFound() throws IOException {
        String gibberish = "zzzqqqxxxrrr";

        Files.writeString(tempFile, gibberish);

        Result result = bruteForce.execute(new String[]{"brute", tempFile.toString()});

        assertEquals(ResultCode.ERROR, result.getResultCode(), "Should return error when no common words found");
        assertTrue(result.getApplicationException().getMessage().contains("No common words"));
    }

    @Test
    void testMissingFile() {
        String fakePath = "nonexistent-" + UUID.randomUUID() + ".txt";
        Result result = bruteForce.execute(new String[]{"brute", fakePath});

        assertEquals(ResultCode.ERROR, result.getResultCode(), "Should return error for missing file");
        assertNotNull(result.getApplicationException());
    }
}