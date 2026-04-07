package com.marcello.digitalgateway.iso8583.util;

import java.util.ArrayList;
import java.util.List;

public class BitmapUtils {
    public static String hexToBinary(String hex) {
        StringBuilder binary = new StringBuilder();

        for (char c: hex.toCharArray()) {
            int value = Integer.parseInt(String.valueOf(c), 16);
            String bin = String.format("%4s", Integer.toBinaryString(value)).replace(' ', '0');
            binary.append(bin);
        }

        return binary.toString();
    }

    public static List<Integer> extractFields(String binaryBitmap) {
        List<Integer> fields = new ArrayList<>();

        for(int i = 0; i < binaryBitmap.length(); i++) {
            if (binaryBitmap.charAt(i) == '1') {
                fields.add(i + 1);
            }
        }

        return fields;
    }
}
