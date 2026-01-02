package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage
 * ---------
 * Page Object representing Login page.
 * Uses BasePage centralized actions.
 */
public class LoginPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    // ❌ No WebDriver constructor
    // ❌ No super(driver)
    // ✅ BasePage constructor is used automatically

    @FindBy(id = "input-email")
    private WebElement emailTextbox;

    @FindBy(id = "input-password")
    private WebElement passwordTextbox;

    @FindBy(xpath = "//input[@value='Login']")
    private WebElement loginButton;

    @FindBy(css = "div.alert.alert-danger.alert-dismissible")
    private WebElement warningMessage;

    // ===============================
    // ACTION METHODS
    // ===============================

    public void setUserEmail(String email) {
        logger.info("Entering email");
        type(emailTextbox, email);
    }

    public void setUserPassword(String pwd) {
        logger.info("Entering password");
        type(passwordTextbox, pwd);
    }

    public void clickOnLogin() {
        logger.info("Clicking Login button");
        click(loginButton);
    }

    public void login(String email, String pwd) {
        setUserEmail(email);
        setUserPassword(pwd);
        clickOnLogin();
    }

    // ===============================
    // VALIDATION METHODS
    // ===============================

    public String getWarningMessage() {
        try {
            return getText(warningMessage);
        } catch (Exception e) {
            logger.warn("Warning message not displayed");
            return "";
        }
        
    }

    public boolean isLoginErrorDisplayed() {
        return !getWarningMessage().isEmpty();
    }
}
