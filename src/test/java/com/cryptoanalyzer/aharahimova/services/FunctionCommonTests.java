package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionCommonTests {

    record FunctionTestCase(Function function, String name, String[] parameters) {}

    static Stream<FunctionTestCase> functionsProvider() {
        return Stream.of(
                new FunctionTestCase(new Encode(), "ENCODE", new String[]{"encode", "nonexistentfile.txt", "3"}),
                new FunctionTestCase(new Decode(), "DECODE", new String[]{"decode", "nonexistentfile.txt", "3"}),
                new FunctionTestCase(new BruteForce(), "BRUTE_FORCE", new String[]{"brute", "nonexistentfile.txt"})
        );
    }

    @ParameterizedTest(name = "{0} should return error for non-existent file")
    @MethodSource("functionsProvider")
    void testNonExistentFileReturnsError(FunctionTestCase testCase) {
        Result result = testCase.function().execute(testCase.parameters());

        assertEquals(ResultCode.ERROR, result.getResultCode(), testCase.name() + " did not return ERROR");
        assertNotNull(result.getApplicationException(), testCase.name() + " should return an exception");
    }
}