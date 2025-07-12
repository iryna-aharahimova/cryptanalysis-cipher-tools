package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import com.cryptoanalyzer.aharahimova.utils.CryptoUtils;
import com.cryptoanalyzer.aharahimova.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static com.cryptoanalyzer.aharahimova.constants.FileSuffixesConstants.DECRYPTED;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.KEY_IS_ZERO;
import static com.cryptoanalyzer.aharahimova.utils.FileNameUtils.getOutputPath;
import static org.junit.jupiter.api.Assertions.*;

class DecodeTest {

    private Decode decode;
    private Path tempFile;
    private Path outputPath;

    @BeforeEach
    void setUp() {
        decode = new Decode();
    }

    @AfterEach
    void tearDown() throws IOException {
        TestUtils.deleteIfExists(tempFile, outputPath);
    }

    static Stream<Arguments> decodeTestData() {
        return Stream.of(
                Arguments.of("This is a test", 5),
                Arguments.of("This is a test", ALPHABET.length() + 7)
        );
    }

    @ParameterizedTest(name = "Decoding \"{0}\" with key={1}")
    @MethodSource("decodeTestData")
    void shouldDecodeSuccessfully(String original, int key) throws Exception {
        String encoded = CryptoUtils.shiftText(original, key, ALPHABET);
        tempFile = TestUtils.createTempFileWithContent("decode-test-", ".txt", encoded);

        Result result = decode.execute(new String[]{"decode", tempFile.toString(), String.valueOf(key)});
        assertEquals(ResultCode.OK, result.getResultCode());

        outputPath = getOutputPath(tempFile, DECRYPTED);
        assertTrue(Files.exists(outputPath), "Decrypted file should exist");

        String decoded = Files.readString(outputPath);
        assertEquals(original, decoded, "Decoded text should match original");
    }

    @Test
    void shouldReturnError_WhenKeyIsInvalid() throws Exception {
        tempFile = TestUtils.createTempFileWithContent("decode-test-", ".txt", "Hello World");

        Result result = decode.execute(new String[]{"decode", tempFile.toString(), "invalidKey"});

        assertEquals(ResultCode.ERROR, result.getResultCode());
        assertNotNull(result.getApplicationException());
    }

    @Test
    void shouldReturnError_WhenKeyIsZero() throws Exception {
        tempFile = TestUtils.createTempFileWithContent("decode-test-", ".txt", "Hello World");

        Result result = decode.execute(new String[]{"decode", tempFile.toString(), "0"});

        assertEquals(ResultCode.ERROR, result.getResultCode());
        assertNotNull(result.getApplicationException());
        assertTrue(result.getApplicationException().getMessage().contains(KEY_IS_ZERO));
    }
}