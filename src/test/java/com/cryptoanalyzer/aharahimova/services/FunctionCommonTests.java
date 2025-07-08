package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.cryptoanalyzer.aharahimova.constants.FunctionCodeConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FunctionCommonTests {

    static Stream<Arguments> functionsProvider() {
        return Stream.of(
                Arguments.of(new Encode(), ENCODE, new String[]{"encode", "nonexistentfile.txt", "3"}),
                Arguments.of(new Decode(), DECODE, new String[]{"decode", "nonexistentfile.txt", "3"}),
                Arguments.of(new BruteForce(), BRUTE_FORCE, new String[]{"brute", "nonexistentfile.txt"})
        );
    }

    @ParameterizedTest(name = "{1} => Testing with non-existent file")
    @MethodSource("functionsProvider")
    void testNonExistentFileReturnsError(Function function, String name, String[] parameters) {
        Result result = function.execute(parameters);

        assertEquals(ResultCode.ERROR, result.getResultCode());
        assertNotNull(result.getApplicationException());
    }
}
