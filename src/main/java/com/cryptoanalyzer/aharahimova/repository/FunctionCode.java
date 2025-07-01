package com.cryptoanalyzer.aharahimova.repository;

import com.cryptoanalyzer.aharahimova.services.Decode;
import com.cryptoanalyzer.aharahimova.services.Encode;
import com.cryptoanalyzer.aharahimova.services.Function;
import com.cryptoanalyzer.aharahimova.services.UnsupportedFunction;

public enum FunctionCode {
    ENCODE(new Encode()),
    DECODE(new Decode()),
    UNSUPPORTED_FUNCTION(new UnsupportedFunction());

    private Function function;

    FunctionCode(Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }
}
