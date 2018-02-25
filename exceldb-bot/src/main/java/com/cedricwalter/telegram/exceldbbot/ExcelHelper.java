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
    public static final int ADDRESSE_COLUMN_INDEX = 9;
    public static final int LAT_COLUMN_INDEX = 14;
    public static final int LONG_COLUMN_INDEX = 15;

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

    public int count(String fileName) throws IOException {
        XSSFWorkbook workbook = getWorkbook(fileName);
        Sheet dataTypeSheet = workbook.getSheetAt(0);

        int rowNum = dataTypeSheet.getPhysicalNumberOfRows();

        return rowNum;
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


    /**
     * Telegram has a max size message limit
     * return list of string no longer than 4096 bytes
     *
     * @param excelRows
     * @return
     */
    public static List<String> toString(List<Row> excelRows) {
        List<String> rowList = new ArrayList<>(excelRows.size());

        //str.append("Too much results (" + rows.size() + "), displaying in condensed form\n\n");
        for (Row row : excelRows) {
            StringBuilder str = new StringBuilder();
            appendIfNotNull(str, row.getCell(NAME_COLUMN_INDEX));
            appendIfNotNull(str, row.getCell(CATEGORY_COLUMN_INDEX));
            appendIfNotNull(str, row.getCell(SUBCATEGORY_COLUMN_INDEX));
            appendIfNotNull(str, row.getCell(URL_COLUMN_INDEX));
            appendIfNotNull(str, row.getCell(MOTTO_COLUMN_INDEX));
            appendIfNotNull(str, row.getCell(DESCRIPTION_COLUMN_INDEX));
            rowList.add(str.toString());
        }

        return rowList;
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

    public Map<String, String> getStats(String excelFileName) throws IOException {
        Map statistics = new HashMap();

        long missingCategory = 0;
        long missingSubcategory = 0;
        long missingMotto = 0;
        long missingDescription = 0;
        long missingAdresses = 0;
        long missinglatLong = 0;

        XSSFWorkbook workbook = getWorkbook(excelFileName);
        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();

            missingCategory += incrementCounterIfCellEmpty(currentRow, CATEGORY_COLUMN_INDEX);
            missingSubcategory += incrementCounterIfCellEmpty(currentRow, SUBCATEGORY_COLUMN_INDEX);
            missingMotto += incrementCounterIfCellEmpty(currentRow, MOTTO_COLUMN_INDEX);
            missingDescription += incrementCounterIfCellEmpty(currentRow, DESCRIPTION_COLUMN_INDEX);
            missingAdresses += incrementCounterIfCellEmpty(currentRow, ADDRESSE_COLUMN_INDEX);
            missinglatLong += incrementCounterIfCellEmpty(currentRow, LAT_COLUMN_INDEX);
            missinglatLong += incrementCounterIfCellEmpty(currentRow, LONG_COLUMN_INDEX);
        }

        statistics.put("number of startup", dataTypeSheet.getPhysicalNumberOfRows());
        statistics.put("startup missing category", missingCategory);
        statistics.put("startup missing subcategory", missingSubcategory);
        statistics.put("startup missing motto", missingMotto);
        statistics.put("startup missing description", missingDescription);
        statistics.put("startup missing addresses", missingAdresses);
        statistics.put("startup missing lat,long", missinglatLong);

        return statistics;
    }

    private long incrementCounterIfCellEmpty(Row currentRow, int column) {
        Cell cell = currentRow.getCell(column);
        long index = 0;
        if (cell != null) {

            if (cell.getCellTypeEnum().equals(CellType.STRING)) {
                String stringCellValue = cell.getStringCellValue().trim();
                if (stringCellValue.length() == 0) {
                    index++;
                }
            } else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                if (cell.getNumericCellValue() == 0) {
                    index++;
                }
            }
        } else {
            index++;
        }

        return index;
    }
}
