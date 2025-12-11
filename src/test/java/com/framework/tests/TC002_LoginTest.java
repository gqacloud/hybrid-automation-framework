package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseClass;
import com.framework.driver.DriverManager;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.MyAccountPage;

public class TC002_LoginTest extends BaseClass {
	@Test(groups={"Sanity","Master", "Regression"})
	public void verifyLogin() {
		logger.info("===== Starting Login Test =====");

		try {
			HomePage homepage = new HomePage(DriverManager.getDriver());
			logger.info("Navigating to My Account");
			homepage.clickOnMyAccount();

			logger.info("Clicking on login");
			homepage.clickOnLogin();

			LoginPage loginpage = new LoginPage(DriverManager.getDriver());
			logger.info("Entering UserEmail: {}", prop.getProperty("email"));
			loginpage.setUserEmail(prop.getProperty("email"));

			logger.info("Entering UserEmail: {}", prop.getProperty("password"));
			loginpage.setUserPassword(prop.getProperty("password"));

			logger.info("Clicking on login button");
			loginpage.clickOnLogin();

			MyAccountPage accountpage = new MyAccountPage(DriverManager.getDriver());
			logger.info("Checking if target page found!");
			boolean targetPage = accountpage.isMyAccountMsg();
			Assert.assertEquals(targetPage, true);
		} catch (Exception e) {
			logger.error("Unexpected Exception Occurred During Test Execution", e);
			Assert.fail("Test failed due to unexpected exception: " + e.getMessage());
		} finally {
			logger.info("===== Login Test Completed =====");
		}
	}
}
