package com.framework.pages;

import java.time.Month;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ProductDetailsPage
 * ------------------
 * Page Object representing Product Details page.
 * Uses centralized actions from BasePage.
 */
public class ProductDetailsPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(ProductDetailsPage.class);

    // ❌ No WebDriver constructor
    // ❌ No waits / ExpectedConditions
    // ❌ No direct click/sendKeys
    // ✅ BasePage constructor is used automatically

    // ---------------- ELEMENTS ---------------- //

    @FindBy(xpath = "//ul[@class='list-unstyled']//h2")
    private WebElement productPriceText;

    @FindBy(css = "div[id='content'] h1")
    private WebElement productNameText;

    @FindBy(xpath = "//p[@class='intro']")
    private WebElement productDescriptionText;

    @FindBy(xpath = "//li[contains(text(),'Brand')]")
    private WebElement brandText;

    @FindBy(xpath = "//li[contains(text(),'Product Code')]")
    private WebElement productCodeText;

    @FindBy(xpath = "//li[contains(text(),'Reward Points')]")
    private WebElement rewardPointsText;

    @FindBy(xpath = "//li[contains(text(),'Availability')]")
    private WebElement availabilityText;

    @FindBy(id = "button-cart")
    private WebElement addToCartButton;

    // Calendar elements
    @FindBy(xpath = "//i[@class='fa fa-calendar']")
    private WebElement datePickerIcon;

    @FindBy(css = "div.datepicker-days th.picker-switch")
    private WebElement dateSwitch;

    @FindBy(css = "div.datepicker-days th.next")
    private WebElement nextButton;

    @FindBy(css = "div.datepicker-days th.prev")
    private WebElement prevButton;

    @FindBy(xpath = "//div[@class='datepicker-days']//td[@class='day']")
    private List<WebElement> dayCells;

    // ---------------- GETTERS ---------------- //

    public String getProductName() {
        logger.info("Fetching product name");
        return getText(productNameText);
    }

    public String getBrand() {
        logger.info("Fetching brand name");
        return getText(brandText).replace("Brand:", "").trim();
    }

    public String getProductCode() {
        logger.info("Fetching product code");
        return getText(productCodeText).split(":")[1].trim();
    }

    public String getRewardPoints() {
        logger.info("Fetching reward points");
        return getText(rewardPointsText).split(":")[1].trim();
    }

    public String getAvailability() {
        logger.info("Fetching availability");
        return getText(availabilityText).split(":")[1].trim();
    }

    public double getDisplayedPrice() {
        logger.info("Fetching product price");
        String price = getText(productPriceText).replace("$", "").trim();
        return Double.parseDouble(price);
    }

    public String getProductDescription() {
        logger.info("Fetching product description");
        return getText(productDescriptionText);
    }

    // ---------------- ACTION METHODS ---------------- //

    public void addProductToCart() {
        logger.info("Clicking Add to Cart");
        click(addToCartButton);
    }

    public void selectDeliveryDate(String month, String year, String day) {
        logger.info("Selecting delivery date: {} {} {}", month, year, day);
        openDatePicker();
        navigateToMonthYear(month, year);
        selectDay(day);
    }

    // ---------------- PRIVATE HELPERS ---------------- //

    private void openDatePicker() {
        click(datePickerIcon);
    }

    private void navigateToMonthYear(String targetMonth, String targetYear) {

        int targetMonthNum = Month.valueOf(targetMonth.toUpperCase()).getValue();
        int targetYearNum = Integer.parseInt(targetYear);

        while (true) {
            String displayed = getText(dateSwitch); // e.g. "December 2025"
            String[] parts = displayed.split(" ");

            int appMonthNum = Month.valueOf(parts[0].toUpperCase()).getValue();
            int appYearNum = Integer.parseInt(parts[1]);

            if (appYearNum < targetYearNum) {
                click(nextButton);
                continue;
            }
            if (appYearNum > targetYearNum) {
                click(prevButton);
                continue;
            }
            if (appMonthNum < targetMonthNum) {
                click(nextButton);
                continue;
            }
            if (appMonthNum > targetMonthNum) {
                click(prevButton);
                continue;
            }
            break;
        }
    }

    private void selectDay(String day) {
        for (WebElement d : dayCells) {
            if (d.getText().equals(day)) {
                click(d);
                return;
            }
        }
        throw new RuntimeException("Day not found in calendar: " + day);
    }
}
