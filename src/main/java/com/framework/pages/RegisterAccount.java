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
	WebElement fnametxtbox;

	@FindBy(id = "input-lastname")
	WebElement lnametxtbox;

	@FindBy(id = "input-email")
	WebElement emailtxtbox;

	@FindBy(id = "input-telephone")
	WebElement phonetxtbox;

	@FindBy(id = "input-password")
	WebElement pwdtxtBox;

	@FindBy(id = "input-confirm")
	WebElement confirmPwdtxtBox;

	@FindBy(xpath = "//label[@class='radio-inline']//input")
	List<WebElement> subscribeRadioBtns;

	@FindBy(name = "agree")
	WebElement policyChckbox;

	@FindBy(xpath = "//input[@value='Continue']")
	WebElement ContinueBtn;

	@FindBy(xpath = "//div[@id='content']/h1")
	WebElement Confirmationtxt;

	// -------------------- Action Methods --------------------

	/**
	 * Enters the first name in the registration form.
	 */
	public void setFistName(String fname) {
		logger.info("Entering First Name: {}", fname);
		wait.until(ExpectedConditions.visibilityOf(fnametxtbox));
		fnametxtbox.clear();
		fnametxtbox.sendKeys(fname);
	}

	/**
	 * Enters the last name in the registration form.
	 */
	public void setLastName(String lname) {
		logger.info("Entering Last Name: {}", lname);
		wait.until(ExpectedConditions.visibilityOf(lnametxtbox));
		lnametxtbox.clear();
		lnametxtbox.sendKeys(lname);
	}

	/**
	 * Enters the user's email address.
	 */
	public void setEmail(String email) {
		logger.info("Entering Email: {}", email);
		wait.until(ExpectedConditions.visibilityOf(emailtxtbox));
		emailtxtbox.clear();
		emailtxtbox.sendKeys(email);
	}

	/**
	 * Enters the phone number.
	 */
	public void setPhoneNumber(String ph) {
		logger.info("Entering Phone Number: {}", ph);
		wait.until(ExpectedConditions.visibilityOf(phonetxtbox));
		phonetxtbox.clear();
		phonetxtbox.sendKeys(ph);
	}

	/**
	 * Enters the password.
	 */
	public void setPassword(String pwd) {
		logger.info("Entering Password");
		wait.until(ExpectedConditions.visibilityOf(pwdtxtBox));
		pwdtxtBox.clear();
		pwdtxtBox.sendKeys(pwd);
	}

	/**
	 * Confirms the password.
	 */
	public void confirmPassword(String pwd) {
		logger.info("Entering Confirm Password");
		wait.until(ExpectedConditions.visibilityOf(confirmPwdtxtBox));
		confirmPwdtxtBox.clear();
		confirmPwdtxtBox.sendKeys(pwd);
	}

	/**
	 * Selects Yes/No for newsletter subscription.
	 */
	public void selectNewsletterOption(String value) {
		logger.info("Selecting Newsletter Option: {}", value.equals("1") ? "Yes" : "No");

		for (WebElement radio : subscribeRadioBtns) {
			if (radio.getAttribute("value").equals(value)) {
				wait.until(ExpectedConditions.elementToBeClickable(radio));
				radio.click();
				logger.debug("Newsletter radio button clicked with value: {}", value);
				break;
			}
		}
	}

	/**
	 * Accepts the privacy policy.
	 */
	public void acceptPolicy() {
		logger.info("Accepting Privacy Policy");
		wait.until(ExpectedConditions.elementToBeClickable(policyChckbox));
		policyChckbox.click();
	}

	/**
	 * Clicks the Continue button to submit the registration form.
	 */
	public void clickOnContinue() {
		logger.info("Clicking on Continue button");
		wait.until(ExpectedConditions.elementToBeClickable(ContinueBtn));

		// Scroll into view for headless stability
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ContinueBtn);

		ContinueBtn.click();
		logger.info("Continue button clicked");
	}

	/**
	 * Fetches the confirmation message after successful registration.
	 * 
	 * @return Confirmation text
	 */
	public String getConfirmationMsg() {
		logger.info("Fetching confirmation message");

		try {
			wait.until(ExpectedConditions.visibilityOf(Confirmationtxt));
			String message = Confirmationtxt.getText();
			logger.info("Registration success message: {}", message);
			return message;
		} catch (Exception e) {
			logger.error("Unable to get confirmation message: {}", e.getMessage());
			return e.getMessage();
		}
	}
	
	
}
