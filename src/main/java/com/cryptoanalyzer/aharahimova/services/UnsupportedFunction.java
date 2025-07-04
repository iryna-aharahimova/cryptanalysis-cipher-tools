package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;

import static com.cryptoanalyzer.aharahimova.repository.ResultCode.ERROR;

public class UnsupportedFunction implements Function {
    @Override
    public Result execute(String[] parameters) {
        return new Result(ERROR, new ApplicationException("Unsupported command: "
                + String.join(" ", parameters)));
    }
}
