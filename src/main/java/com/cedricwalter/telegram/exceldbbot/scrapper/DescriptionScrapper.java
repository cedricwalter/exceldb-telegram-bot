package com.cedricwalter.telegram.exceldbbot.scrapper;

import com.cedricwalter.telegram.exceldbbot.database.ExcelColumnNumber;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DescriptionScrapper {
    private static String sheetId = GoogleSheet.SINGAPORE_SHEET_ID;
    //private static String sheetId = GoogleSheet.SWISS_SHEET_ID;

    protected int linkCount = 0;
    protected int errors = 0;

    public static void main(String[] args) throws Exception {
        new DescriptionScrapper().analyze(GoogleSheet.getSingaporeRows());
        //new DescriptionScrapper().analyze(GoogleSheet.getSwissRows(), GoogleSheet.SWISS_SHEET_ID);
    }

    private void analyze(List<List<Object>> rows) throws Exception {
        int i = 1; // jump headers
        rows = rows.subList(1, rows.size());
        for (List<Object> row : rows) {
            try {
                ArrayList<ValueRange> data = new ArrayList<>();

                String webpage = getSafeValue(row, ExcelIndexes.webpage);

                if (webpage.contains("crunchbase") ||
                        webpage.contains("linked") ||
                        webpage.contains("N/A")) {
                    System.err.println("Not enough data for " + getSafeValue(row, ExcelIndexes.name) + " to get eocial and email links");
                } else {
                    String desc = getSafeValue(row, ExcelIndexes.DESCRIPTION_COLUMN_INDEX);

                    if (desc.isEmpty()) {
                        analyzeLink(data, i, webpage);
                    }

                }


                linkCount++;
            } catch (Exception e) {
                System.err.println(e);
                errors++;
            }
            i++;
        }

        System.out.println("analyzed  " + i + " entries " + linkCount + " added and found " + errors + " errors");
    }

    private String getSafeValue(List<Object> row, int columnIndex) {
        return row.size() >= columnIndex ? row.get(columnIndex).toString() : "";
    }

    protected void analyzeLink(ArrayList<ValueRange> data, int i, String url) throws Exception {
        System.out.println("analyze " + url);

        Document doc = Jsoup.connect(url).get();

        String description = "";

        Elements select = doc.select("meta[name=description]");
        if (select.size() > 0) {
            description = select.get(0)
                    .attr("content");
        }
        if ("".equals(description)) {
            Elements hTags = doc.select("h1");
            Elements h1Tags = hTags.select("h1");
            if (h1Tags.size() > 0) {
                description = h1Tags.get(0).text();
            }
        }

        List<List<Object>> values = Arrays.asList(
                Arrays.asList(
                        doc.title(), description
                )

        );

        data.add(new ValueRange()
                .setRange("active!" + ExcelColumnNumber.toName(ExcelIndexes.MOTTO_COLUMN_INDEX) + i + ":" + ExcelColumnNumber.toName(ExcelIndexes.DESCRIPTION_COLUMN_INDEX) + i)
                .setValues(values));
        GoogleSheet.update(data, sheetId);
    }

}
