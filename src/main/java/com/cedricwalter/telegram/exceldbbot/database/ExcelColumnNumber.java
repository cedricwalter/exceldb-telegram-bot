package com.cedricwalter.telegram.exceldbbot.database;

public class ExcelColumnNumber {
    public static String toName(int number) {

        number++; // google drive start at 0 not like excel

        StringBuilder sb = new StringBuilder();
        while (number-- > 0) {
            sb.append((char)('A' + (number % 26)));
            number /= 26;
        }
        return sb.reverse().toString();
    }

    public static int toNumber(String name) {
        int number = 0;
        for (int i = 0; i < name.length(); i++) {
            number = number * 26 + (name.charAt(i) - ('A' - 1));
        }
        return number - 1;  // google drive start at 0 not like excel
    }
}
