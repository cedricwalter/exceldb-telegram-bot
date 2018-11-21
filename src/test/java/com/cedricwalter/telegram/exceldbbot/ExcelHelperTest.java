package com.cedricwalter.telegram.exceldbbot;

import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
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

    @Test
    public void isWhiteListed() throws Exception {
        String user = "oh_MG";

        assertThat(isWhitelisted(user), is(true));
    }

    private boolean isWhitelisted(String user) {
        List<String> whiteList = Arrays.asList("oh_MG,CedricWalter,GLRalf,rogerdarin,viauri,rrkubli,mathiasruch,lukasetter,Fgordillo,Superruti,bumbacher,broechner,EugenLechner,Ellabee,Vince8802,damiirux".split(","));
        for (String name : whiteList) {
            if (user.equals(name)) {
                return true;
            }
        }
        return false;
    }


}