package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//ul[@class='list-inline']//li[@class='dropdown']")
    private WebElement myAccountLink;

    @FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-right']//a[normalize-space()='Register']")
    private WebElement registerLink;

    @FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-right']//a[text()='Login']")
    private WebElement loginLink;

    public void clickOnMyAccount() {
        try {
            logger.info("Clicking My Account");
            wait.until(ExpectedConditions.elementToBeClickable(myAccountLink)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click My Account", e);
        }
    }

    public void clickOnRegister() {
        try {
            logger.info("Clicking Register");
            wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Register link", e);
        }
    }

    public void clickOnLogin() {
        try {
            logger.info("Clicking Login");
            wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Login link", e);
        }
    }
}
