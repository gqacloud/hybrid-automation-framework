package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.framework.driver.DriverManager;
import com.framework.utils.helpers.InteractionHelper;

/**
 * BasePage -------- Centralized page-level actions. All Page Objects should
 * extend this class.
 */
public class BasePage {

	protected WebDriver driver;
	protected InteractionHelper action;
	protected final Logger logger = LogManager.getLogger(getClass());

	/**
	 * BasePage constructor - Gets driver from DriverManager - Initializes
	 * PageFactory - Initializes InteractionHelper
	 */
	public BasePage() {
	    this.driver = DriverManager.getDriver();

	    if (this.driver == null) {
	        throw new IllegalStateException(
	            "WebDriver is NULL. @BeforeClass setup did not run. " +
	            "Do not use groups on setup/teardown."
	        );
	    }

	    PageFactory.initElements(driver, this);
	    this.action = new InteractionHelper(driver);
	}

	// =========================
	// CENTRALIZED ACTIONS
	// =========================

	protected void click(WebElement element) {
		action.safeClick(element);
	}

	protected void type(WebElement element, String text) {
		action.safeSendKeys(element, text);
	}

	protected void clear(WebElement element) {
		action.safeClear(element);
	}

	// =========================
	// TEXT HELPERS
	// =========================
	protected String getText(WebElement element) {
	    return action.getText(element);
	}

	protected String getText(WebElement element, String elementName) {
	    return action.getText(element, elementName);
	}

	// =========================
	// VALIDATION HELPERS
	// =========================

	protected boolean isDisplayed(WebElement element) {
	    try {
	        return element.isDisplayed();
	    } catch (Exception e) {
	        return false;
	    }
	}

	// =========================
	// PAGE INFO
	// =========================

	public String getPageTitle() {
		return driver.getTitle();
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}
}
