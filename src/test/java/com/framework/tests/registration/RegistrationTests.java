package com.framework.tests.registration;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.pages.HomePage;
import com.framework.pages.RegisterAccount;
import com.framework.utils.helpers.RandomDataUtils;

public class RegistrationTests extends BaseTest {

    // ======================================================
    // SCENARIO 1: Valid Account Registration (Positive)
    // ======================================================
    @Test(groups = { "Regression", "Registration", "Master","CoreFlow" })
    public void validAccountRegistrationScenario() {

        HomePage homePage = new HomePage();
        RegisterAccount accountPage = new RegisterAccount();

        // Navigate
        homePage.clickOnMyAccount();
        homePage.clickOnRegister();

        // Test Data
        String firstName = RandomDataUtils.randomString().toUpperCase();
        String lastName  = RandomDataUtils.randomString().toUpperCase();
        String email     = RandomDataUtils.getRandomEmail();
        String phone     = RandomDataUtils.randomNumber();
        String password  = RandomDataUtils.getRandomAlphaNumeric(10);

        // Fill form
        accountPage.setFirstName(firstName);
        accountPage.setLastName(lastName);
        accountPage.setEmail(email);
        accountPage.setPhoneNumber(phone);
        accountPage.setPassword(password);
        accountPage.confirmPassword(password);
        accountPage.acceptPolicy();
        accountPage.clickOnContinue();

        // Validation
        Assert.assertEquals(
                accountPage.getConfirmationMsg(),
                "Your Account Has Been Created!",
                "Account creation confirmation message mismatch."
        );
    }

    // ======================================================
    // SCENARIO 2: Registration Without Accepting Policy
    // ======================================================
    @Test(groups = { "Regression", "Registration","CoreFlow" })
    public void registrationWithoutAcceptingPolicyScenario() {

        HomePage homePage = new HomePage();
        RegisterAccount accountPage = new RegisterAccount();

        // Navigate
        homePage.clickOnMyAccount();
        homePage.clickOnRegister();

        // Test Data
        String firstName = RandomDataUtils.randomString().toUpperCase();
        String lastName  = RandomDataUtils.randomString().toUpperCase();
        String email     = RandomDataUtils.getRandomEmail();
        String phone     = RandomDataUtils.randomNumber();
        String password  = RandomDataUtils.getRandomAlphaNumeric(10);

        // Fill form (policy NOT accepted)
        accountPage.setFirstName(firstName);
        accountPage.setLastName(lastName);
        accountPage.setEmail(email);
        accountPage.setPhoneNumber(phone);
        accountPage.setPassword(password);
        accountPage.confirmPassword(password);

        accountPage.clickOnContinue();

        // Validation
        Assert.assertTrue(
                accountPage.isPolicyWarningDisplayed(),
                "Registration should fail when Privacy Policy is not accepted."
        );
    }

    // ======================================================
    // SCENARIO 3: Registration With Existing Email
    // ======================================================
    @Test(groups = { "Regression", "Registration","CoreFlow" })
    public void registrationWithExistingEmailScenario() {

        HomePage homePage = new HomePage();
        RegisterAccount accountPage = new RegisterAccount();

        // Navigate
        homePage.clickOnMyAccount();
        homePage.clickOnRegister();

        // Existing email from config
        String firstName = RandomDataUtils.randomString().toUpperCase();
        String lastName  = RandomDataUtils.randomString().toUpperCase();
        String email     = prop.getProperty("email"); // already registered
        String phone     = RandomDataUtils.randomNumber();
        String password  = RandomDataUtils.getRandomAlphaNumeric(10);

        // Fill form
        accountPage.setFirstName(firstName);
        accountPage.setLastName(lastName);
        accountPage.setEmail(email);
        accountPage.setPhoneNumber(phone);
        accountPage.setPassword(password);
        accountPage.confirmPassword(password);
        accountPage.acceptPolicy();
        accountPage.clickOnContinue();

        // Validation
        Assert.assertTrue(
                accountPage.isDuplicateEmailWarningDisplayed(),
                "Registration should fail for an already registered email."
        );
    }

    // ======================================================
    // SCENARIO 4: Registration With Mismatched Passwords
    // ======================================================
    @Test(groups = { "Regression", "Registration","CoreFlow" })
    public void registrationWithMismatchedPasswordsScenario() {

        HomePage homePage = new HomePage();
        RegisterAccount accountPage = new RegisterAccount();

        // Navigate
        homePage.clickOnMyAccount();
        homePage.clickOnRegister();

        // Test Data
        String firstName = RandomDataUtils.randomString().toUpperCase();
        String lastName  = RandomDataUtils.randomString().toUpperCase();
        String email     = RandomDataUtils.getRandomEmail();
        String phone     = RandomDataUtils.randomNumber();

        // Fill form
        accountPage.setFirstName(firstName);
        accountPage.setLastName(lastName);
        accountPage.setEmail(email);
        accountPage.setPhoneNumber(phone);
        accountPage.setPassword("Password123");
        accountPage.confirmPassword("Password456");
        accountPage.acceptPolicy();
        accountPage.clickOnContinue();

        // Validation
        Assert.assertTrue(
                accountPage.isPasswordMismatchWarningDisplayed(),
                "Registration should fail when passwords do not match."
        );
    }

    // ======================================================
    // SCENARIO 5: Registration With Empty Mandatory Fields
    // ======================================================
    @Test(groups = { "Regression", "Registration","CoreFlow" })
    public void registrationWithoutMandatoryFieldsScenario() {

        HomePage homePage = new HomePage();
        RegisterAccount accountPage = new RegisterAccount();

        // Navigate
        homePage.clickOnMyAccount();
        homePage.clickOnRegister();

        // Do not fill any fields
        accountPage.clickOnContinue();

        // Validation
        Assert.assertTrue(
                accountPage.isMandatoryFieldWarningDisplayed(),
                "Registration should fail when mandatory fields are empty."
        );
    }
}
