package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import com.cryptoanalyzer.aharahimova.utils.CryptoUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.KEY_IS_ZERO;
import static org.junit.jupiter.api.Assertions.*;

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

    static Stream<Arguments> decodeTestData() {
        return Stream.of(
                Arguments.of("This is a test", 5),
                Arguments.of("This is a test", ALPHABET.length() + 7)
        );
    }

    @ParameterizedTest(name = "Decoding \"{0}\" with key={1}")
    @MethodSource("decodeTestData")
    void testDecodingWithVariousKeys(String original, int key) throws Exception {
        String encoded = CryptoUtils.shiftText(original, key, ALPHABET);
        Files.writeString(tempFile, encoded);

        Result result = decode.execute(new String[]{"decode", tempFile.toString(), String.valueOf(key)});

        assertEquals(ResultCode.OK, result.getResultCode());

        outputPath = com.cryptoanalyzer.aharahimova.utils.PathUtils.getOutputPath(tempFile, "_[DECRYPTED]");
        assertTrue(Files.exists(outputPath), "File was created");

        String decodedText = Files.readString(outputPath);
        assertEquals(original, decodedText, "Decoded text matches the original one");
    }

    @Test
    void testDecodeWithInvalidKeyReturnsError() throws Exception {
        String originalText = "Hello World";
        Files.writeString(tempFile, originalText);

        Result result = decode.execute(new String[]{"decode", tempFile.toString(), "invalidKey"});

        assertEquals(ResultCode.ERROR, result.getResultCode(), "Invalid key results in error");
        assertNotNull(result.getApplicationException());
    }

    @Test
    void testDecodeWithKeyZeroReturnsError() throws IOException {
        String originalText = "Hello World";
        Files.writeString(tempFile, originalText);

        Result result = decode.execute(new String[]{"decode", tempFile.toString(), "0"});

        assertEquals(ResultCode.ERROR, result.getResultCode());
        assertNotNull(result.getApplicationException());
        assertTrue(result.getApplicationException().getMessage().contains(KEY_IS_ZERO));
    }
}