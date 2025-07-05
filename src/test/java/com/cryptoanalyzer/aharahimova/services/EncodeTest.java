package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import com.cryptoanalyzer.aharahimova.utils.TestUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.utils.PathUtils.getOutputPath;
import static org.junit.jupiter.api.Assertions.*;

public class EncodeTest {

    private final Encode encode = new Encode();
    int randomKey = TestUtils.getRandomKey();

    @Test
    void testSuccessfulEncoding() throws Exception {
        String originalText = "abc xyz";

        Path tempInput = Files.createTempFile("encode-test-", ".txt");
        Files.writeString(tempInput, originalText);

        Result result = encode.execute(new String[]{"encode", tempInput.toString(), String.valueOf(randomKey)});

        assertEquals(ResultCode.OK, result.getResultCode());

        Path outputPath = getOutputPath(tempInput, "_[ENCRYPTED]");
        assertTrue(Files.exists(outputPath));

        String encryptedText = Files.readString(outputPath);
        assertNotEquals(originalText, encryptedText);

        Files.deleteIfExists(tempInput);
        Files.deleteIfExists(outputPath);
    }

    @Test
    void testEncodingWithInvalidKey() throws Exception {
        String originalText = "abc xyz";
        String invalidKey = "invalid";

        Path tempInput = Files.createTempFile("encode-test-", ".txt");
        Files.writeString(tempInput, originalText);

        Result result = encode.execute(new String[]{"encode", tempInput.toString(), invalidKey});

        assertEquals(ResultCode.ERROR, result.getResultCode());
        assertNotNull(result.getApplicationException());

        Files.deleteIfExists(tempInput);
    }
}
