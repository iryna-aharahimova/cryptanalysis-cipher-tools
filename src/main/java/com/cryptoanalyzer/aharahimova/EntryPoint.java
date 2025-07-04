package com.cryptoanalyzer.aharahimova;

import com.cryptoanalyzer.aharahimova.app.Application;
import com.cryptoanalyzer.aharahimova.controller.MainController;
import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.view.GUIView;
import com.cryptoanalyzer.aharahimova.view.View;

public class EntryPoint {

    public static void main(String[] args) {

        View view = new GUIView();

        MainController mainController = new MainController(view);
        Application application = new Application(mainController);

        Result result = application.run();
        application.printResult(result);
    }
}