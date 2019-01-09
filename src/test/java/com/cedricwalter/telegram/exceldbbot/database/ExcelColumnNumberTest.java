package com.cedricwalter.telegram.exceldbbot.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcelColumnNumberTest {


    @Test
    public void toNumberTest() {
        assertTrue(ExcelColumnNumber.toName(0).equals("A"));
        assertTrue(ExcelColumnNumber.toName(1).equals("B"));
        assertTrue(ExcelColumnNumber.toName(31).equals("AF"));

        assertTrue(ExcelColumnNumber.toNumber("A") == 0);
        assertTrue(ExcelColumnNumber.toNumber("B") == 1);
        assertTrue(ExcelColumnNumber.toNumber("AE") == 30);
    }


}