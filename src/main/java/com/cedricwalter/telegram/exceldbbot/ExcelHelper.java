package com.cedricwalter.telegram.exceldbbot;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.BotConfig;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class ExcelHelper {

    public static final int LAT_COLUMN_INDEX = 15;
    public static final int LONG_COLUMN_INDEX = 16;


    public static final int NAME_COLUMN_INDEX = 0;
    public static final int CATEGORY_COLUMN_INDEX = 1;
    public static final int SUBCATEGORY_COLUMN_INDEX = 2;
    public static final int URL_COLUMN_INDEX = 3;
    public static final int MOTTO_COLUMN_INDEX = 4;
    public static final int DESCRIPTION_COLUMN_INDEX = 5;
    public static final int ADDRESS1_COLUMN_INDEX = 7;
    public static final int ADDRESS2_COLUMN_INDEX = 8;
    public static final int ADDRESS3_COLUMN_INDEX = 9;

    public Set<String> getStruct(String excelFileName) throws IOException {
        XSSFWorkbook workbook = getWorkbook(excelFileName);

        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();

        Set<String> potential = new HashSet<>();
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Cell cellCat = currentRow.getCell(CATEGORY_COLUMN_INDEX);
            Cell cellSubCat = currentRow.getCell(SUBCATEGORY_COLUMN_INDEX);
            if (cellCat != null) {
                potential.add(cellCat.getStringCellValue().replaceAll(" ", "-") + "|" + cellSubCat.getStringCellValue().replaceAll(" ", "-"));
            }
        }
        return potential;
    }


    public Set<String> getUniqueColumnValues(String excelFileName, int columnIndex) throws IOException {
        XSSFWorkbook workbook = getWorkbook(excelFileName);

        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();

        Set<String> potential = new HashSet<>();
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Cell cell = currentRow.getCell(columnIndex);
            if (cell != null) {
                potential.add(cell.getStringCellValue());
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

                Pattern compile = Pattern.compile(Pattern.quote(entry), Pattern.CASE_INSENSITIVE);
                boolean inName = compile.matcher(nameCell.getStringCellValue()).find();
                boolean inUrl = compile.matcher(urlCell.getStringCellValue()).find();

                if (inName || inUrl) {
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
        Map statistics = new TreeMap();

        BotConfig config = new BotConfig();

        long missingCategoryCounter = 0;
        long missingSubcategoryCounter = 0;
        long missingMottoCounter = 0;
        long missingDescriptionCounter = 0;
        long missingAddressesCounter = 0;
        long missingLatCounter = 0;
        long missingLongCounter = 0;
        long missingLogo = 0;

        XSSFWorkbook workbook = getWorkbook(excelFileName);
        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();


        StringBuilder missingLogoNames = new StringBuilder();
        Set<String> names = getUniqueColumnValues(excelFileName, 0);
        for (String name : names) {
            String trim = name.trim();
            if (!fileExistsCaseSensitive(config.getLogoPath() + trim + ".png")) {
                missingLogoNames.append(trim+".png").append("\n");
                missingLogo++;
            }
        }

        StringBuilder missingAddresses = new StringBuilder();
        StringBuilder missingLat = new StringBuilder();
        StringBuilder missingLong = new StringBuilder();
        StringBuilder missingCategory = new StringBuilder();
        StringBuilder missingSubCategory = new StringBuilder();
        StringBuilder missingMotto = new StringBuilder();
        StringBuilder missingDescription = new StringBuilder();
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();

            missingCategoryCounter = report(missingCategoryCounter, missingCategory, currentRow, CATEGORY_COLUMN_INDEX);
            missingSubcategoryCounter = report(missingSubcategoryCounter, missingSubCategory, currentRow, SUBCATEGORY_COLUMN_INDEX);
            missingMottoCounter = report(missingMottoCounter, missingMotto, currentRow, MOTTO_COLUMN_INDEX);
            missingDescriptionCounter = report(missingDescriptionCounter, missingDescription, currentRow, DESCRIPTION_COLUMN_INDEX);

            missingAddressesCounter = report(missingAddressesCounter, missingAddresses, currentRow, ADDRESS3_COLUMN_INDEX);
            missingLatCounter = report(missingLatCounter, missingLat, currentRow, LAT_COLUMN_INDEX);
            missingLongCounter = report(missingLongCounter, missingLong, currentRow, LONG_COLUMN_INDEX);
        }

        statistics.put("number of startup", dataTypeSheet.getPhysicalNumberOfRows());

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        statistics.put("Last modified", sdf.format(new File(excelFileName).lastModified()));

        addStats(statistics, missingLogo, missingLogoNames, "- missing logo");
        addStats(statistics, missingAddressesCounter, missingAddresses, "- missing addresses");
        addStats(statistics, missingCategoryCounter, missingCategory, "- missing category");
        addStats(statistics, missingSubcategoryCounter, missingSubCategory, "- missing subcategory");

        addStats(statistics, missingMottoCounter, missingMotto, "- missing motto");
        addStats(statistics, missingDescriptionCounter, missingDescription, "- missing description");
        addStats(statistics, missingLatCounter, missingLat, "- missing lat");
        addStats(statistics, missingLongCounter, missingLong, "- missing long");

        return statistics;
    }

    private void addStats(Map statistics, long counter, StringBuilder stringBuilder, String message) {
        if (counter != 0) {
            statistics.put(message, counter + ":\n" + stringBuilder.toString());
        }
    }

    private long report(long counter, StringBuilder stringBuilder, Row currentRow, int column) {
        long l = incrementCounterIfCellEmpty(currentRow, column);
        if (l == 1) {
            counter += l;
            Cell cell = currentRow.getCell(0);
            if (cell != null) {
                stringBuilder.append("   - ").append(cell.getStringCellValue()).append("\n");
            }
        }
        return counter;
    }

    public static boolean fileExistsCaseSensitive(String path) {
        try {
            File file = new File(path);
            return file.exists() && file.getCanonicalFile().getName().equals(file.getName());
        } catch (IOException e) {
            return false;
        }
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
