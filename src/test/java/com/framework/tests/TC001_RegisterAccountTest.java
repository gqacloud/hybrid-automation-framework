package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseClass;
import com.framework.driver.DriverManager;
import com.framework.pages.HomePage;
import com.framework.pages.RegisterAccount;
import com.framework.utils.RandomDataUtils;

public class TC001_RegisterAccountTest extends BaseClass {

	@Test(groups={"Regression", "Master"})
	public void verifyAccountRegisteration() throws InterruptedException {
		logger.info("===== Starting Register Account Test =====");
		try {
			
			HomePage homePage = new HomePage(DriverManager.getDriver());
			logger.info("Navigating to My Account");
			homePage.clickOnMyAccount();

			logger.info("Clicking on Register");
			homePage.clickOnRegister();

			RegisterAccount account = new RegisterAccount(DriverManager.getDriver());

			String firstName = RandomDataUtils.randomString().toUpperCase();
			logger.info("Entering First Name: {}", firstName);
			account.setFistName(firstName);

			String lastName = RandomDataUtils.randomString().toUpperCase();
			logger.info("Entering Last Name: {}", lastName);
			account.setLastName(lastName);

			String email = RandomDataUtils.getRandomEmail();
			logger.info("Entering Email: {}", email);
			account.setEmail(email);

			String phone = RandomDataUtils.randomNumber();
			logger.info("Entering Phone: {}", phone);
			account.setPhoneNumber(phone);

			String pwd = RandomDataUtils.getRandomAlphaNumeric(10);
			logger.debug("Generated Password: {}", pwd);
			account.setPassword(pwd);
			account.confirmPassword(pwd);

			logger.info("Accepting privacy policy");
			account.acceptPolicy();

			logger.info("Clicking on Continue button");
			account.clickOnContinue();

			Thread.sleep(3);

			String confirmMsg = account.getConfirmationMsg();
			logger.info("Confirmation Message Received: {}", confirmMsg);

			try {
				Assert.assertEquals(confirmMsg, "Your Account Has Been Created!");
				logger.info("Account creation test PASSED");
			} catch (AssertionError e) {
				logger.error("Assertion Failed: Expected message not found");
				logger.error("Actual Message: {}", confirmMsg);
				logger.error("Assertion Error Details: {}", e.getMessage());
				throw e; // rethrow so the test is marked as failed
			}

		} catch (Exception e) {

			logger.error("Unexpected Exception Occurred During Test Execution", e);
			logger.error("Test FAILED due to an unhandled exception.");

			Assert.fail("Test failed due to unexpected exception: " + e.getMessage());

		} finally {
			logger.info("===== Register Account Test Completed =====");
		}

	}
}
