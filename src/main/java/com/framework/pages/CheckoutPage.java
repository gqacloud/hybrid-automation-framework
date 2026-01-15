package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * CheckoutPage
 * ------------
 * Page Object representing Checkout page.
 * Uses centralized actions from BasePage.
 */
public class CheckoutPage extends BasePage {

    // ❌ No WebDriver constructor
    // ❌ No waits / ExpectedConditions
    // ❌ No direct click/sendKeys
    // ✅ BasePage constructor is used automatically
	 private static final Logger logger = LogManager.getLogger(CheckoutPage.class);
    // -------------------- Web Elements --------------------

    @FindBy(id = "input-payment-firstname")
    private WebElement firstNameTxtBox;

    @FindBy(id = "input-payment-lastname")
    private WebElement lastNameTxtBox;

    @FindBy(id = "input-payment-address-1")
    private WebElement address1TxtBox;

    @FindBy(id = "input-payment-address-2")
    private WebElement address2TxtBox;

    @FindBy(id = "input-payment-city")
    private WebElement cityTxtBox;

    @FindBy(id = "input-payment-postcode")
    private WebElement zipCodeTxtBox;

    @FindBy(id = "input-payment-country")
    private WebElement countryDrop;

    @FindBy(id = "input-payment-zone")
    private WebElement zoneDrop;

    // -------------------- Action Methods --------------------

    public void enterFirstName(String fname) {
        type(firstNameTxtBox, fname);
    }

    public void enterLastName(String lname) {
        type(lastNameTxtBox, lname);
    }

    public void enterAddressLine1(String address1) {
        type(address1TxtBox, address1);
    }

    public void enterAddressLine2(String address2) {
        type(address2TxtBox, address2);
    }

    public void enterCity(String city) {
        type(cityTxtBox, city);
    }

    public void enterZipCode(String zip) {
        type(zipCodeTxtBox, zip);
    }

    public void selectCountry(String country) {
        new Select(countryDrop).selectByVisibleText(country);
    }

    public void selectZone(String zone) {
        new Select(zoneDrop).selectByVisibleText(zone);
    }

    // -------------------- Business Flow (Recommended) --------------------

    public void fillBillingDetails(
            String firstName,
            String lastName,
            String address1,
            String address2,
            String city,
            String zip,
            String country,
            String zone) {

        enterFirstName(firstName);
        enterLastName(lastName);
        enterAddressLine1(address1);
        enterAddressLine2(address2);
        enterCity(city);
        enterZipCode(zip);
        selectCountry(country);
        selectZone(zone);
    }
    
    public boolean isCheckoutPageDisplayed() {
        logger.info("Checking if Checkout page is displayed");
        return getCurrentUrl().contains("checkout");
    }
}
