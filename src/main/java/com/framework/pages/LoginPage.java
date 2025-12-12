package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "input-email")
    private WebElement emailTextbox;

    @FindBy(id = "input-password")
    private WebElement passwordTextbox;

    @FindBy(xpath = "//input[@value='Login']")
    private WebElement loginButton;

    @FindBy(css = "div.alert.alert-danger.alert-dismissible")
    private WebElement warningMessage;

    // ===============================
    // ACTION METHODS (with logging)
    // ===============================

    public void setUserEmail(String email) {
        logger.info("Entering email");
        wait.until(ExpectedConditions.visibilityOf(emailTextbox));
        emailTextbox.clear();
        emailTextbox.sendKeys(email);
    }

    public void setUserPassword(String pwd) {
        logger.info("Entering password");
        wait.until(ExpectedConditions.visibilityOf(passwordTextbox));
        passwordTextbox.clear();
        passwordTextbox.sendKeys(pwd);
    }

    public void clickOnLogin() {
        logger.info("Clicking Login button");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    // ===============================
    // VALIDATION METHODS
    // ===============================

    public String getWarningMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(warningMessage));
            return warningMessage.getText().trim();
        } catch (Exception e) {
            logger.warn("Warning message not displayed");
            return "";
        }
    }

    public boolean isLoginErrorDisplayed() {
        return !getWarningMessage().isEmpty();
    }
}
