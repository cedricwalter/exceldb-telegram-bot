package com.cedricwalter.telegram.exceldbbot;

import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class ExcelHelperTest {

    public static final int CATEGORY_COLUMN_INDEX = 3;
    private static URL resource;

    @BeforeAll
    public static void init() {
        resource = ExcelHelperTest.class.getResource("/index.xlsx");
    }

    @Test
    public void withIndexExcel_hasEntry_expectEntryNotFound() throws IOException {
        // Arrange
        ExcelHelper excelHelper = new ExcelHelper();


        // Act
        List<Row> rows = excelHelper.hasEntry(resource.getFile(), "anything-not-in-excel");

        // Assert
        assertTrue(rows.size() == 0);
    }

    @Test
    public void withIndexExcel_hasEntry_expectEntryFound() throws IOException {
        // Arrange
        ExcelHelper excelHelper = new ExcelHelper();

        // Act
        List<Row> rows = excelHelper.hasEntry(resource.getFile(), "cedric");

        // Assert
        assertTrue(rows.size() > 0);
    }

    @Test
    public void withIndexExcel_getCat_expectCategoriesFound() throws IOException {
        // Arrange
        ExcelHelper excelHelper = new ExcelHelper();

        // Act
        Set<String> uniqueColumnValues = excelHelper.getUniqueColumnValues(resource.getFile(), CATEGORY_COLUMN_INDEX);

        // Assert
        assertThat(uniqueColumnValues.size(), is(2));
    }

}