package com.cedricwalter.telegram.exceldbbot.scrapper;

import com.cedricwalter.telegram.exceldbbot.database.ExcelColumnNumber;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KeepitalScrapper {


    protected int linkCount = 0;
    protected int errors = 0;

    private static final String sheetId = GoogleSheet.SINGAPORE_SHEET_ID;
    private WebDriver driver = new ChromeDriver();

    public static void main(String[] args) throws Exception {
        new KeepitalScrapper().analyze(GoogleSheet.getSingaporeRows(), sheetId);
    }

    protected void analyze(List<List<Object>> rows, String sheetId) throws Exception {
        int i = 1; // jump headers
        try {
            for (List<Object> row : rows) {
                try {


                    String addresse = row.size() > ExcelIndexes.street ? row.get(ExcelIndexes.street).toString() : "";

                    if ("".equals(addresse)) {
                        String url = row.get(ExcelIndexes.name).toString();
                        analyzeLink(i, url);
                        Thread.sleep(4000);
                        //GoogleSheet.update(data, sheetId);
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
        boolean manualIntervention = false;
        System.out.println("analyze " + name);
        String url = "https://www.keepital.com/search?t=company&q=" + name;

        driver.get(url);
        Thread.sleep(4000);

        String pageSource = driver.getPageSource();


        Document doc = Jsoup.parse(pageSource);

        String socialReason = "N/A";
        String companyNameFull = "";
        Elements companyName = doc.select("span[class=company-name]");
        if (companyName.size() > 0) {
            for (Element element : companyName) {
                if (element.text().toLowerCase().contains(name.toLowerCase()))
                {
                    if (!companyNameFull.isEmpty() && !manualIntervention) {
                        // too much match found human should sort out
                        System.err.println("check " + url);
                        manualIntervention =  true;
                    } else {
                        companyNameFull = element.text();
                    }
                }
            }
        }
        if (!manualIntervention) {

            if (companyNameFull != null) {
                if (companyNameFull.contains("Ltd")) {
                    socialReason = "Ltd";
                } else if (companyNameFull.contains("Pte. Ltd")) {
                    socialReason = "Pte. Ltd";
                } else {
                    socialReason = companyNameFull.replace(name, "");
                }
            }

            String street = getElementValue(doc, "span[itemprop=streetAddress]", "N/A");
            String unitNumber = getElementValue(doc, "span[itemprop=unitNumber]", "");
            String buildingName = getElementValue(doc, "span[itemprop=buildingName]", "");
            String country = getElementValue(doc, "span[itemprop=addressCountry]", "N/A");
            String postalCode = getElementValue(doc, "span[itemprop=postalCode]", "");

            ArrayList<ValueRange> data = new ArrayList<>();
            data.add(new ValueRange()
                    .setRange("active!" + ExcelColumnNumber.toName(ExcelIndexes.street) + i + ":" + ExcelColumnNumber.toName(ExcelIndexes.zipcodeCountry) + i)
                    .setValues(Arrays.asList(
                            Arrays.asList(
                                    street, unitNumber + " " + buildingName, "".equals(country) ? "singapore" : country
                                            + " " + postalCode
                            )

                    )));
            GoogleSheet.update(data, sheetId);

            data = new ArrayList<>();
            data.add(new ValueRange()
                    .setRange("active!" + ExcelColumnNumber.toName(ExcelIndexes.legalEntity) + i)
                    .setValues(Arrays.asList(
                            Arrays.asList(
                                    socialReason
                            )

                    )));
            GoogleSheet.update(data, sheetId);


            if ("N/A".equals(socialReason)) {
                System.err.println("Please check " + url);
            }
        }

    }

    private String getElementValue(Document doc, String selector, String defaultValue) {
        String value = defaultValue;
        Elements select = doc.select(selector);
        if (select.size() > 0) {
            value = select.get(0).text();
        }

        return value;
    }

}
