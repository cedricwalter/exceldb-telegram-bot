package com.cedricwalter.telegram.exceldbbot;

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
    public void withIndexExcel_hasEntry_expectEntryNotFound() throws Exception {
        // Arrange
        // Act
        Set<List<Object>> rows = excelHelper.hasEntry("anything-not-in-excel");

        // Assert
        assertTrue(rows.size() == 0);
    }

    @Test
    public void withIndexExcel_hasEntry_expectEntryFound() throws Exception {
        // Arrange
        // Act
        Set<List<Object>> rows = excelHelper.hasEntry("cedric");

        // Assert
        assertTrue(rows.size() > 0);
    }

    @Test
    public void withIndexExcel_getCat_expectCategoriesFound() throws Exception {
        // Arrange
        // Act
        Set<String> uniqueColumnValues = excelHelper.getUniqueColumnValues(CATEGORY_COLUMN_INDEX);

        // Assert
        assertThat(uniqueColumnValues.size(), is(2));
    }


}