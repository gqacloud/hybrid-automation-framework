package com.framework.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.framework.pages.BasePage;

public class TableComponent extends BasePage {

    private static final Logger logger = LogManager.getLogger(TableComponent.class);

    private final WebElement table;

    public TableComponent(WebDriver driver, WebElement table) {
        super();
        this.table = table;
    }

    private void ensureTableVisible() {
    	isDisplayed(table);
    }
    
    
    public int getRowCount() {
        try {
            logger.info("Fetching row count from table");
            ensureTableVisible();
            return table.findElements(By.xpath("./tbody/tr")).size();
        } catch (Exception e) {
            throw new RuntimeException("Unable to get table row count", e);
        }
    }

    public int getColumnCount() {
        try {
            logger.info("Fetching column count from table");
            ensureTableVisible();
            return table.findElements(By.xpath("./tbody/tr[1]/td")).size();
        } catch (Exception e) {
            throw new RuntimeException("Unable to get table column count", e);
        }
    }

    public String getCellValue(int row, int col) {
        try {
            logger.info("Fetching value at row {}, column {}", row, col);
            ensureTableVisible();

            validateCellCoordinates(row, col);

            return table.findElement(
                    By.xpath("./tbody/tr[" + row + "]/td[" + col + "]")
            ).getText().trim();

        } catch (Exception e) {
            throw new RuntimeException("Unable to get cell value at (" + row + ", " + col + ")", e);
        }
    }

    public List<String> getColumnValues(int col) {
        try {
            logger.info("Fetching all values from column {}", col);
            ensureTableVisible();

            validateColumnIndex(col);

            List<String> values = new ArrayList<>();
            List<WebElement> rows = table.findElements(By.xpath("./tbody/tr"));

            for (WebElement row : rows) {
                String value = row.findElement(By.xpath("./td[" + col + "]")).getText().trim();
                values.add(value);
            }

            return values;

        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch values for column " + col, e);
        }
    }

    public List<String> getRowValues(int row) {
        try {
            logger.info("Fetching all values from row {}", row);
            ensureTableVisible();

            validateRowIndex(row);

            List<String> values = new ArrayList<>();
            List<WebElement> columns = table.findElements(By.xpath("./tbody/tr[" + row + "]/td"));

            for (WebElement col : columns) {
                values.add(col.getText().trim());
            }

            return values;

        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch values for row " + row, e);
        }
    }

    public WebElement getCellElement(int row, int col) {
        try {
            logger.info("Returning cell WebElement at row {}, column {}", row, col);
            ensureTableVisible();

            validateCellCoordinates(row, col);

            return table.findElement(By.xpath("./tbody/tr[" + row + "]/td[" + col + "]"));

        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch cell element at (" + row + ", " + col + ")", e);
        }
    }

    // ---------------- VALIDATION HELPERS ---------------- //

    private void validateCellCoordinates(int row, int col) {
        validateRowIndex(row);
        validateColumnIndex(col);
    }

    private void validateRowIndex(int row) {
        int rowCount = getRowCount();
        if (row < 1 || row > rowCount) {
            throw new IllegalArgumentException("Invalid row index: " + row + ". Table has only " + rowCount + " rows.");
        }
    }

    private void validateColumnIndex(int col) {
        int colCount = getColumnCount();
        if (col < 1 || col > colCount) {
            throw new IllegalArgumentException("Invalid column index: " + col + ". Table has only " + colCount + " columns.");
        }
    }
}
