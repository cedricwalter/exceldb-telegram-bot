package com.cedricwalter.telegram.exceldbbot.database;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoogleSheet {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS
    );
    private static final String CLIENT_SECRET_DIR = "/client_secret.json2";

    public static String SWISS_SHEET_ID = "1Awu0tOG8MBzWU8odiQS9LSW1Y1qqZ8QJaxn96QK87a4";
    public static String SINGAPORE_SHEET_ID = "1p9EnDgNa4zzcAJvLQHhcbdReYD3_hwTBQThCgqJRXrY";

    private static String TOP30_DIRECTORY_SHEET_ID = "1cum9GOnjKZ-WiR_AiynmgjA5Jy8gL2QcMtPi974C-HU";


    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws Exception If there is no client_secret.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        // Load client secrets.
        InputStream in = GoogleSheet.class.getResourceAsStream(CLIENT_SECRET_DIR);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static List<List<Object>> getSwissRows() throws Exception {
        List<List<Object>> rows = getRows(SWISS_SHEET_ID, "active!A:AZ");
        ExcelIndexes.buildIndex(rows.get(0));

        return rows;
    }

    public static List<List<Object>> getSingaporeRows() throws Exception {
        List<List<Object>> list = getRows(SINGAPORE_SHEET_ID, "active!A:AZ");
        ExcelIndexes.buildIndex(list.get(0));
        return list;

    }

    public static List<List<Object>> getTop30() throws Exception {
        return getRows(TOP30_DIRECTORY_SHEET_ID, "TOP 40 - active!A:AA");
    }

    public static List<List<Object>> getRows(String sheetId, String range) throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(sheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
            return null;
        } else {
            return values;
        }
    }



    public static void update(ArrayList<ValueRange> data, String swissSheetId) throws Exception {
        Sheets service = getService();

        String valueInputOption = "RAW";
        BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                .setValueInputOption(valueInputOption)
                .setData(data);

        BatchUpdateValuesResponse result =
                service.spreadsheets().values().batchUpdate(swissSheetId, body).execute();
        // System.out.println(result.getTotalUpdatedCells() + " cells updated.");
    }

    private static Sheets getService() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}