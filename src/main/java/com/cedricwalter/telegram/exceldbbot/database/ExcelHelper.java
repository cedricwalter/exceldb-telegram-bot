package com.cedricwalter.telegram.exceldbbot.database;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class ExcelHelper {

    public Set<String> getStruct() throws Exception {
        Set<String> potential = new HashSet<>();

        List<List<Object>> rows = GoogleSheet.getSwissRows();
        for (List<Object> row : rows) {
            String category = getValueSafe(row, ExcelIndexes.CATEGORY_COLUMN_INDEX);
            String subcategory = getValueSafe(row, ExcelIndexes.SUB_CATEGORY_COLUMN_INDEX);
            if (category != null) {
                potential.add(category.replaceAll(" ", "-") + "|" + subcategory.replaceAll(" ", "-"));
            }
        }
        return potential;
    }

    public List<String> geColumnValues(int columnIndex, List<List<Object>> rows) throws Exception {
        List<String> potential = new LinkedList<>();

        for (List<Object> row : rows) {
            if (row.size() < columnIndex) {
                continue;
            }

            Object value = row.get(columnIndex);

            if (value != null) {
                potential.add(String.valueOf(value));
            }
        }
        return potential;
    }

    public Set<String> getUniqueColumnValues(int columnIndex, List<List<Object>> rows) throws Exception {
        Set<String> potential = new LinkedHashSet<>();

        for (List<Object> row : rows) {
            if (row.size() < columnIndex) {
                continue;
            }

            Object value = row.get(columnIndex);

            if (value != null) {
                potential.add(String.valueOf(value));
            }
        }
        return potential;
    }

    public Set<String> getNameForColumnMatching(int columnIndex, String value) throws Exception {
        Set<String> potential = new HashSet<>();

        List<List<Object>> rows = GoogleSheet.getSwissRows();
        for (List<Object> row : rows) {

            String match = getValueSafe(row, columnIndex);
            if (match.equals(value)) {
                potential.add(getValueSafe(row, ExcelIndexes.name) + " (" +
                        getValueSafe(row, ExcelIndexes.CATEGORY_COLUMN_INDEX) + "/" +
                        getValueSafe(row, ExcelIndexes.SUB_CATEGORY_COLUMN_INDEX) + ")"
                );
            }

        }
        return potential;
    }

    public static String getValueSafe(List<Object> currentRow, int columnIndex) {

        Object obj = currentRow.size() > columnIndex ? currentRow.get(columnIndex) : "";

        String string = String.valueOf(obj);
        return string;
    }

    public Set<List<Object>> hasEntry(String entry, List<List<Object>> source) throws Exception {
        Set<List<Object>> potential = new HashSet<>();

        for (List<Object> row : source) {

            String nameString = getValueSafe(row, ExcelIndexes.name);
            String urlString = getValueSafe(row, ExcelIndexes.webpage);

            if (nameString != null && urlString != null) {

                Pattern compile = Pattern.compile(Pattern.quote(entry), Pattern.CASE_INSENSITIVE);
                boolean inName = compile.matcher(nameString).find();
                boolean inUrl = compile.matcher(urlString).find();

                if (inName || inUrl) {
                    potential.add(row);
                }
            }
        }
        return potential;
    }



    /**
     * Telegram has a max size message limit
     * return list of string no longer than 4096 bytes
     *
     * @return
     */
    public static List<String> toString(Set<List<Object>> rows) {
        List<String> rowList = new ArrayList<>(rows.size());

        //str.append("Too much results (" + rows.size() + "), displaying in condensed form\n\n");
        for (List<Object> row : rows) {
            StringBuilder str = new StringBuilder();
            appendIfNotNull(str, getValueSafe(row, ExcelIndexes.name));
            appendIfNotNull(str, getValueSafe(row, ExcelIndexes.CATEGORY_COLUMN_INDEX));
            appendIfNotNull(str, getValueSafe(row, ExcelIndexes.SUB_CATEGORY_COLUMN_INDEX));
            appendIfNotNull(str, getValueSafe(row, ExcelIndexes.webpage));
            appendIfNotNull(str, getValueSafe(row, ExcelIndexes.MOTTO_COLUMN_INDEX));
            appendIfNotNull(str, getValueSafe(row, ExcelIndexes.DESCRIPTION_COLUMN_INDEX));
            rowList.add(str.toString());
        }

        return rowList;
    }

    private static void appendIfNotNull(StringBuilder str, String categoriesString) {
        str.append(categoriesString);
        str.append("  |  ");
    }

    public Map<String, String> getStats(List<List<Object>> rows) throws Exception {
        Map statistics = new TreeMap();

        long missingCategoryCounter = 0;
        long missingSubcategoryCounter = 0;
        long missingMottoCounter = 0;
        long missingDescriptionCounter = 0;
        long missingAddressesCounter = 0;
        long missingLatCounter = 0;
        long missingLongCounter = 0;
        long missingLogo = 0;

        StringBuilder missingAddresses = new StringBuilder();
        StringBuilder missingLat = new StringBuilder();
        StringBuilder missingLong = new StringBuilder();
        StringBuilder missingCategory = new StringBuilder();
        StringBuilder missingSubCategory = new StringBuilder();
        StringBuilder missingMotto = new StringBuilder();
        StringBuilder missingDescription = new StringBuilder();
        for (List<Object> currentRow : rows) {

            missingCategoryCounter = report(missingCategoryCounter, missingCategory, currentRow, ExcelIndexes.CATEGORY_COLUMN_INDEX);
            missingSubcategoryCounter = report(missingSubcategoryCounter, missingSubCategory, currentRow, ExcelIndexes.SUB_CATEGORY_COLUMN_INDEX);
            missingMottoCounter = report(missingMottoCounter, missingMotto, currentRow, ExcelIndexes.MOTTO_COLUMN_INDEX);
            missingDescriptionCounter = report(missingDescriptionCounter, missingDescription, currentRow, ExcelIndexes.DESCRIPTION_COLUMN_INDEX);

            missingAddressesCounter = report(missingAddressesCounter, missingAddresses, currentRow, ExcelIndexes.zipcodeCountry);
            missingLatCounter = report(missingLatCounter, missingLat, currentRow, ExcelIndexes.LAT_COLUMN_INDEX);
            missingLongCounter = report(missingLongCounter, missingLong, currentRow, ExcelIndexes.LONG_COLUMN_INDEX);
        }

        statistics.put("number of startup", rows.size());


        // addStats(statistics, missingLogo, missingLogoNames, "- missing logo");
        addStats(statistics, missingAddressesCounter, missingAddresses, "- missing addresses");
        addStats(statistics, missingCategoryCounter, missingCategory, "- missing category");
        addStats(statistics, missingSubcategoryCounter, missingSubCategory, "- missing subcategory");

        addStats(statistics, missingMottoCounter, missingMotto, "- missing motto");
        addStats(statistics, missingDescriptionCounter, missingDescription, "- missing description");
        addStats(statistics, missingLatCounter, missingLat, "- missing lat");
        addStats(statistics, missingLongCounter, missingLong, "- missing long");

        return statistics;
    }

    public static boolean exists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            //        HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con =
                    (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private void addStats(Map statistics, long counter, StringBuilder stringBuilder, String message) {
        if (counter != 0) {
            statistics.put(message, counter + ":\n" + stringBuilder.toString());
        }
    }

    private long report(long counter, StringBuilder stringBuilder, List<Object> currentRow, int column) {
        long l = incrementCounterIfStringEmpty(currentRow, column);
        if (l == 1) {
            counter += l;
            String name = String.valueOf(currentRow.get(0));
            if (name != null) {
                stringBuilder.append("   - ").append(name).append("\n");
            }
        }
        return counter;
    }

    public static boolean fileExistsCaseSensitive(String path) {
        try {
            File file = new File(path);
            return file.exists() && file.getCanonicalFile().getName().equals(file.getName());
        } catch (Exception e) {
            return false;
        }
    }


    private long incrementCounterIfStringEmpty(List<Object> currentRow, int column) {
        long index = 0;
        if (currentRow.size() > column) {
            String string = java.lang.String.valueOf(currentRow.get(column));

            if ("".equals(string)) {
                index++;
            }
        }

        return index;
    }
}
