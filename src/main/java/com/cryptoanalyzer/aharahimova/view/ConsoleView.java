package com.cryptoanalyzer.aharahimova.view;

import com.cryptoanalyzer.aharahimova.entity.Result;

import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.EXCEPTION;
import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.SUCCESS;

public class ConsoleView implements View {
    @Override
    public String[] getParameters() {
        //todo finish consoleView getParameters method
        return new String[0];
    }

    @Override
    public void printResult(Result result) {
        switch (result.getResultCode()) {
            case OK -> System.out.println(SUCCESS);
            case ERROR -> System.out.println(EXCEPTION + result.getApplicationException().getMessage());
        }
    }
}
