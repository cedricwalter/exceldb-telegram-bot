package com.cedricwalter.telegram.exceldbbot;

import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class ExcelHelperTest {

    public static final int CATEGORY_COLUMN_INDEX = 3;
    private static URL resource;
    private ExcelHelper excelHelper;

    @BeforeEach
    public void init() {
        excelHelper = new ExcelHelper();
        resource = ExcelHelperTest.class.getResource("/src/test/resources/index.xlsx");
    }

    @Test
    public void withIndexExcel_hasEntry_expectEntryNotFound() throws IOException {
        // Arrange
        // Act
        List<Row> rows = excelHelper.hasEntry(resource.getFile(), "anything-not-in-excel");

        // Assert
        assertTrue(rows.size() == 0);
    }

    @Test
    public void withIndexExcel_hasEntry_expectEntryFound() throws IOException {
        // Arrange
        // Act
        List<Row> rows = excelHelper.hasEntry(resource.getFile(), "cedric");

        // Assert
        assertTrue(rows.size() > 0);
    }

    @Test
    public void withIndexExcel_getCat_expectCategoriesFound() throws IOException {
        // Arrange
        // Act
        Set<String> uniqueColumnValues = excelHelper.getUniqueColumnValues(resource.getFile(), CATEGORY_COLUMN_INDEX);

        // Assert
        assertThat(uniqueColumnValues.size(), is(2));
    }

    @Test
    public void withIndexExcel_getStats_expectStatsCorrect() throws IOException {
        // Arrange
        // Act
        Map<String, String> stats = excelHelper.getStats(ExcelHelperTest.class.getResource("/src/test/resources/getStats-missingCategory.xlsx").getFile());

        // Assert
        String startup_missing_category = String.valueOf(stats.get("startup missing category"));
        assertThat(startup_missing_category, is("1"));
    }

}