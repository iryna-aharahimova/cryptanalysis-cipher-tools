package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import com.cryptoanalyzer.aharahimova.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.KEY_IS_ZERO;
import static com.cryptoanalyzer.aharahimova.utils.PathUtils.getOutputPath;
import static org.junit.jupiter.api.Assertions.*;

public class EncodeTest {

    private Encode encode;
    private Path tempFile;
    private int randomKey;
    private Path outputPath;

    @BeforeEach
    void setUp() throws IOException {
        encode = new Encode();
        tempFile = Files.createTempFile("encode-test-", ".txt");
        randomKey = TestUtils.getRandomKey();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
        if (outputPath != null) {
            Files.deleteIfExists(outputPath);
        }
    }

    @Test
    void testSuccessfulEncoding() throws Exception {
        String original = "abc xyz";

        Files.writeString(tempFile, original);

        Result result = encode.execute(new String[]{"encode", tempFile.toString(), String.valueOf(randomKey)});

        assertEquals(ResultCode.OK, result.getResultCode());

        outputPath = getOutputPath(tempFile, "_[ENCRYPTED]");
        assertTrue(Files.exists(outputPath), "Encrypted output file should exist");

        String encryptedText = Files.readString(outputPath);
        assertNotEquals(original, encryptedText, "Encrypted text should differ from original");
    }

    @Test
    void testEncodingWithInvalidKey() throws Exception {
        String original = "abc xyz";
        String invalidKey = "invalid";

        Files.writeString(tempFile, original);

        Result result = encode.execute(new String[]{"encode", tempFile.toString(), invalidKey});

        assertEquals(ResultCode.ERROR, result.getResultCode(), "Invalid key should cause error");
        assertNotNull(result.getApplicationException(), "Exception info should be present");
    }

    @Test
    void testEncodeWithKeyZeroReturnsError() throws IOException {
        String original = "test text";
        Files.writeString(tempFile, original);

        Result result = encode.execute(new String[]{"encode", tempFile.toString(), "0"});

        assertEquals(ResultCode.ERROR, result.getResultCode());
        assertNotNull(result.getApplicationException());
        assertTrue(result.getApplicationException().getMessage().contains(KEY_IS_ZERO));
    }
}