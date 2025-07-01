package com.cryptoanalyzer.aharahimova.services;

import com.cryptoanalyzer.aharahimova.entity.Result;

public class UnsupportedFunction implements Function{
    @Override
    public Result execute(String[] parameters) {
        //todo unsupported
        return null;
    }
}
