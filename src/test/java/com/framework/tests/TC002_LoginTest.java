package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseClass;
import com.framework.driver.DriverManager;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.MyAccountPage;

public class TC002_LoginTest extends BaseClass {

    @Test(groups = { "Sanity", "Master", "Regression" })
    public void verifyLogin() throws InterruptedException {

        // Navigate to Login Page
        HomePage homePage = new HomePage(DriverManager.getDriver());
        homePage.clickOnMyAccount();
        homePage.clickOnLogin();

        // Enter Login Credentials
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.setUserEmail(prop.getProperty("email"));
        loginPage.setUserPassword(prop.getProperty("password"));
        loginPage.clickOnLogin();

        // Verification
        MyAccountPage accountPage = new MyAccountPage(DriverManager.getDriver());
        Assert.assertTrue(
                accountPage.isMyAccountMsg(),
                "Login failed: My Account page is not displayed."
        );
    }
}
