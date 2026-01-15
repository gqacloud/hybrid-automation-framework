package com.framework.tests.login;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.MyAccountPage;

public class LoginTests extends BaseTest {

	// ===============================
	// POSITIVE SCENARIOS
	// ===============================

	@Test(groups = { "Sanity", "Regression", "Login" })
	public void validLoginTest() {

		HomePage homePage = new HomePage();
		homePage.clickOnMyAccount();
		homePage.clickOnLogin();

		LoginPage loginPage = new LoginPage();
		loginPage.login(prop.getProperty("email"), prop.getProperty("password"));

		MyAccountPage accountPage = new MyAccountPage();
		Assert.assertTrue(accountPage.isMyAccountMsgDisplayed(), "Valid login failed");
		accountPage.clickOnLogout();
	}

	// ===============================
	// NEGATIVE SCENARIOS
	// ===============================

	@Test(groups = { "Regression", "Login" })
	public void invalidLoginTest() {

		HomePage homePage = new HomePage();
		homePage.clickOnMyAccount();
		homePage.clickOnLogin();

		LoginPage loginPage = new LoginPage();
		loginPage.login("invalid@test.com", "wrongpassword");

		Assert.assertTrue(loginPage.isLoginErrorDisplayed(), "Error message not shown for invalid login");
	}

	@Test(groups = { "Regression", "Login" })
	public void emptyCredentialsLoginTest() {

		HomePage homePage = new HomePage();
		homePage.clickOnMyAccount();
		homePage.clickOnLogin();

		LoginPage loginPage = new LoginPage();
		loginPage.login("", "");

		Assert.assertTrue(loginPage.isLoginErrorDisplayed(), "Error message not shown for empty credentials");
	}

	@Test(dataProvider = "LoginData", dataProviderClass = com.framework.utils.data.DataProviders.class, groups = {
			"Login", "DataDriven", "Regression", "Master" })
	public void loginWithMultipleCredentialsScenario(String email, String password, String expectedResult) {

		HomePage homePage = new HomePage();
		homePage.clickOnMyAccount();
		homePage.clickOnLogin();

		LoginPage loginPage = new LoginPage();
		loginPage.login(email, password);

		MyAccountPage accountPage = new MyAccountPage();
		boolean actualLoginSuccess = accountPage.isMyAccountMsgDisplayed();
		boolean expectedValid = expectedResult.equalsIgnoreCase("Valid");

		if (expectedValid) {
			Assert.assertTrue(actualLoginSuccess, "Expected VALID login but failed for user: " + email);
			accountPage.clickOnLogout();
		} else {
			Assert.assertFalse(actualLoginSuccess, "Expected INVALID login but succeeded for user: " + email);
		}
	}
}
