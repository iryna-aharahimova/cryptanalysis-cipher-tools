package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import com.cryptoanalyzer.aharahimova.utils.CryptoUtils;
import com.cryptoanalyzer.aharahimova.utils.TestUtils;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants.BRUTE_FORCE;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.BRUTE_FORCE_NO_COMMON_WORDS;
import static com.cryptoanalyzer.aharahimova.utils.FileNameUtils.getOutputPath;
import static org.junit.jupiter.api.Assertions.*;

class BruteForceTest {

    private BruteForce bruteForce;
    private Path tempFile;
    private Path outputPath;
    private int randomKey;

    @BeforeEach
    void setUp() {
        bruteForce = new BruteForce();
        randomKey = TestUtils.getRandomKey();
    }

    @AfterEach
    void tearDown() throws IOException {
        TestUtils.deleteIfExists(tempFile, outputPath);
    }

    @Test
    void shouldDecryptSuccessfully_WithBruteForce() throws IOException {
        String original = "you are good";
        String encoded = CryptoUtils.shiftText(original, randomKey, ALPHABET);

        tempFile = TestUtils.createTempFileWithContent("brute-encrypted-", ".txt", encoded);

        Result result = bruteForce.execute(new String[]{"brute", tempFile.toString()});
        assertEquals(ResultCode.OK, result.getResultCode());

        outputPath = getOutputPath(tempFile, BRUTE_FORCE);
        assertTrue(Files.exists(outputPath), "Decrypted output file should exist");

        String decoded = Files.readString(outputPath);
        assertEquals(original, decoded, "Decoded text should match original");
    }

    @Test
    void shouldReturnError_WhenNoCommonWordsFound() throws IOException {
        String gibberish = "zzzqqqxxxrrr";

        tempFile = TestUtils.createTempFileWithContent("brute-encrypted-", ".txt", gibberish);

        Result result = bruteForce.execute(new String[]{"brute", tempFile.toString()});
        assertEquals(ResultCode.OK, result.getResultCode());

        assertNotNull(result.getApplicationException());
        assertTrue(result.getApplicationException().getMessage().contains(BRUTE_FORCE_NO_COMMON_WORDS));
    }
}