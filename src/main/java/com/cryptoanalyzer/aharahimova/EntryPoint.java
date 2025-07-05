package com.cryptoanalyzer.aharahimova;

import com.cryptoanalyzer.aharahimova.app.Application;
import com.cryptoanalyzer.aharahimova.controller.MainController;
import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.view.ConsoleView;
import com.cryptoanalyzer.aharahimova.view.GUIView;

public class EntryPoint {
    public static void main(String[] args) {
        GUIView guiView = new GUIView();

        guiView.setOnUserInputReady((v) -> {
            if (guiView.getParameters() == null) {
                ConsoleView consoleView = new ConsoleView();
                MainController controller = new MainController(consoleView);
                Application app = new Application(controller);
                Result result = app.run();
                app.printResult(result);
            } else {
                MainController controller = new MainController(guiView);
                Application app = new Application(controller);
                Result result = app.run();
                app.printResult(result);
            }
        });
    }
}