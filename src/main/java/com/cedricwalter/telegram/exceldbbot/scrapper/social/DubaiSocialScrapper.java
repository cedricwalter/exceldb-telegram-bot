package com.cedricwalter.telegram.exceldbbot.scrapper.social;

import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;

public class DubaiSocialScrapper {

    private static String sheetId = GoogleSheet.DUBAI_SHEET_ID;

    public static void main(String[] args) throws Exception {
        new SocialScrapper().analyze(GoogleSheet.getDubaiRows(), sheetId);
    }
}
