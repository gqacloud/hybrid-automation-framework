package com.framework.utils.data;

import java.io.IOException;
import org.testng.annotations.DataProvider;

public class DataProviders {

    // ======================================================
    // DataProvider 1 — Reads login data from Excel properly
    // ======================================================

    @DataProvider(name = "LoginData")
    public Object[][] getLoginData() throws IOException {

        // Always use a dynamic project path
        String path = System.getProperty("user.dir") + "/testData/Users.xlsx";
        String sheetName = "LoginData";

        ExcelUtility2 xlutil = new ExcelUtility2(path);

        int totalRows = xlutil.getRowCount(sheetName);      // including header
        int totalCols = xlutil.getCellCount(sheetName, 1); // header row

        // Skip header row → row 0 is header
        Object[][] loginData = new Object[totalRows - 1][totalCols];

        for (int row = 1; row < totalRows; row++) {
            for (int col = 0; col < totalCols; col++) {
                loginData[row - 1][col] = xlutil.getCellData(sheetName, row, col);
            }
        }

        return loginData;
    }

    // ======================================================
    // DataProvider 2 — Template
    // ======================================================
    @DataProvider(name = "DP2")
    public Object[][] dataProvider2() {
        return new Object[][] {
            // {"value1", "value2"}
        };
    }

    // ======================================================
    // DataProvider 3 — Template
    // ======================================================
    @DataProvider(name = "DP3")
    public Object[][] dataProvider3() {
        return new Object[][] {
            // Add your data
        };
    }
}
