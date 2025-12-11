package com.framework.utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;

public class ExcelUtility {

    private final String filePath;

    // Constructor to set the Excel file path
    public ExcelUtility(String filePath) {
        this.filePath = filePath;
    }

    /* ----------------------------- Helper Methods ----------------------------- */

    // Load workbook safely using try-with-resources
    private XSSFWorkbook loadWorkbook() throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return new XSSFWorkbook(fis);
        }
    }

    // Save workbook safely
    private void saveWorkbook(XSSFWorkbook workbook) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }

    /* ----------------------------- Sheet Operations ----------------------------- */

    // Returns the number of rows in a sheet
    public int getRowCount(String sheetName) throws IOException {
        try (XSSFWorkbook workbook = loadWorkbook()) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            return sheet != null ? sheet.getLastRowNum() : 0;
        }
    }

    // Returns the number of cells in a row
    public int getColumnCount(String sheetName, int rowIndex) throws IOException {
        try (XSSFWorkbook workbook = loadWorkbook()) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return 0;

            Row row = sheet.getRow(rowIndex);
            return row != null ? row.getLastCellNum() : 0;
        }
    }

    /* ----------------------------- Cell Read Operations ----------------------------- */

    // Returns string value from a cell
    public String readCell(String sheetName, int rowIndex, int colIndex) throws IOException {
        try (XSSFWorkbook workbook = loadWorkbook()) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return "";

            Row row = sheet.getRow(rowIndex);
            if (row == null) return "";

            Cell cell = row.getCell(colIndex);
            if (cell == null) return "";

            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell);
        }
    }

    /* ----------------------------- Cell Write Operations ----------------------------- */

    // Writes data to a specific cell
    public void writeCell(String sheetName, int rowIndex, int colIndex, String value) throws IOException {
        XSSFWorkbook workbook;

        // If file doesn't exist, create a new workbook
        File file = new File(filePath);
        if (!file.exists()) {
            workbook = new XSSFWorkbook();
            workbook.createSheet(sheetName);
            saveWorkbook(workbook);
        }

        // Now load it
        workbook = loadWorkbook();

        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) sheet = workbook.createSheet(sheetName);

        Row row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);

        Cell cell = row.getCell(colIndex);
        if (cell == null) cell = row.createCell(colIndex);

        cell.setCellValue(value);

        saveWorkbook(workbook);
        workbook.close();
    }

    /* ----------------------------- Cell Formatting ----------------------------- */

    // Fills the specified cell with a color
    private void fillCellColor(String sheetName, int rowIndex, int colIndex, IndexedColors color) throws IOException {
        XSSFWorkbook workbook = loadWorkbook();
        XSSFSheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) throw new IOException("Sheet not found: " + sheetName);

        Row row = sheet.getRow(rowIndex);
        if (row == null) throw new IOException("Row not found: " + rowIndex);

        Cell cell = row.getCell(colIndex);
        if (cell == null) throw new IOException("Cell not found: " + colIndex);

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(style);

        saveWorkbook(workbook);
        workbook.close();
    }

    // Apply green highlight
    public void highlightCellGreen(String sheetName, int rowIndex, int colIndex) throws IOException {
        fillCellColor(sheetName, rowIndex, colIndex, IndexedColors.LIGHT_GREEN);
    }

    // Apply red highlight
    public void highlightCellRed(String sheetName, int rowIndex, int colIndex) throws IOException {
        fillCellColor(sheetName, rowIndex, colIndex, IndexedColors.LIGHT_YELLOW);
    }

}
