package com.cedricwalter.telegram.exceldbbot.scrapper;

import com.cedricwalter.telegram.exceldbbot.database.ExcelColumnNumber;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MarketCapScrapper {

    protected int linkCount = 0;
    protected int errors = 0;

    private WebDriver driver = new ChromeDriver();

    public static void main(String[] args) throws Exception {

        boolean fillUpOnlyEmptyCell =  true;
        String fillUpOnlyThisCompany =  null; // "Melonport";
        String date = "20181231";

        // new MarketCapScrapper().analyze(GoogleSheet.getSingaporeRows(), GoogleSheet.SINGAPORE_SHEET_ID);
        new MarketCapScrapper().analyze(GoogleSheet.getSwissRows(), GoogleSheet.SWISS_SHEET_ID, fillUpOnlyEmptyCell, fillUpOnlyThisCompany, date);
    }

    protected void analyze(List<List<Object>> rows, String sheetId, boolean fillUpOnlyEmptyCell, String fillUpOnlyThisCompany, String date) throws Exception {
        int i = 1; // jump headers

//        String url = "https://coinmarketcap.com/all/views/all/";
//        driver.get(url);
//        String pageSource = driver.getPageSource();

        // save file to All Cryptocurrencies _ CoinMarketCap.html

        File input = new File("/Users/cedric/IdeaProjects/exceldb-telegram-bot/src/main/resources/All Cryptocurrencies _ CoinMarketCap.html");
        Document doc = Jsoup.parse(input, "UTF-8", "https://coinmarketcap.com/");

        try {
            for (List<Object> row : rows) {
                try {
                    String name = getSafeValue(row, ExcelIndexes.name);
                    String token = getSafeValue(row, ExcelIndexes.abbreviationToken);
                    String coinmarketCapUrl = getSafeValue(row, ExcelIndexes.coinmarketcapUrl);
                    String valuationUsd = getSafeValue(row, ExcelIndexes.valuationUsd);

                    // jump header
                    if ("Company Name".equals(name)) {
                        i++;
                        continue;
                    }

                    // jump all if they dont match fillUpOnlyThisCompany
                    if (fillUpOnlyThisCompany != null && !fillUpOnlyThisCompany.equals(name)) {
                        i++;
                        continue;
                    }

                    // jump over empty valuationUsd cell
                    if (!valuationUsd.isEmpty() && fillUpOnlyEmptyCell) {
                        i++;
                        continue;
                    }

                    analyzeLink(i, name, date, sheetId, doc, token, coinmarketCapUrl);

                    linkCount++;
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

    protected void analyzeLink(int i, String name, String date, String sheetId, Document doc, String token, String coinmarketCapUrl) throws Exception {
        System.out.println("analyze " + name + " with token " + token);

        String coinUrl = "";
        if (!"".equals(coinmarketCapUrl)) {
            coinUrl = coinmarketCapUrl;
        } else {
            Element found = getElementWithTextLink(name, doc);

            // try with tokenname
            if (found == null) {
                Elements rows = doc.getElementsByAttributeValue("role", "row");
                for (Element row : rows) {
                    Elements elementsByClass = row.getElementsByClass("text-left col-symbol");
                    if (elementsByClass.size() == 1) {
                        if (token.equals(elementsByClass.get(0).text())) {
                            Elements links = row.getElementsByClass("currency-name-container link-secondary");
                            found = links.get(0);
                        }
                    }
                }
            }


            if (found != null) {
                coinUrl = found.attr("href");
            }
        }
        ArrayList<ValueRange> data = new ArrayList<>();
        String valuation = "";

        if (!coinUrl.isEmpty()) {
            System.err.println("Found page for " + name + " or " + token + " in coinmarketcap");

            Random rand = new Random();
            int n = rand.nextInt(5) + 1;
            Thread.sleep(n * 1000);

            driver.get(coinUrl + "/historical-data/?start=" + date + "&end=" + date);

            WebElement element = driver.findElement(By.cssSelector("#historical-data > div > div.table-responsive > table > tbody > tr > td:nth-child(7)"));
            valuation = element.getText();

            data.add(new ValueRange()
                    .setRange("active!" + ExcelColumnNumber.toName(ExcelIndexes.valuationUsd) + i)
                    .setValues(Arrays.asList(
                            Arrays.asList(
                                    valuation
                            )

                    )));

            data.add(new ValueRange()
                    .setRange("active!" + ExcelColumnNumber.toName(ExcelIndexes.coinmarketcapUrl) + i)
                    .setValues(Arrays.asList(
                            Arrays.asList(
                                    coinUrl
                            )

                    )));
            GoogleSheet.update(data, sheetId);

        } else {
            System.err.println("Did not found name " + name + " or " + token + " on page");
            errors++;
        }


    }

    private String getSafeValue(List<Object> row, int columnIndex) {
        return row.size() > columnIndex ? row.get(columnIndex).toString() : "";
    }

    private Element getElementWithTextLink(String name, Document doc) {
        Elements links = doc.getElementsByClass("currency-name-container link-secondary");

        Element found = null;

        // search a link with company name
        for (Element link : links) {
            if (link.text().equalsIgnoreCase(name)) {
                found = link;
            }
        }
        return found;
    }

}
