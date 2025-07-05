package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import com.cryptoanalyzer.aharahimova.utils.CryptoUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecodeTest {
    private Decode decode;
    private Path tempFile;
    private Path outputPath;

    @BeforeEach
    void setUp() throws IOException {
        decode = new Decode();
        tempFile = Files.createTempFile("decode-test-", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);

        if (outputPath != null) {
            Files.deleteIfExists(outputPath);
        }
    }

    @Test
    void testSuccessfulDecoding() throws Exception {
        String original = "Hello World";
        int key = 5;

        String encoded = CryptoUtils.shiftText(original, key, ALPHABET);

        Files.writeString(tempFile, encoded);

        Result result = decode.execute(new String[]{"decode", tempFile.toString(), String.valueOf(key)});

        assertEquals(ResultCode.OK, result.getResultCode());

        outputPath = com.cryptoanalyzer.aharahimova.utils.PathUtils.getOutputPath(tempFile, "_[DECRYPTED]");
        assertTrue(Files.exists(outputPath));

        String decodedText = Files.readString(outputPath);
        assertEquals(original, decodedText);
    }

    @Test
    void testDecodeWithInvalidKeyReturnsError() throws Exception {
        String originalText = "Hello World";

        Files.writeString(tempFile, originalText);

        Result result = decode.execute(new String[]{"decode", tempFile.toString(), "invalidKey"});

        assertEquals(ResultCode.ERROR, result.getResultCode());
    }

    @Test
    void testDecodeWithNonExistentFileReturnsError() {
        String fakePath = "nonexistentfile.txt";
        Result result = decode.execute(new String[]{"decode", fakePath, "3"});
        assertEquals(ResultCode.ERROR, result.getResultCode());
    }
}