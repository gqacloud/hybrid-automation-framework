package com.framework.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterAccount extends BasePage {

    private static final Logger logger = LogManager.getLogger(RegisterAccount.class);

    public RegisterAccount(WebDriver driver) {
        super(driver);
    }

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

    @FindBy(xpath = "//div[@id='content']/h1")
    private WebElement confirmationText;

    // -------------------- Action Methods --------------------

    public void setFistName(String fname) {
        try {
            logger.info("Entering First Name: {}", fname);
            wait.until(ExpectedConditions.visibilityOf(firstNameTextbox));
            firstNameTextbox.clear();
            firstNameTextbox.sendKeys(fname);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter First Name", e);
        }
    }

    public void setLastName(String lname) {
        try {
            logger.info("Entering Last Name: {}", lname);
            wait.until(ExpectedConditions.visibilityOf(lastNameTextbox));
            lastNameTextbox.clear();
            lastNameTextbox.sendKeys(lname);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter Last Name", e);
        }
    }

    public void setEmail(String email) {
        try {
            logger.info("Entering Email: {}", email);
            wait.until(ExpectedConditions.visibilityOf(emailTextbox));
            emailTextbox.clear();
            emailTextbox.sendKeys(email);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter Email", e);
        }
    }

    public void setPhoneNumber(String phone) {
        try {
            logger.info("Entering Phone Number: {}", phone);
            wait.until(ExpectedConditions.visibilityOf(phoneTextbox));
            phoneTextbox.clear();
            phoneTextbox.sendKeys(phone);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter Phone Number", e);
        }
    }

    public void setPassword(String password) {
        try {
            logger.info("Entering Password");
            wait.until(ExpectedConditions.visibilityOf(passwordTextbox));
            passwordTextbox.clear();
            passwordTextbox.sendKeys(password);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter Password", e);
        }
    }

    public void confirmPassword(String password) {
        try {
            logger.info("Entering Confirm Password");
            wait.until(ExpectedConditions.visibilityOf(confirmPasswordTextbox));
            confirmPasswordTextbox.clear();
            confirmPasswordTextbox.sendKeys(password);
        } catch (Exception e) {
            throw new RuntimeException("Failed to confirm Password", e);
        }
    }

    public void selectNewsletterOption(String value) {
        try {
            logger.info("Selecting Newsletter Option: {}", value.equals("1") ? "Yes" : "No");

            for (WebElement option : newsletterOptions) {
                if (option.getAttribute("value").equals(value)) {
                    wait.until(ExpectedConditions.elementToBeClickable(option));
                    option.click();
                    return;
                }
            }

            throw new RuntimeException("Newsletter option not found: " + value);

        } catch (Exception e) {
            throw new RuntimeException("Failed to select Newsletter option", e);
        }
    }

    public void acceptPolicy() {
        try {
            logger.info("Accepting Privacy Policy");
            wait.until(ExpectedConditions.elementToBeClickable(policyCheckbox)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to accept Privacy Policy", e);
        }
    }

    public void clickOnContinue() {
        try {
            logger.info("Clicking Continue button");

            wait.until(ExpectedConditions.elementToBeClickable(continueButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", continueButton);

            continueButton.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Continue button", e);
        }
    }

    public String getConfirmationMsg() {
        try {
            logger.info("Fetching confirmation message");
            wait.until(ExpectedConditions.visibilityOf(confirmationText));

            String msg = confirmationText.getText().trim();
            logger.info("Confirmation message received: {}", msg);

            return msg;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch confirmation message", e);
        }
    }
}
