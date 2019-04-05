package com.cedricwalter.telegram.exceldbbot.scrapper.social;

import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;

public class SingaporeSocialScrapper {

    private static String sheetId = GoogleSheet.SINGAPORE_SHEET_ID;

    public static void main(String[] args) throws Exception {
        new SocialScrapper().analyze(GoogleSheet.getSingaporeRows(), sheetId);
    }
}
