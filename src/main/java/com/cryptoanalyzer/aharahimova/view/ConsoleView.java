package com.cryptoanalyzer.aharahimova.view;

import com.cryptoanalyzer.aharahimova.entity.Result;

import java.util.Scanner;
import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.EXCEPTION;
import static com.cryptoanalyzer.aharahimova.constants.ApplicationCompletionConstants.SUCCESS;

public class ConsoleView implements View {
    private String[] parameters;

    public ConsoleView() {
        this.parameters = collectUserInput();
    }

    private String[] collectUserInput() {
        Scanner scanner = new Scanner(System.in);

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
            case "1" -> "input.txt";
            case "2", "3" -> "input[ENCRYPTED].txt";
            default -> {
                System.out.println("Unsupported mode. Falling back to ENCODE.");
                yield "input.txt";
            }
        };

        System.out.printf("Enter path to source file [%s%s]: ", basePath, defaultFileName);
        String sourceInput = scanner.nextLine().trim();
        String source = sourceInput.isEmpty() ? basePath + defaultFileName : sourceInput;

        String key = "";
        if (mode.equals("1") || mode.equals("2")) {
            System.out.print("Enter key (number): ");
            key = scanner.nextLine().trim();
            return new String[]{mode, source, key};
        }

        return new String[]{mode, source};
    }

    @Override
    public String[] getParameters() {
        return parameters;
    }

    @Override
    public void printResult(Result result) {
        switch (result.getResultCode()) {
            case OK -> System.out.println(SUCCESS);
            case ERROR -> System.out.println(EXCEPTION + result.getApplicationException().getMessage());
        }
    }
}
