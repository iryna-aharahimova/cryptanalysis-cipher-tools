package com.cryptoanalyzer.aharahimova.view.gui;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class GUILogHandler extends AppenderBase<ILoggingEvent> {

    private static JTextPane logArea;

    public static void setLogArea(JTextPane area) {
        logArea = area;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (logArea != null) {
            SwingUtilities.invokeLater(() -> {
                StyledDocument doc = logArea.getStyledDocument();
                Style style = getStyleForLevel(event);
                String logEntry = formatLogEntry(event);

                try {
                    doc.insertString(doc.getLength(), logEntry, style);
                    logArea.setCaretPosition(doc.getLength());
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Style getStyleForLevel(ILoggingEvent event) {
        Style style = logArea.addStyle("logStyle", null);

        switch (event.getLevel().levelStr) {
            case "ERROR" -> StyleConstants.setForeground(style, Color.RED);
            case "WARN"  -> StyleConstants.setForeground(style, Color.ORANGE);
            case "INFO"  -> StyleConstants.setForeground(style, Color.GRAY);
            default -> StyleConstants.setForeground(style, Color.BLACK);
        }

        return style;
    }

    private String formatLogEntry(ILoggingEvent event) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append(event.getFormattedMessage()).append("\n");

        if (event.getThrowableProxy() != null) {
            logEntry.append(event.getThrowableProxy().getClassName())
                    .append(": ")
                    .append(event.getThrowableProxy().getMessage())
                    .append("\n");

            for (var element : event.getThrowableProxy().getStackTraceElementProxyArray()) {
                logEntry.append("\tat ").append(element.getSTEAsString()).append("\n");
            }
        }

        return logEntry.toString();
    }
}