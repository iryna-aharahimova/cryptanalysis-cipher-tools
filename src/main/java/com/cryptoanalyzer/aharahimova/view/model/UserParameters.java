package com.cryptoanalyzer.aharahimova.view.model;

public class UserParameters {
    private final String mode;
    private final String sourceFile;
    private final String key;

    public UserParameters(String mode, String sourceFile, String key) {
        this.mode = mode;
        this.sourceFile = sourceFile;
        this.key = key;
    }

    public UserParameters(String mode, String sourceFile) {
        this(mode, sourceFile, null);
    }

    public String getKey() {
        return key;
    }

    public String[] toParametersArray() {
        if (key == null) {
            return new String[]{mode, sourceFile};
        } else {
            return new String[]{mode, sourceFile, key};
        }
    }
}