package com.cedricwalter.telegram.exceldbbot.database;

public class SheetUtils {

    public static String columnToLetter(int column) {
        int temp;
        String letter = "";
        while (column > 0) {
            temp = (column - 1) % 26;
            letter = ((char) temp + 65) + letter;
            column = (column - temp - 1) / 26;
        }
        return letter;
    }


}
