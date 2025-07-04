package com.cryptoanalyzer.aharahimova.repository;

import com.cryptoanalyzer.aharahimova.services.*;

public enum FunctionCode {
    ENCODE(new Encode()),
    DECODE(new Decode()),
    BRUTE_FORCE(new BruteForce()),
    UNSUPPORTED_FUNCTION(new UnsupportedFunction());

    private Function function;

    FunctionCode(Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }
}
