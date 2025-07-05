package com.cryptoanalyzer.aharahimova;

import com.cryptoanalyzer.aharahimova.app.Application;
import com.cryptoanalyzer.aharahimova.controller.MainController;
import com.cryptoanalyzer.aharahimova.entity.Result;
import com.cryptoanalyzer.aharahimova.view.ConsoleView;
import com.cryptoanalyzer.aharahimova.view.GUIView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.CONSOLE_MODE_STARTED;
import static com.cryptoanalyzer.aharahimova.constants.LogMessagesConstants.GUI_MODE_STARTED;

public class EntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) {
        logger.info(GUI_MODE_STARTED);
        GUIView guiView = new GUIView();

        guiView.setOnUserInputReady((v) -> {
            if (guiView.getParameters() == null) {

                logger.info(CONSOLE_MODE_STARTED);
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