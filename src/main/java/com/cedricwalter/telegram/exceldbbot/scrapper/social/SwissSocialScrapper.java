package com.cedricwalter.telegram.exceldbbot.scrapper.social;

import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;

public class SwissSocialScrapper {

    private static String sheetId = GoogleSheet.SWISS_SHEET_ID;

    public static void main(String[] args) throws Exception {
        new SocialScrapper().analyze(GoogleSheet.getSwissRows(), sheetId);
    }
}
