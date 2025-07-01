package com.cryptoanalyzer.aharahimova.controller;

import com.cryptoanalyzer.aharahimova.view.View;

public class MainController {

    private View view;

    public MainController(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
