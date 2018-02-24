package com.cedricwalter.telegram.exceldbbot;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelHelper {

    public static final int NAME_COLUMN_INDEX = 2;
    public static final int CATEGORY_COLUMN_INDEX = 3;
    public static final int SUBCATEGORY_COLUMN_INDEX = 4;
    public static final int URL_COLUMN_INDEX = 5;
    public static final int MOTTO_COLUMN_INDEX = 6;
    public static final int DESCRIPTION_COLUMN_INDEX = 7;

    public Set<String> getUniqueColumnValues(String excelFileName, int columnIndex) throws IOException {
        XSSFWorkbook workbook = getWorkbook(excelFileName);

        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();

        Set<String> potential = new HashSet<>();
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Cell category = currentRow.getCell(columnIndex);
            if (category != null) {
                potential.add(category.getStringCellValue());
            }
        }
        return potential;
    }

    public List<Row> hasEntry(String fileName, String entry) throws IOException {
        XSSFWorkbook workbook = getWorkbook(fileName);

        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();

        List<Row> potential = new ArrayList<>();
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();

            Cell nameCell = currentRow.getCell(NAME_COLUMN_INDEX);
            Cell urlCell = currentRow.getCell(URL_COLUMN_INDEX);

            if (nameCell != null && urlCell != null) {
                if (nameCell.getStringCellValue().contains(entry) || urlCell.getStringCellValue().contains(entry)) {
                    potential.add(currentRow);
                }
            }
        }
        return potential;
    }

    public boolean addEntry(String fileName, String name, String category, String subCategory, String url) throws IOException {
        XSSFWorkbook workbook = getWorkbook(fileName);
        Sheet dataTypeSheet = workbook.getSheetAt(0);

        int rowNum = dataTypeSheet.getPhysicalNumberOfRows();
        Row row = dataTypeSheet.createRow(rowNum++);

        row.createCell(NAME_COLUMN_INDEX).setCellValue(name);
        row.createCell(CATEGORY_COLUMN_INDEX).setCellValue(category);
        row.createCell(SUBCATEGORY_COLUMN_INDEX).setCellValue(subCategory);
        row.createCell(URL_COLUMN_INDEX).setCellValue(url);

        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static String toString(List<Row> rows) {
        StringBuilder str = new StringBuilder();

        //Telegram has a max size message limit
        boolean displayAll = true;

        if (rows.size() > 5) {
            displayAll = false;
            str.append("Too much results (" + rows.size() + "), displaying in condensed form\n\n");
        }
        for (Row row : rows) {
            appendIfNotNull(str, row.getCell(NAME_COLUMN_INDEX));
            appendIfNotNull(str, row.getCell(CATEGORY_COLUMN_INDEX));
            appendIfNotNull(str, row.getCell(SUBCATEGORY_COLUMN_INDEX));
            appendIfNotNull(str, row.getCell(URL_COLUMN_INDEX));
            if (displayAll) {
                appendIfNotNull(str, row.getCell(MOTTO_COLUMN_INDEX));
                appendIfNotNull(str, row.getCell(DESCRIPTION_COLUMN_INDEX));
            }

            str.append("\n\n");
        }

        return str.toString();
    }

    private static void appendIfNotNull(StringBuilder str, Cell categoriesCell) {
        if (categoriesCell != null) {
            str.append(categoriesCell.getStringCellValue());
            str.append("  |  ");
        }
    }

    private XSSFWorkbook getWorkbook(String excelFileName) throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(excelFileName));

        return new XSSFWorkbook(excelFile);
    }
}
