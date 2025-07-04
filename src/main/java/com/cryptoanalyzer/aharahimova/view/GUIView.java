package com.cryptoanalyzer.aharahimova.view;

import com.cryptoanalyzer.aharahimova.app.Application;
import com.cryptoanalyzer.aharahimova.controller.MainController;
import com.cryptoanalyzer.aharahimova.entity.Result;

import javax.swing.*;

import java.awt.*;

import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.EXCEPTION;
import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.SUCCESS;

public class GUIView implements View {
    private String[] parameters;

    private final String basePath = "C:\\Users\\irynaa\\IdeaProjects\\cryptoanalysis-cipher-tools\\";

    public GUIView() {
        SwingUtilities.invokeLater(this::showGuiWindow);
    }

    private void showGuiWindow() {
        JFrame frame = new JFrame("Crypto Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 250);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] modes = {"Encode", "Decode", "Brute Force"};
        JComboBox<String> modeComboBox = new JComboBox<>(modes);

        JTextField sourceField = new JTextField(basePath + "input.txt");

        JTextField keyField = new JTextField("3");

        modeComboBox.addActionListener(e -> {
            int selectedIndex = modeComboBox.getSelectedIndex();
            if (selectedIndex == 2) { // Brute Force
                keyField.setVisible(false);
                keyField.setVisible(false);
                sourceField.setText(basePath + "input_[ENCRYPTED].txt");
            } else {
                keyField.setVisible(true);
                keyField.setVisible(true);
                sourceField.setText(selectedIndex == 0 ? basePath + "input.txt" : basePath + "input_[ENCRYPTED].txt");
            }
            panel.revalidate();
            panel.repaint();
        });

        JButton consoleButton = new JButton("Switch to Console");
        JButton runButton = new JButton("Run");


        panel.add(new JLabel("Mode:"));
        panel.add(modeComboBox);
        panel.add(new JLabel("Source File:"));
        panel.add(sourceField);
        panel.add(new JLabel("Key (for Encode/Decode):"));
        panel.add(keyField);
        panel.add(consoleButton);
        panel.add(runButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        runButton.addActionListener(e -> {
            int modeIndex = modeComboBox.getSelectedIndex();
            String mode = String.valueOf(modeIndex + 1);
            String source = sourceField.getText();
            String key = keyField.getText();

            if (mode.equals("3")) {
                parameters = new String[]{mode, source};
            } else {
                parameters = new String[]{mode, source, key};
            }

            MainController controller = new MainController(this);
            Application app = new Application(controller);
            Result result = app.run();
            app.printResult(result);
        });

        consoleButton.addActionListener(e -> {
            frame.dispose();
            View consoleView = new ConsoleView();
            MainController controller = new MainController(consoleView);
            Application app = new Application(controller);
            Result result = app.run();
            app.printResult(result);
        });
    }

    @Override
    public String[] getParameters() {
        boolean switchToConsole = false;
        return switchToConsole ? null : parameters;
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
