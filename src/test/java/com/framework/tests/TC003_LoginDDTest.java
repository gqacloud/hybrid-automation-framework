package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseClass;
import com.framework.driver.DriverManager;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.MyAccountPage;
import com.framework.utils.DataProviders;

public class TC003_LoginDDTest extends BaseClass {

	// to mention data provider created in different package we should mention 2
	// parameters data provider name and the class
	@Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class, groups={"DataDriven","Master",})
	public void verify_loginDDTest(String email, String pwd, String expResult) {
		logger.info("===== Starting Data Driven Login Test =====");

		try {
			HomePage homepage = new HomePage(DriverManager.getDriver());
			logger.info("Navigating to My Account...");
			homepage.clickOnMyAccount();

			logger.info("Clicking on Login...");
			homepage.clickOnLogin();

			LoginPage loginpage = new LoginPage(DriverManager.getDriver());

			logger.info("Entering User Email: {}", email);
			loginpage.setUserEmail(email);

			logger.info("Entering User Password: {}", pwd);
			loginpage.setUserPassword(pwd);

			logger.info("Clicking on Login button...");
			loginpage.clickOnLogin();

			MyAccountPage accountpage = new MyAccountPage(DriverManager.getDriver());
			logger.info("Validating if MyAccount page is displayed...");
			boolean targetPage = accountpage.isMyAccountMsg();

			logger.info("Target page status: {}", targetPage);

			if (expResult.equalsIgnoreCase("Valid")) {
				logger.info("Expected result: VALID login");

				if (targetPage) {
					logger.info("Login successful with valid credentials.");
					accountpage.clickOnLogout();
					Assert.assertTrue(true);
					
				} else {
					logger.warn("Login failed even though credentials are valid.");
					Assert.assertTrue(false);
				}
			}

			if (expResult.equalsIgnoreCase("Invalid")) {
				logger.info("Expected result: INVALID login");

				if (targetPage) {
					logger.warn("Login succeeded even though credentials are invalid.");
					accountpage.clickOnLogout();
					Assert.assertTrue(false);
				} else {
					logger.info("Login failed as expected with invalid credentials.");
					logger.info(loginpage.warnMessage());
					Assert.assertTrue(true);
				}
			}

		} catch (Exception e) {
			logger.error("Unexpected Exception Occurred During Test Execution", e);
			Assert.fail("Test failed due to unexpected exception: " + e.getMessage());

		} finally {
			logger.info("===== Data Driven Login Test Completed =====");
		}

	}

}
