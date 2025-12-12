package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseClass;
import com.framework.driver.DriverManager;
import com.framework.pages.HomePage;
import com.framework.pages.RegisterAccount;
import com.framework.utils.RandomDataUtils;

public class TC001_RegisterAccountTest extends BaseClass {

    @Test(groups = {"Regression", "Master"})
    public void verifyAccountRegistration() {

        HomePage homePage = new HomePage(DriverManager.getDriver());
        RegisterAccount accountPage;

        try {
            // Navigate to Register Page
            homePage.clickOnMyAccount();
            homePage.clickOnRegister();

            accountPage = new RegisterAccount(DriverManager.getDriver());

            // Test Data generation
            String firstName = RandomDataUtils.randomString().toUpperCase();
            String lastName  = RandomDataUtils.randomString().toUpperCase();
            String email     = RandomDataUtils.getRandomEmail();
            String phone     = RandomDataUtils.randomNumber();
            String password  = RandomDataUtils.getRandomAlphaNumeric(10);

            // Fill registration form
            accountPage.setFistName(firstName);
            accountPage.setLastName(lastName);
            accountPage.setEmail(email);
            accountPage.setPhoneNumber(phone);
            accountPage.setPassword(password);
            accountPage.confirmPassword(password);
            accountPage.acceptPolicy();
            accountPage.clickOnContinue();

            // Validate success message
            String confirmationText = accountPage.getConfirmationMsg();
            Assert.assertEquals(confirmationText, "Your Account Has Been Created!", 
                                "Account creation confirmation message mismatch.");

        } catch (Exception e) {
            // Let listener handle logging + screenshots
            Assert.fail("Test failed due to unexpected exception: " + e.getMessage());
        }
    }
}
