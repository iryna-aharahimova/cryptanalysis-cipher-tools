package com.cryptoanalyzer.aharahimova.view.gui;

import javax.swing.*;
import java.awt.*;

public class CryptoAnalyzerWindow extends JFrame {

    private JComboBox<String> modeComboBox;
    private JTextField sourceField;
    private JTextField keyField;
    private JButton consoleButton;
    private JButton runButton;

    private final String basePath = "C:\\Users\\irynaa\\IdeaProjects\\cryptoanalysis-cipher-tools\\";

    public CryptoAnalyzerWindow() {
        super("Crypto Analyzer");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] modes = {"Encode", "Decode", "Brute Force"};
        modeComboBox = new JComboBox<>(modes);

        sourceField = new JTextField(basePath + "input.txt");
        keyField = new JTextField("3");

        panel.add(new JLabel("Mode:"));
        panel.add(modeComboBox);
        panel.add(new JLabel("Source File:"));
        panel.add(sourceField);
        panel.add(new JLabel("Key (for Encode/Decode):"));
        panel.add(keyField);

        consoleButton = new JButton("Switch to Console");
        runButton = new JButton("Run");

        panel.add(consoleButton);
        panel.add(runButton);

        add(panel, BorderLayout.CENTER);

        modeComboBox.addActionListener(e -> {
            int selectedIndex = modeComboBox.getSelectedIndex();
            if (selectedIndex == 2) {
                keyField.setVisible(false);
                sourceField.setText(basePath + "input_[ENCRYPTED].txt");
            } else {
                keyField.setVisible(true);
                sourceField.setText(selectedIndex == 0 ? basePath + "input.txt" : basePath + "input_[ENCRYPTED].txt");
            }
            panel.revalidate();
            panel.repaint();
        });
    }

    public JComboBox<String> getModeComboBox() {
        return modeComboBox;
    }

    public JTextField getSourceField() {
        return sourceField;
    }

    public JTextField getKeyField() {
        return keyField;
    }

    public JButton getConsoleButton() {
        return consoleButton;
    }

    public JButton getRunButton() {
        return runButton;
    }
}