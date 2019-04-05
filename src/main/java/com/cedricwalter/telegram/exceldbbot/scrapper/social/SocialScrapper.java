package com.cedricwalter.telegram.exceldbbot.scrapper.social;

import com.cedricwalter.telegram.exceldbbot.database.ExcelColumnNumber;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SocialScrapper {

    private int linkCount = 0;
    private int errors = 0;

    protected void analyze(List<List<Object>> rows, String sheetId) throws Exception {

        String webpage = "";
        int i = 2; // jump headers
        rows = rows.subList(1, rows.size());
        for (List<Object> row : rows) {
            try {
                ArrayList<ValueRange> data = new ArrayList<>();

                webpage = getSafeValue(row, ExcelIndexes.webpage);
                String twitter = getSafeValue(row, ExcelIndexes.twitter);

                if (webpage.contains("crunchbase") ||
                        webpage.contains("linked") ||
                        webpage.contains("N/A")) {
                    System.err.println("Not enough data for " + getSafeValue(row, ExcelIndexes.name) + " to get social and email links");
                } else {
                    if (twitter.length() == 0) {
                        analyzeLink(data, i, webpage, sheetId);
                        linkCount++;
                    }
                }
                System.out.print(".");
            } catch (Exception e) {
                System.err.println("webpage " + webpage + e);
                errors++;
            }
            i++;
        }

        System.out.println("analyzed  " + i + " entries " + linkCount + " added and found " + errors + " errors");
    }

    private String getSafeValue(List<Object> row, int columnIndex) {
        try {
            return row.size() >= columnIndex ? row.get(columnIndex).toString() : "";
        } catch(Exception e) {
            return "";
        }
    }

    private void analyzeLink(ArrayList<ValueRange> data, int i, String url, String sheetId) throws Exception {
        Document doc = Jsoup.connect(url).get();

        String twitter = "N/A";
        String reddit = "N/A";
        String telegram = "N/A";
        String facebook = "N/A";
        String slack = "N/A";
        String forum = "N/A";
        String github = "N/A";
        String medium = "N/A";
        String youtube = "N/A";
        String linkedin = "N/A";
        String email = "N/A";

        Elements links = doc.select("a[href]");
        for (Element link : links) {

            linkCount++;

            String href = link.attr("abs:href");
            if (href.contains("https://twitter.com")) {
                twitter = href;
            }
            if (href.contains("t.me")) {
                telegram = href;
            }
            if (href.contains("facebook")) {
                facebook = href;
            }
            if (href.contains("slack")) {
                slack = href;
            }
            if (href.contains("reddit")) {
                reddit = href;
            }
            if (href.contains("forum")) {
                forum = href;
            }
            if (href.contains("github.com")) {
                github = href;
            }
            if (href.contains("medium")) {
                medium = href;
            }
            if (href.contains("youtube")) {
                medium = href;
            }
            if (href.contains("linkedin.com")) {
                linkedin = href;
            }
            if (href.contains("mailto:")) {
                email = href.replaceAll("mailto:", "");

            }
        }

        System.out.println(email);

        List<List<Object>> values = Arrays.asList(
                Arrays.asList(
                        twitter, telegram, facebook, slack, reddit, forum, github, medium, youtube, linkedin
                )

        );

        data.add(new ValueRange()
                .setRange("active!" + ExcelColumnNumber.toName(ExcelIndexes.twitter) + i + ":" + ExcelColumnNumber.toName(ExcelIndexes.linkedin) + i)
                .setValues(values));
        GoogleSheet.update(data, sheetId);
    }

}
