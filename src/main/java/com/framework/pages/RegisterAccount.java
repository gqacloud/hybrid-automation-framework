package com.framework.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * RegisterAccount
 * ---------------
 * Page Object representing Register Account page.
 * Uses centralized actions from BasePage.
 */
public class RegisterAccount extends BasePage {

    private static final Logger logger = LogManager.getLogger(RegisterAccount.class);

    // ❌ No WebDriver constructor
    // ❌ No waits / JS / ExpectedConditions
    // ✅ BasePage constructor is used automatically

    // -------------------- Web Elements --------------------

    @FindBy(id = "input-firstname")
    private WebElement firstNameTextbox;

    @FindBy(id = "input-lastname")
    private WebElement lastNameTextbox;

    @FindBy(id = "input-email")
    private WebElement emailTextbox;

    @FindBy(id = "input-telephone")
    private WebElement phoneTextbox;

    @FindBy(id = "input-password")
    private WebElement passwordTextbox;

    @FindBy(id = "input-confirm")
    private WebElement confirmPasswordTextbox;

    @FindBy(xpath = "//label[@class='radio-inline']//input")
    private List<WebElement> newsletterOptions;

    @FindBy(name = "agree")
    private WebElement policyCheckbox;

    @FindBy(xpath = "//input[@value='Continue']")
    private WebElement continueButton;

    @FindBy(xpath="//div[@id='content']/h1")
    private WebElement confirmationMsg;
    
    @FindBy(css = "div.alert.alert-danger")
    private WebElement policyWarningMessage;
    
    @FindBy(xpath = "//div[contains(@class,'text-danger')]")
    private List<WebElement> fieldLevelWarnings;
    
    @FindBy(css = "div.alert.alert-danger")
    private WebElement commonWarningMessage;

    // -------------------- Action Methods --------------------

    public void setFirstName(String fname) {
        logger.info("Entering First Name: {}", fname);
        type(firstNameTextbox, fname);
    }

    public void setLastName(String lname) {
        logger.info("Entering Last Name: {}", lname);
        type(lastNameTextbox, lname);
    }

    public void setEmail(String email) {
        logger.info("Entering Email: {}", email);
        type(emailTextbox, email);
    }

    public void setPhoneNumber(String phone) {
        logger.info("Entering Phone Number: {}", phone);
        type(phoneTextbox, phone);
    }

    public void setPassword(String password) {
        logger.info("Entering Password");
        type(passwordTextbox, password);
    }

    public void confirmPassword(String password) {
        logger.info("Entering Confirm Password");
        type(confirmPasswordTextbox, password);
    }

    public void selectNewsletterOption(String value) {
        logger.info("Selecting Newsletter Option: {}", value.equals("1") ? "Yes" : "No");

        for (WebElement option : newsletterOptions) {
            if (value.equals(option.getAttribute("value"))) {
                click(option);
                return;
            }
        }

        throw new RuntimeException("Newsletter option not found: " + value);
    }

    public void acceptPolicy() {
        logger.info("Accepting Privacy Policy");
        click(policyCheckbox);
    }

    public void clickOnContinue() {
        logger.info("Clicking Continue button");
        click(continueButton);
    }

    public String getConfirmationMsg() {
        return getText(confirmationMsg);
    }
    
    public boolean isPolicyWarningDisplayed() {
        try {
            logger.info("Checking policy warning message");
            return commonWarningMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isDuplicateEmailWarningDisplayed() {
        try {
            logger.info("Checking duplicate email warning message");
            return commonWarningMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isPasswordMismatchWarningDisplayed() {
        logger.info("Checking password mismatch warning");
        return !fieldLevelWarnings.isEmpty();
    }

    public boolean isMandatoryFieldWarningDisplayed() {
        logger.info("Checking mandatory field warnings");
        return !fieldLevelWarnings.isEmpty();
    }

    // -------------------- Business Flow (Optional but Recommended) --------------------

    public void registerAccount(
            String fname,
            String lname,
            String email,
            String phone,
            String password,
            String newsletterValue) {

        setFirstName(fname);
        setLastName(lname);
        setEmail(email);
        setPhoneNumber(phone);
        setPassword(password);
        confirmPassword(password);
        selectNewsletterOption(newsletterValue);
        acceptPolicy();
        clickOnContinue();
    }
}
