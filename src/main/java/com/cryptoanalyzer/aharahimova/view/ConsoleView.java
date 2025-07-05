package com.cryptoanalyzer.aharahimova.view;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.view.console.ConsoleInputCollector;

import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.EXCEPTION;
import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.SUCCESS;

public class ConsoleView implements View {
    private final String[] parameters;

    public ConsoleView() {
        this.parameters = new ConsoleInputCollector().collect();
    }

    @Override
    public String[] getParameters() {
        return parameters;
    }

    @Override
    public void printResult(Result result) {
        switch (result.getResultCode()) {
            case OK -> System.out.println(SUCCESS);
            case ERROR -> {
                System.out.println(EXCEPTION);
                Throwable cause = result.getApplicationException().getCause();
                if (cause != null) {
                    System.out.println("Cause: " + cause.getClass().getSimpleName());
                    System.out.println("Message: " + cause.getMessage());
                } else {
                    System.out.println("Message: " + result.getApplicationException().getMessage());
                }
            }
        }
    }
}
