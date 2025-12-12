package com.framework.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final Logger logger = LogManager.getLogger(getClass());

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // --------------------- WAIT HELPERS --------------------- //

    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }


    // --------------------- ACTION HELPERS --------------------- //

    protected void safeClick(WebElement element) {
        try {
            logger.info("Clicking element: {}", element);
            waitForClickability(element).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click element: " + element, e);
        }
    }

    protected void safeType(WebElement element, String text) {
        try {
            logger.info("Typing '{}' into element: {}", text, element);
            WebElement el = waitForVisibility(element);
            el.clear();
            el.sendKeys(text);
        } catch (Exception e) {
            throw new RuntimeException("Failed to type into element: " + element, e);
        }
    }

    protected String safeGetText(WebElement element) {
        try {
            logger.info("Getting text from element: {}", element);
            return waitForVisibility(element).getText().trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get text from element: " + element, e);
        }
    }


    // --------------------- JAVASCRIPT HELPERS --------------------- //

    protected void scrollIntoView(WebElement element) {
        try {
            logger.info("Scrolling element into view: {}", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            throw new RuntimeException("Failed to scroll element into view: " + element, e);
        }
    }

    protected void clickByJS(WebElement element) {
        try {
            logger.info("Clicking element using JavaScript: {}", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click element using JavaScript: " + element, e);
        }
    }


    // --------------------- UTILITY METHODS --------------------- //

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isDisplayed(WebElement element) {
        try {
            return waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
