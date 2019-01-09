package com.cedricwalter.telegram.exceldbbot.scrapper;

import com.cedricwalter.telegram.exceldbbot.database.ExcelColumnNumber;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WebpageScrapper {

    protected int linkCount = 0;
    protected int errors = 0;

    private static final String sheetId = GoogleSheet.SINGAPORE_SHEET_ID;
    private WebDriver driver = new ChromeDriver();

    public static void main(String[] args) throws Exception {
        new WebpageScrapper().analyze(GoogleSheet.getSingaporeRows(), sheetId);
    }

    protected void analyze(List<List<Object>> rows, String sheetId) throws Exception {
        int i = 1; // jump headers
        try {
            for (List<Object> row : rows) {
                try {


                    String webpage = row.size() > ExcelIndexes.webpage ? row.get(ExcelIndexes.webpage).toString() : "";

                    if ("".equals(webpage)) {
                        String url = row.get(ExcelIndexes.name).toString();
                        analyzeLink(i, url);
                        Thread.sleep(2000);
                        linkCount++;
                    }

                } catch (Exception e) {
                    System.err.println(e);
                    errors++;
                }
                i++;
            }
        } finally {
            driver.quit();
        }

        System.out.println("analyzed  " + i + " entries " + linkCount + " added and found " + errors + " errors");
    }

    protected void analyzeLink(int i, String name) throws Exception {
        System.out.println("analyze " + name);
        String url = "https://www.google.com/search?q="+name+"+singapore";

        driver.get(url);
        Thread.sleep(2000);

        driver.findElement( By.cssSelector("#rso > div > div > div:nth-child(1) > div > div > div.r > a > h3")).click();
        Thread.sleep(2000);

        String webpage = driver.getCurrentUrl();

        ArrayList<ValueRange> data = new ArrayList<>();
        data.add(new ValueRange()
                .setRange("active!" + ExcelColumnNumber.toName(ExcelIndexes.webpage) + i)
                .setValues(Arrays.asList(
                        Arrays.asList(
                                webpage
                        )

                )));
        GoogleSheet.update(data, sheetId);



    }

}
