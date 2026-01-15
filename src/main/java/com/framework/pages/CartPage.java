package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.framework.components.TableComponent;

/**
 * CartPage
 * --------
 * Page Object representing Shopping Cart page.
 * Uses centralized actions from BasePage.
 */
public class CartPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CartPage.class);

    // ❌ No WebDriver constructor
    // ❌ No waits / ExpectedConditions
    // ❌ No direct click/sendKeys
    // ✅ BasePage constructor is used automatically

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
        logger.info("Clicking Cart header");
        click(cartHeader);
    }

    public void applyOffer(String offerCode) {
        logger.info("Applying coupon code: {}", offerCode);

        click(couponPanel);
        type(couponTextBox, offerCode);
        click(applyCouponButton);
    }

    public void getShippingTaxEstimation(String country, String region, String zipcode) {
        logger.info(
            "Estimating shipping: Country={}, Region={}, Zip={}",
            country, region, zipcode
        );

        click(taxPanel);

        new Select(countryDropdown).selectByVisibleText(country);
        new Select(regionDropdown).selectByVisibleText(region);

        type(zipCodeTextBox, zipcode);
        click(getQuoteButton);
    }

    public void clickOnCheckout() {
        logger.info("Clicking Checkout button");
        click(checkoutButton);
    }
    
    public boolean isProductPresentInCart(String productName) {
        logger.info("Checking if product [{}] is present in cart", productName);

        TableComponent table = getCartTable();
        int rowCount = table.getRowCount();

        for (int row = 1; row <= rowCount; row++) {
            String nameInCart = table.getCellValue(row, 2); // column 2 = Product Name
            if (nameInCart.equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }
    
    public String getProductQuantity(String productName) {
        logger.info("Fetching quantity for product [{}]", productName);

        TableComponent table = getCartTable();
        int rowCount = table.getRowCount();

        for (int row = 1; row <= rowCount; row++) {
            if (table.getCellValue(row, 2).equalsIgnoreCase(productName)) {
                return table.getCellValue(row, 4); // column 4 = Quantity
            }
        }
        throw new RuntimeException("Product not found in cart: " + productName);
    }
    
    public String getProductTotalPrice(String productName) {
        logger.info("Fetching total price for product [{}]", productName);

        TableComponent table = getCartTable();
        int rowCount = table.getRowCount();

        for (int row = 1; row <= rowCount; row++) {
            if (table.getCellValue(row, 2).equalsIgnoreCase(productName)) {
                return table.getCellValue(row, 6); // column 6 = Total
            }
        }
        throw new RuntimeException("Product not found in cart: " + productName);
    }
}
