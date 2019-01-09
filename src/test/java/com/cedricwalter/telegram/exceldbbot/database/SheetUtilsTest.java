package com.cedricwalter.telegram.exceldbbot.database;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SheetUtilsTest {

    @Test
    public void test() {


        String s = SheetUtils.columnToLetter(1);
        System.out.println(s);
    }



}