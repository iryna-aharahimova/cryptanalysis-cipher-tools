package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.cryptoanalyzer.aharahimova.constants.CryptoAlphabet.ALPHABET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecodeTest {

    private final Decode decode = new Decode();

    private String encodeText(String text, int key) {
        StringBuilder sb = new StringBuilder();
        for (char ch : text.toCharArray()) {
            int index = ALPHABET.indexOf(ch);
            if (index == -1) {
                sb.append(ch);
            } else {
                int newIndex = (index + key) % ALPHABET.length();
                sb.append(ALPHABET.charAt(newIndex));
            }
        }
        return sb.toString();
    }

    @Test
    void testSuccessfulDecoding() throws Exception {
        String originalText = "Hello World";
        int key = 5;
        String encrypted = encodeText(originalText, key);

        Path tempInput = Files.createTempFile("decode-test-", ".txt");
        Files.writeString(tempInput, encrypted);

        Result result = decode.execute(new String[]{"decode", tempInput.toString(), String.valueOf(key)});

        assertEquals(ResultCode.OK, result.getResultCode());

        Path outputPath = com.cryptoanalyzer.aharahimova.utils.PathUtils.getOutputPath(tempInput, "_[DECRYPTED]");
        assertTrue(Files.exists(outputPath));

        String decodedText = Files.readString(outputPath);
        assertEquals(originalText, decodedText);

        Files.deleteIfExists(tempInput);
        Files.deleteIfExists(outputPath);
    }

    @Test
    void testDecodeWithInvalidKeyReturnsError() throws Exception {
        String originalText = "Hello World";

        Path tempInput = Files.createTempFile("decode-test-", ".txt");
        Files.writeString(tempInput, originalText);

        Result result = decode.execute(new String[]{"decode", tempInput.toString(), "invalidKey"});

        assertEquals(ResultCode.ERROR, result.getResultCode());

        Files.deleteIfExists(tempInput);
    }

    @Test
    void testDecodeWithNonExistentFileReturnsError() {
        String fakePath = "nonexistentfile.txt";
        Result result = decode.execute(new String[]{"decode", fakePath, "3"});
        assertEquals(ResultCode.ERROR, result.getResultCode());
    }
}