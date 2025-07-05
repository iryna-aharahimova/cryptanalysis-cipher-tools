package com.cryptoanalyzer.aharahimova.utils;

public class CryptoUtils {
    public static String shiftText(String text, int key, String alphabet) {
        StringBuilder sb = new StringBuilder();
        int len = alphabet.length();
        for (char ch : text.toCharArray()) {
            int idx = alphabet.indexOf(ch);
            if (idx == -1) {
                sb.append(ch);
            } else {
                int newIndex = (idx + key + len) % len;
                sb.append(alphabet.charAt(newIndex));
            }
        }
        return sb.toString();
    }

    public static boolean validateKey(int key) {
        return key != 0;
    }
}
