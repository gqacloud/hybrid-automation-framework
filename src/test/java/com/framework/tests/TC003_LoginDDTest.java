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

    @Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class,
          groups = {"DataDriven", "Master"})
    public void verify_loginDDTest(String email, String pwd, String expResult) {

        logger.info("Data Set → email: {} | expected: {}", email, expResult);

        // Navigate to Login Page
        HomePage homePage = new HomePage(DriverManager.getDriver());
        homePage.clickOnMyAccount();
        homePage.clickOnLogin();

        // Login attempt
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.setUserEmail(email);
        loginPage.setUserPassword(pwd);
        loginPage.clickOnLogin();

        // Check login success
        MyAccountPage accountPage = new MyAccountPage(DriverManager.getDriver());
        boolean loginSuccess = accountPage.isMyAccountMsg();

        boolean expectedValid = expResult.equalsIgnoreCase("Valid");

        if (expectedValid) {
            // Expecting successful login
            Assert.assertTrue(loginSuccess, "Valid login failed for dataset: " + email);
            accountPage.clickOnLogout();
        } else {
            // Expecting login failure
            Assert.assertFalse(loginSuccess, "Invalid login succeeded for dataset: " + email);
        }
    }
}
