package com.cedricwalter.telegram.exceldbbot.scrapper;

import com.cedricwalter.telegram.exceldbbot.database.ExcelColumnNumber;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LatLongScrapper {

    private int linkCount = 0;
    private int errors = 0;
    private String apiKey = "AIzaSyDoFsHMLWFiShgjiZZqrhuoieyr3yHH08g";
    private String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json?key=" + apiKey + "&address=";

    public static void main(String[] args) throws Exception {
         new LatLongScrapper().analyze(GoogleSheet.getSingaporeRows(), GoogleSheet.SINGAPORE_SHEET_ID);
        // new LatLongScrapper().analyze(GoogleSheet.getSwissRows(), GoogleSheet.SWISS_SHEET_ID);
    }

    private void analyze(List<List<Object>> rows, String sheetId) throws Exception {
        int i = 1; // jump header
        rows = rows.subList(1, rows.size());
        for (List<Object> row : rows) {

            String street = getSafeValue(row, ExcelIndexes.street);


            String address = URLEncoder.encode(street + " " +
                    getSafeValue(row, ExcelIndexes.address) + " " +
                    getSafeValue(row, ExcelIndexes.zipcodeCountry), "UTF-8");
            String existingLatitude = getSafeValue(row, ExcelIndexes.LAT_COLUMN_INDEX);
            String existingLongitude = getSafeValue(row, ExcelIndexes.LONG_COLUMN_INDEX);

            String name = getSafeValue(row, ExcelIndexes.name);

            try {
                if (!street.isEmpty() && !street.equals("N/A") && existingLatitude.isEmpty()) {

                    JsonObject location = getLocation(address);

                    String newlatitude = location.get("lat").toString();
                    String newLongitude = location.get("lng").toString();

                    if (!existingLatitude.equals(newlatitude) ||
                            !existingLongitude.equals(newLongitude)
                    ) {
                        List<List<Object>> values = Arrays.asList(
                                Arrays.asList(
                                        newlatitude, newLongitude
                                )

                        );
                        System.out.println(name +
                                " latitude or longitude not identical so replacing " + existingLatitude + " with " + newlatitude + " and " +
                                existingLongitude + " with " + newLongitude);

                        ArrayList<ValueRange> data = new ArrayList<>();
                        data.add(new ValueRange()
                                .setRange("active!" + ExcelColumnNumber.toName(ExcelIndexes.LAT_COLUMN_INDEX) + i + ":" + ExcelColumnNumber.toName(ExcelIndexes.LONG_COLUMN_INDEX) + i)
                                .setValues(values));

                        GoogleSheet.update(data, sheetId);
                        linkCount++;
                    }
                }
            } catch (Exception e) {
                System.err.println("not found for line " + name + " " + address);
                errors++;
            }

            i++;
        }

        System.out.println("analyzed  " + i + " entries " + linkCount + " added and found " + errors + " errors");
    }

    private String getSafeValue(List<Object> row, int columnIndex) {
        return row.size() >= columnIndex ? row.get(columnIndex).toString() : "";
    }

    private JsonObject getLocation(String address) throws IOException {
        String sURL = baseUrl + address + "&sensor=false";

        URL urlObj = new URL(sURL);
        URLConnection request = urlObj.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject rootobj = root.getAsJsonObject();

        JsonArray results = rootobj.getAsJsonArray("results");
        JsonObject entry = results.get(0).getAsJsonObject();
        JsonObject geometry = entry.getAsJsonObject("geometry");
        return geometry.getAsJsonObject("location");
    }

}
