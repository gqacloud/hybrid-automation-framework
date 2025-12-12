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

    // ---------------- ELEMENTS ---------------- //

    @FindBy(xpath = "//span[contains(text(),'Shopping Cart')]")
    private WebElement cartHeader;

    @FindBy(xpath = "(.//table[contains(@class,'table')])[3]")
    private WebElement cartTable;

    @FindBy(xpath = "(.//table[contains(@class,'table')])[4]")
    private WebElement priceInfoTable;

    @FindBy(xpath = "//a[normalize-space()='Use Coupon Code']")
    private WebElement couponPanel;

    @FindBy(id = "input-coupon")
    private WebElement couponTextBox;

    @FindBy(id = "button-coupon")
    private WebElement applyCouponButton;

    @FindBy(xpath = "//a[normalize-space()='Estimate Shipping & Taxes']")
    private WebElement taxPanel;

    @FindBy(id = "input-country")
    private WebElement countryDropdown;

    @FindBy(id = "input-zone")
    private WebElement regionDropdown;

    @FindBy(id = "input-postcode")
    private WebElement zipCodeTextBox;

    @FindBy(id = "button-quote")
    private WebElement getQuoteButton;

    @FindBy(id = "button-shipping")
    private WebElement confirmShippingButton;

    @FindBy(xpath = "//a[@class='btn btn-primary']")
    private WebElement checkoutButton;


    // ---------------- TABLE ACCESSORS ---------------- //

    public TableComponent getCartTable() {
        logger.info("Accessing cart items table");
        return new TableComponent(driver, cartTable);
    }

    public TableComponent getCartTotalTable() {
        logger.info("Accessing cart total summary table");
        return new TableComponent(driver, priceInfoTable);
    }


    // ---------------- ACTION METHODS ---------------- //

    public void clickOnCartHeader() {
        try {
            logger.info("Clicking Cart header at the top navigation");
            wait.until(ExpectedConditions.elementToBeClickable(cartHeader)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Cart header", e);
        }
    }

    public void applyOffer(String offerCode) {
        try {
            logger.info("Applying coupon code: {}", offerCode);

            wait.until(ExpectedConditions.elementToBeClickable(couponPanel)).click();
            wait.until(ExpectedConditions.visibilityOf(couponTextBox)).clear();
            couponTextBox.sendKeys(offerCode);

            applyCouponButton.click();

        } catch (Exception e) {
            throw new RuntimeException("Failed to apply coupon code: " + offerCode, e);
        }
    }

    public void getShippingTaxEstimation(String country, String region, String zipcode) {
        try {
            logger.info("Estimating shipping: Country={}, Region={}, Zip={}", country, region, zipcode);

            wait.until(ExpectedConditions.elementToBeClickable(taxPanel)).click();

            new Select(countryDropdown).selectByVisibleText(country);
            new Select(regionDropdown).selectByVisibleText(region);

            zipCodeTextBox.clear();
            zipCodeTextBox.sendKeys(zipcode);

            getQuoteButton.click();

        } catch (Exception e) {
            throw new RuntimeException("Failed to estimate shipping for: " + zipcode, e);
        }
    }

    public void clickOnCheckout() {
        try {
            logger.info("Clicking on Checkout button");
            wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Checkout button", e);
        }
    }
}
