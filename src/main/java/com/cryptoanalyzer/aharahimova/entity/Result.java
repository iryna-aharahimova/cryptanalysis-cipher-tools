package com.cryptoanalyzer.aharahimova.entity;

import com.cryptoanalyzer.aharahimova.exception.ApplicationException;
import com.cryptoanalyzer.aharahimova.repository.ResultCode;

public class Result {
    private ResultCode resultCode;
    private ApplicationException applicationException;

    public Result(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public ApplicationException getApplicationException() {
        return applicationException;
    }

    public Result(ResultCode resultCode, ApplicationException applicationException) {


        this.resultCode = resultCode;
        this.applicationException = applicationException;
    }
}
