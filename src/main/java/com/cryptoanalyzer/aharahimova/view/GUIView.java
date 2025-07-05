package com.cryptoanalyzer.aharahimova.view;

import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.view.model.UserParameters;
import com.cryptoanalyzer.aharahimova.view.gui.CryptoAnalyzerWindow;
import javax.swing.*;
import java.util.function.Consumer;

import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.EXCEPTION;
import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.SUCCESS;

public class GUIView implements View {

    private CryptoAnalyzerWindow window;
    private UserParameters parameters;
    private boolean switchToConsole = false;
    private Consumer<Void> onUserInputReady;

    public GUIView() {
        SwingUtilities.invokeLater(() -> {
            window = new CryptoAnalyzerWindow();
            setupListeners();
            window.setVisible(true);
        });
    }

    private void setupListeners() {
        window.getRunButton().addActionListener(e -> {
            int modeIndex = window.getModeComboBox().getSelectedIndex();
            String mode = String.valueOf(modeIndex + 1);
            String source = window.getSourceField().getText();
            String key = window.getKeyField().getText();

            if (mode.equals("3")) {
                parameters = new UserParameters(mode, source);
            } else {
                parameters = new UserParameters(mode, source, key);
            }
            switchToConsole = false;

            if (onUserInputReady != null) {
                onUserInputReady.accept(null);
            }
        });

        window.getConsoleButton().addActionListener(e -> {
            window.dispose();
            switchToConsole = true;
            parameters = null;

            if (onUserInputReady != null) {
                onUserInputReady.accept(null);
            }
        });
    }

    public void setOnUserInputReady(Consumer<Void> callback) {
        this.onUserInputReady = callback;
    }

    @Override
    public String[] getParameters() {
        return switchToConsole ? null : (parameters != null ? parameters.toParametersArray() : null);
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