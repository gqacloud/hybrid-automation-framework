package com.framework.utils.helpers;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * InteractionHelper ------------------ Centralized, headless-safe interaction
 * utilities. This class should be used by BasePage and all Page Objects.
 */
public class InteractionHelper {

	private static final Logger logger = LogManager.getLogger(InteractionHelper.class);

	private final WebDriver driver;
	private final WebDriverWait wait;

	public InteractionHelper(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// =========================
	// Page Load
	// =========================

	public void waitForPageLoad() {
		logger.debug("Waiting for the page to load");
		wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
	}

	// =========================
	// Wait Utilities
	// =========================

	public void waitForVisibility(By locator) {
		logger.debug("Waiting for visibility of element: {}", locator);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void waitForVisibility(WebElement element) {
		logger.debug("Waiting for visibility of element");
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void waitForClickable(By locator) {
		logger.debug("Waiting for element to be clickable: {}", locator);
		wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void waitForClickable(WebElement element) {
		logger.debug("Waiting for element to be clickable");
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	// =========================
	// Scroll Utilities
	// =========================

	public void scrollIntoView(WebElement element) {
		logger.debug("Scrolling element into view");
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
				element);
	}

	// =========================
	// Safe Actions
	// =========================

	/**
	 * Safe click with wait + scroll + JS fallback.
	 */
	public void safeClick(WebElement element) {
		try {
			waitForPageLoad();
			scrollIntoView(element);
			wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		} catch (Exception e) {
			logger.warn("Standard click failed, attempting JS click", e);
			jsClick(element);
		}
	}

	/**
	 * Safe sendKeys with wait + clear + type.
	 */
	public void safeSendKeys(WebElement element, String value) {
		waitForPageLoad();
		scrollIntoView(element);
		wait.until(ExpectedConditions.visibilityOf(element));
		element.clear();
		element.sendKeys(value);
	}

	/**
	 * Safe clear field.
	 */
	public void safeClear(WebElement element) {
		waitForVisibility(element);
		element.clear();
	}

	// =========================
	// JavaScript Helpers
	// =========================

	public void jsClick(WebElement element) {
		// logger.debug("Performing JS click");
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public void jsSendKeys(WebElement element, String value) {
		// logger.debug("Performing JS sendKeys");
		((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", element, value);
	}

	// =========================
	// Mouse Actions (Optional)
	// =========================

	public void hover(WebElement element) {
		// logger.debug("Hovering over element");
		new Actions(driver).moveToElement(element).pause(Duration.ofMillis(300)).perform();
	}

	// =========================
	// Validation Helpers
	// =========================

	public boolean isElementDisplayed(By locator, int timeoutSec) {
		try {
			new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
					.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}
}
