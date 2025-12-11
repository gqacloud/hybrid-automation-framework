package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.framework.components.TableComponent;

public class CartPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CartPage.class);

    public CartPage(WebDriver driver) {
        super(driver);
    }
    
    @FindBy(xpath="//span[contains(text(),'Shopping Cart')]")
    WebElement CartTopLnk;

    // Cart items table
    @FindBy(xpath = "(.//table[contains(@class,'table')])[3]")
    WebElement cartTable;

    // Price summary table
    @FindBy(xpath = "(.//table[contains(@class,'table')])[4]")
    WebElement priceInfoTable;

    // Coupon Elements
    @FindBy(xpath = "//a[normalize-space()='Use Coupon Code']")
    WebElement couponPanel;

    @FindBy(id = "input-coupon")
    WebElement couponTxtBox;

    @FindBy(id = "button-coupon")
    WebElement couponBtn;

    // Shipping estimation
    @FindBy(xpath = "//a[normalize-space()='Estimate Shipping & Taxes']")
    WebElement taxCalPanel;

    @FindBy(id = "input-country")
    WebElement countryDropDown;

    @FindBy(id = "input-zone")
    WebElement regionDropdown;

    @FindBy(id = "input-postcode")
    WebElement postCodeTxtBox;

    @FindBy(id = "button-quote")
    WebElement getQuoteBtn;

    @FindBy(id = "button-shipping")
    WebElement shippingAlertBtn;

    // Checkout button
    @FindBy(xpath = "//a[@class='btn btn-primary']")
    WebElement checkoutBtn;


    // ---------------- TABLE ACCESSORS ---------------- //

    public TableComponent getCartTable() {
        return new TableComponent(driver, cartTable);
    }

    public TableComponent getCartTotalTable() {
        return new TableComponent(driver, priceInfoTable);
    }


    // ---------------- ACTION METHODS ---------------- //
    
    public void clickOnCartHeader() {
    	wait.until(ExpectedConditions.elementToBeClickable(CartTopLnk)).click();
    	CartTopLnk.click();
    }

    public void applyOffer(String offerCode) {
        wait.until(ExpectedConditions.elementToBeClickable(couponPanel)).click();
        couponTxtBox.clear();
        couponTxtBox.sendKeys(offerCode);
        couponBtn.click();
    }

    public void getShippingTaxEstimation(String country, String region, String zipcode) {
        wait.until(ExpectedConditions.elementToBeClickable(taxCalPanel)).click();

        Select countryOptions = new Select(countryDropDown);
        countryOptions.selectByVisibleText(country);

        Select regionOptions = new Select(regionDropdown);
        regionOptions.selectByVisibleText(region);

        postCodeTxtBox.clear();
        postCodeTxtBox.sendKeys(zipcode);

        getQuoteBtn.click();
    }


    public void clickOnCheckout() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn)).click();
            logger.info("Clicked checkout button.");
        } catch (Exception e) {
            logger.error("Failed to click checkout button: {}", e.getMessage());
        }
    }
}
