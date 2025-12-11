package com.framework.components;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.framework.pages.BasePage;

public class TableComponent extends BasePage {
	private WebElement table;

	public TableComponent(WebDriver driver, WebElement table) {
		super(driver);
		this.table = table;
		// TODO Auto-generated constructor stub
	}

	public int getRowCount() {
		return table.findElements(By.xpath("./tbody/tr")).size();
	}

	public int getColumnCount() {
		return table.findElements(By.xpath("./tbody/tr[1]/td")).size();
	}

	public String getCellValue(int row, int col) {
		return table.findElement(By.xpath("./tbody/tr[" + row + "]/td[" + col + "]")).getText().trim();
	}

	public List<String> getColumnValues(int col) {
		List<String> values = new ArrayList<>();
		List<WebElement> rows = table.findElements(By.xpath("./tbody/tr"));

		for (WebElement row : rows) {
			String text = row.findElement(By.xpath("./td[" + col + "]")).getText().trim();
			values.add(text);
		}
		return values;
	}

	public List<String> getRowValues(int row) {
		List<String> values = new ArrayList<>();
		List<WebElement> cols = table.findElements(By.xpath("./tbody/tr[" + row + "]/td"));

		for (WebElement colEl : cols) {
			values.add(colEl.getText().trim());
		}
		return values;
	}

	public WebElement getCellElement(int row, int col) {
		return table.findElement(By.xpath("./tbody/tr[" + row + "]/td[" + col + "]"));
	}

}
