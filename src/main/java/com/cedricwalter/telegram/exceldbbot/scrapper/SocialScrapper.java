package com.cedricwalter.telegram.exceldbbot.scrapper;

import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SocialScrapper {
    static int linkCount = 0;
    static int errors = 0;

    public static void main(String[] args) throws Exception {
        ArrayList<ValueRange> data = new ArrayList<>();
        ExcelHelper excelHelper = new ExcelHelper();

        int i = 2; // jump header
        Set<String> urls = excelHelper.getUniqueColumnValues(ExcelIndexes.URL_COLUMN_INDEX);
        for (String url : urls) {

            if (url.trim().isEmpty() || url.equals("Webpage")) {
                continue;
            }

            try {
                analyzeLink(data, i, url);
            } catch (Exception e) {
                System.err.println(e);
                errors++;
            }
            i++;
        }

        GoogleSheet.update(data);

        System.out.println("analyzed  " + linkCount + " links and found " + errors + " errors");

    }

    private static void analyzeLink(ArrayList<ValueRange> data, int i, String url) throws IOException {
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

        Elements links = doc.select("a[href]");
        for (Element link : links) {

            linkCount++;

            String href = link.attr("abs:href").toString();
            if (href.contains("twitter")) {
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
            if (href.contains("github")) {
                github = href;
            }
            if (href.contains("medium")) {
                medium = href;
            }
            if (href.contains("youtube")) {
                medium = href;
            }
            if (href.contains("linkedin")) {
                linkedin = href;
            }
        }

        GoogleSheet.addUpdateSocial
                (i, twitter, telegram, facebook, slack, reddit, forum, github, medium, youtube, linkedin, data);

    }

}
