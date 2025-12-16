package com.framework.utils.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility2 {

    private String path;

    public ExcelUtility2(String path) {
        this.path = path;
    }

    // -------------------------------
    // GET ROW COUNT
    // -------------------------------
    public int getRowCount(String sheetName) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            return sheet.getLastRowNum();
        }
    }

    // -------------------------------
    // GET CELL COUNT
    // -------------------------------
    public int getCellCount(String sheetName, int rownum) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            XSSFRow row = sheet.getRow(rownum);
            return row.getLastCellNum();
        }
    }

    // -------------------------------
    // GET CELL DATA
    // -------------------------------
    public String getCellData(String sheetName, int rownum, int colnum) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            XSSFRow row = sheet.getRow(rownum);
            XSSFCell cell = row.getCell(colnum);

            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell);
        } catch (Exception e) {
            return "";
        }
    }

    // -------------------------------
    // SET CELL DATA
    // -------------------------------
    public void setCellData(String sheetName, int rownum, int colnum, String data) throws IOException {

        File file = new File(path);

        // Create file if it doesn't exist
        if (!file.exists()) {
            try (XSSFWorkbook workbook = new XSSFWorkbook();
                 FileOutputStream fos = new FileOutputStream(path)) {
                workbook.write(fos);
            }
        }

        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);

            // Create sheet if not exists
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            XSSFRow row = sheet.getRow(rownum);
            if (row == null) row = sheet.createRow(rownum);

            XSSFCell cell = row.getCell(colnum);
            if (cell == null) cell = row.createCell(colnum);

            cell.setCellValue(data);

            try (FileOutputStream fos = new FileOutputStream(path)) {
                workbook.write(fos);
            }
        }
    }

    // -------------------------------
    // FILL GREEN COLOR
    // -------------------------------
    public void fillGreenColor(String sheetName, int rownum, int colnum) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            XSSFRow row = sheet.getRow(rownum);
            XSSFCell cell = row.getCell(colnum);

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cell.setCellStyle(style);

            try (FileOutputStream fos = new FileOutputStream(path)) {
                workbook.write(fos);
            }
        }
    }

    // -------------------------------
    // FILL RED COLOR
    // -------------------------------
    public void fillRedColor(String sheetName, int rownum, int colnum) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            XSSFRow row = sheet.getRow(rownum);
            XSSFCell cell = row.getCell(colnum);

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cell.setCellStyle(style);

            try (FileOutputStream fos = new FileOutputStream(path)) {
                workbook.write(fos);
            }
        }
    }

    // -------------------------------
    // GET ENTIRE SHEET DATA (2D ARRAY)
    // Perfect for TestNG DataProvider
    // -------------------------------
    public Object[][] getSheetData(String sheetName) throws IOException {

        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            DataFormatter formatter = new DataFormatter();

            int totalRows = sheet.getLastRowNum();          // excluding header row
            int totalCols = sheet.getRow(0).getLastCellNum();

            Object[][] data = new Object[totalRows][totalCols];

            for (int i = 1; i <= totalRows; i++) {          // start at row 1 (skip header)
                XSSFRow row = sheet.getRow(i);

                for (int j = 0; j < totalCols; j++) {
                    XSSFCell cell = row.getCell(j);
                    data[i - 1][j] = formatter.formatCellValue(cell);
                }
            }

            return data;
        }
    }
}
