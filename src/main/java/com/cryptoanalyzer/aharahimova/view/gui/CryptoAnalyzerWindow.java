package com.cryptoanalyzer.aharahimova.view.gui;

import javax.swing.*;
import java.awt.*;

public class CryptoAnalyzerWindow extends JFrame {

    private JComboBox<String> modeComboBox;
    private JTextField sourceField;
    private JTextField keyField;
    private JButton consoleButton;
    private JButton runButton;
    private JTextPane logArea;

    private final String basePath = "C:\\Users\\irynaa\\IdeaProjects\\cryptoanalysis-cipher-tools\\";

    public CryptoAnalyzerWindow() {
        super("Crypto Analyzer");
        initUI();
    }

    private void initUI() {
        initFrame();
        createComponents();
        layoutComponents();
        addListeners();
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void createComponents() {
        String[] modes = {"Encode", "Decode", "Brute Force"};
        modeComboBox = new JComboBox<>(modes);
        sourceField = new JTextField(basePath + "input.txt");
        keyField = new JTextField("3");

        consoleButton = new JButton("Switch to Console");
        runButton = new JButton("Run");

        logArea = new JTextPane();
        logArea.setEditable(false);
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel modeLabel = new JLabel("Mode:");
        JLabel sourceLabel = new JLabel("Source File:");
        JLabel keyLabel = new JLabel("Key (for Encode/Decode):");

        modeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        sourceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        keyLabel.setHorizontalAlignment(SwingConstants.LEFT);

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridy = 0;
        panel.add(modeLabel, gbc);
        gbc.gridy = 1;
        panel.add(sourceLabel, gbc);
        gbc.gridy = 2;
        panel.add(keyLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        panel.add(modeComboBox, gbc);
        gbc.gridy = 1;
        panel.add(sourceField, gbc);
        gbc.gridy = 2;
        panel.add(keyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 5, 5, 5);

        panel.add(consoleButton, gbc);

        gbc.gridx = 1;
        panel.add(runButton, gbc);

        JScrollPane scrollPane = new JScrollPane(logArea);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addListeners() {
        modeComboBox.addActionListener(e -> {
            int selectedIndex = modeComboBox.getSelectedIndex();
            if (selectedIndex == 2) {
                keyField.setVisible(false);
                sourceField.setText(basePath + "input_[ENCRYPTED].txt");
            } else {
                keyField.setVisible(true);
                sourceField.setText(selectedIndex == 0
                        ? basePath + "input.txt"
                        : basePath + "input_[ENCRYPTED].txt");
            }
            revalidate();
            repaint();
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

    public JTextPane getLogArea() {
        return logArea;
    }
}