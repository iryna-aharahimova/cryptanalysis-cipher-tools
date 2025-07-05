package com.cryptoanalyzer.aharahimova.view.console;

import java.util.Scanner;

public class ConsoleInputCollector {
    private final Scanner scanner = new Scanner(System.in);

    public String[] collect() {
        System.out.println("""
                ======================================
                         CRYPTO ANALYZER TOOL
                ======================================
                Please select an operation mode:
                  1 - Encode
                  2 - Decode
                  3 - Brute Force
                --------------------------------------
                """);

        System.out.print("Enter option (1/2/3): ");
        String mode = scanner.nextLine().trim();

        String basePath = "C:\\\\Users\\\\irynaa\\\\IdeaProjects\\\\cryptoanalysis-cipher-tools\\\\";
        String defaultFileName = switch (mode) {
            case "2", "3" -> "input_[ENCRYPTED].txt";
            default -> "input.txt";
        };

        System.out.printf("Enter path to source file [%s%s]: ", basePath, defaultFileName);
        String sourceInput = scanner.nextLine().trim();
        String source = sourceInput.isEmpty() ? basePath + defaultFileName : sourceInput;

        if (mode.equals("1") || mode.equals("2")) {
            System.out.print("Enter key (number): ");
            String key = scanner.nextLine().trim();
            return new String[]{mode, source, key};
        }

        return new String[]{mode, source};
    }
}