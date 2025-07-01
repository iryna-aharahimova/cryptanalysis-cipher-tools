package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;

import static com.cryptoanalyzer.aharahimova.repository.ResultCode.ERROR;
import static com.cryptoanalyzer.aharahimova.repository.ResultCode.OK;

public class Decode implements Function {
    @Override
    public Result execute(String[] parameters) {
        try {
            //TODO decode
        } catch (Exception e) {
            return new Result(ERROR, new ApplicationException("Decode operation finished with exception ", e));
        }
        return new Result(OK);
    }
}
