package com.cryptoanalyzer.aharahimova.view;

import com.cryptoanalyzer.aharahimova.entity.Result;

public interface View {
    String[] getParameters();
    void printResult(Result result);
}
