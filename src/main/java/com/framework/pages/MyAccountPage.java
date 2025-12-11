package com.framework.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class MyAccountPage extends BasePage {

	private static final Logger logger = LogManager.getLogger(MyAccountPage.class);

	public MyAccountPage(WebDriver driver) {
		super(driver);
	}

	// -------------------- Web Elements --------------------

	@FindBy(xpath = "//h2[normalize-space()='My Account']")
	WebElement MyAccountMsg;

	@FindBy(xpath = "//div[@class='list-group']//a[text()='Logout']")
	WebElement LogoutBtn;

	


	// -------------------- Action Methods --------------------

	/**
	 * Verifies that the "My Account" heading is displayed after login.
	 * 
	 * @return true if the heading is visible; false otherwise.
	 */
	public boolean isMyAccountMsg() {
		logger.info("Validating the presence of 'My Account' message.");
		try {
			wait.until(ExpectedConditions.visibilityOf(MyAccountMsg));
			boolean displayed = MyAccountMsg.isDisplayed();

			if (displayed) {
				logger.info("'My Account' message is displayed successfully.");
			} else {
				logger.warn("'My Account' message is NOT displayed.");
			}

			return displayed;

		} catch (Exception e) {
			logger.error("Exception occurred while checking 'My Account' message: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * Clicks on the Logout button in the account panel.
	 */
	public void clickOnLogout() {
		logger.info("Attempting to click on Logout button.");
		try {
			wait.until(ExpectedConditions.elementToBeClickable(LogoutBtn));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", LogoutBtn);

			LogoutBtn.click();
			logger.info("Logout button clicked successfully.");

		} catch (Exception e) {
			logger.error("Failed to click Logout button: {}", e.getMessage());
		}
	}

	

}
