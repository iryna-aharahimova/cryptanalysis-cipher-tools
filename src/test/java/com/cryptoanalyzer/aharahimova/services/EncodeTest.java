package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants.ENCRYPTED;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.KEY_IS_ZERO;
import static com.cryptoanalyzer.aharahimova.utils.FileNameUtils.getOutputPath;
import static com.cryptoanalyzer.aharahimova.utils.TestUtils.*;

import static org.junit.jupiter.api.Assertions.*;

class EncodeTest {

    private Encode encode;
    private Path tempFile;
    private int randomKey;
    private Path outputPath;

    @BeforeEach
    void setUp() throws IOException {
        encode = new Encode();
        randomKey = getRandomKey();
        tempFile = createTempFileWithContent("encode-test-", ".txt", "");
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteIfExists(tempFile, outputPath);
    }

    @Test
    void shouldEncodeSuccessfully() throws Exception {
        String original = "abc xyz";
        Files.writeString(tempFile, original);

        Result result = encode.execute(new String[]{"encode", tempFile.toString(), String.valueOf(randomKey)});
        assertEquals(ResultCode.OK, result.getResultCode());

        outputPath = getOutputPath(tempFile, ENCRYPTED);
        assertTrue(Files.exists(outputPath), "Encrypted file should exist");

        String encryptedText = Files.readString(outputPath);
        assertNotEquals(original, encryptedText, "Encrypted text must differ from original");
    }

    @Test
    void shouldReturnError_WhenKeyIsInvalid() throws Exception {
        Files.writeString(tempFile, "abc xyz");

        Result result = encode.execute(new String[]{"encode", tempFile.toString(), "invalid"});
        assertEquals(ResultCode.ERROR, result.getResultCode());
        assertNotNull(result.getApplicationException());
    }

    @Test
    void shouldReturnError_WhenKeyIsZero() throws IOException {
        Files.writeString(tempFile, "some text");

        Result result = encode.execute(new String[]{"encode", tempFile.toString(), "0"});
        assertEquals(ResultCode.ERROR, result.getResultCode());

        assertNotNull(result.getApplicationException());
        assertTrue(result.getApplicationException().getMessage().contains(KEY_IS_ZERO));
    }
}