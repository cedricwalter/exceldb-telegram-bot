package com.cedricwalter.telegram.exceldbbot.database;

import java.util.List;

public class ExcelIndexes {
    public static int name = 0;
    public static int CATEGORY_COLUMN_INDEX = 1;
    public static int SUB_CATEGORY_COLUMN_INDEX = 2;
    public static int webpage = 3;
    public static int MOTTO_COLUMN_INDEX = 4;
    public static int DESCRIPTION_COLUMN_INDEX = 5;
    public static int address = 7;
    public static int street = 8;
    public static int zipcodeCountry = 9;
    public static int TOP30_COLUMN_INDEX = 23;

    public static int CANTON_COLUMN_INDEX = 10;

    public static int IN_CVL__COLUMN_INDEX = 30;
    public static int IN_LP_COLUMN_INDEX = 31;


    public static int LAT_COLUMN_INDEX = 14;
    public static int LONG_COLUMN_INDEX = 15;
    public static int twitter;
    public static int facebook;
    public static int reddit;
    public static int slack;
    public static int forum;
    public static int telegram;
    public static int youtube;
    public static int medium;
    public static int github;
    public static int linkedin;
    public static int legalEntity;
    public static int valuationUsd;
    public static int abbreviationToken;
    public static int coinmarketcapUrl;

    public static void buildIndex(List<Object> row) {
        int columnIndex = 0;
        for (Object column : row) {

            if (String.valueOf(column).equals("Company Name")) {
                name = columnIndex;
            }
            if (String.valueOf(column).equals("Category")) {
                CATEGORY_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("Sub-Category")) {
                SUB_CATEGORY_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("Webpage")) {
                webpage = columnIndex;
            }
            if (String.valueOf(column).equals("Motto")) {
                MOTTO_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("Webpage")) {
                webpage = columnIndex;
            }
            if (String.valueOf(column).equals("Motto")) {
                MOTTO_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("Description")) {
                DESCRIPTION_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("Lat")) {
                LAT_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("Long")) {
                LONG_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("Motto")) {
                MOTTO_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("Address")) {
                address = columnIndex;
            }
            if (String.valueOf(column).equals("Street")) {
                street = columnIndex;
            }
            if (String.valueOf(column).equals("Zip Code/City")) {
                zipcodeCountry = columnIndex;
            }
            if (String.valueOf(column).equals("Canton")) {
                CANTON_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("In CVLabs")) {
                IN_CVL__COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("In LBC")) {
                IN_LP_COLUMN_INDEX = columnIndex;
            }
            if (String.valueOf(column).equals("twitter")) {
                twitter = columnIndex;
            }
            if (String.valueOf(column).equals("facebook")) {
                facebook = columnIndex;
            }
            if (String.valueOf(column).equals("slack")) {
                slack = columnIndex;
            }
            if (String.valueOf(column).equals("reddit")) {
                reddit = columnIndex;
            }
            if (String.valueOf(column).equals("forum")) {
                forum = columnIndex;
            }
            if (String.valueOf(column).equals("github")) {
                github = columnIndex;
            }
            if (String.valueOf(column).equals("medium")) {
                medium = columnIndex;
            }
            if (String.valueOf(column).equals("youtube")) {
                youtube = columnIndex;
            }
            if (String.valueOf(column).equals("linkedin")) {
                linkedin = columnIndex;
            }
            if (String.valueOf(column).equals("telegram")) {
                telegram = columnIndex;
            }
            if (String.valueOf(column).equals("Legal Entity")) {
                legalEntity = columnIndex;
            }

            if (String.valueOf(column).equals("Valuation USD")) {
                valuationUsd = columnIndex;
            }

            if (String.valueOf(column).equals("Abbreviation Token")) {
                abbreviationToken = columnIndex;
            }

            if (String.valueOf(column).equals("coinmarketcap url")) {
                coinmarketcapUrl = columnIndex;
            }

            columnIndex++;
        }
    }
}
