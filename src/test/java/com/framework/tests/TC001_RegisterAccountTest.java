package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.pages.HomePage;
import com.framework.pages.RegisterAccount;
import com.framework.utils.helpers.RandomDataUtils;

public class TC001_RegisterAccountTest extends BaseTest {

    @Test(groups = {"Regression", "Master"})
    public void verifyAccountRegistration() {

        // Page objects (no driver passed)
        HomePage homePage = new HomePage();
        RegisterAccount accountPage = new RegisterAccount();

        // Navigate to Register Page
        homePage.clickOnMyAccount();
        homePage.clickOnRegister();

        // Test Data generation
        String firstName = RandomDataUtils.randomString().toUpperCase();
        String lastName  = RandomDataUtils.randomString().toUpperCase();
        String email     = RandomDataUtils.getRandomEmail();
        String phone     = RandomDataUtils.randomNumber();
        String password  = RandomDataUtils.getRandomAlphaNumeric(10);

        // Fill registration form
        accountPage.setFirstName(firstName);
        accountPage.setLastName(lastName);
        accountPage.setEmail(email);
        accountPage.setPhoneNumber(phone);
        accountPage.setPassword(password);
        accountPage.confirmPassword(password);
        accountPage.acceptPolicy();
        accountPage.clickOnContinue();

        // Validate success message
        String confirmationText = accountPage.getConfirmationMsg();
        Assert.assertEquals(
                confirmationText,
                "Your Account Has Been Created!",
                "Account creation confirmation message mismatch."
        );
    }
}
