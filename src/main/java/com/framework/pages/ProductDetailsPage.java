package com.framework.pages;

import java.time.Month;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductDetailsPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(ProductDetailsPage.class);

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

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

    // ---------------- GETTERS WITH LOGGING ---------------- //

    public String getProductName() {
        try {
            logger.info("Fetching product name");
            wait.until(ExpectedConditions.visibilityOf(productNameText));
            return productNameText.getText().trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product name", e);
        }
    }

    public String getBrand() {
        try {
            logger.info("Fetching brand name");
            wait.until(ExpectedConditions.visibilityOf(brandText));
            return brandText.getText().replace("Brand: ", "").trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get brand", e);
        }
    }

    public String getProductCode() {
        try {
            logger.info("Fetching product code");
            wait.until(ExpectedConditions.visibilityOf(productCodeText));
            return productCodeText.getText().split(":")[1].trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product code", e);
        }
    }

    public String getRewardpoints() {
        try {
            logger.info("Fetching reward points");
            wait.until(ExpectedConditions.visibilityOf(rewardPointsText));
            return rewardPointsText.getText().split(":")[1].trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reward points", e);
        }
    }

    public String getAvailability() {
        try {
            logger.info("Fetching availability");
            wait.until(ExpectedConditions.visibilityOf(availabilityText));
            return availabilityText.getText().split(":")[1].trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get availability", e);
        }
    }

    public double getDisplayedPrice() {
        try {
            logger.info("Fetching product price");
            wait.until(ExpectedConditions.visibilityOf(productPriceText));
            String priceText = productPriceText.getText().replace("$", "").trim();
            return Double.parseDouble(priceText);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product price", e);
        }
    }

    public String getProductDescription() {
        try {
            logger.info("Fetching product description");
            wait.until(ExpectedConditions.visibilityOf(productDescriptionText));
            return productDescriptionText.getText().trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product description", e);
        }
    }

    // ---------------- ACTION METHODS ---------------- //

    public void addProductToCart() {
        try {
            logger.info("Clicking Add to Cart");
            wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Add to Cart button", e);
        }
    }

    public void selectDeliveryDate(String month, String year, String day) {
        try {
            logger.info("Selecting delivery date: {} {} {}", month, year, day);
            selectDateFromCalendar(month, year, day);
        } catch (Exception e) {
            throw new RuntimeException("Failed to select delivery date", e);
        }
    }

    private void selectDateFromCalendar(String givenMonth, String givenYear, String givenDay) {

        wait.until(ExpectedConditions.elementToBeClickable(datePickerIcon)).click();

        while (true) {

            String displayed = dateSwitch.getText().trim();  // Example: "December 2025"
            String[] parts = displayed.split(" ");

            String appMonth = parts[0];
            int appYear = Integer.parseInt(parts[1]);

            int targetYear = Integer.parseInt(givenYear);

            // YEAR navigation
            if (appYear < targetYear) {
                nextButton.click();
                continue;
            }
            if (appYear > targetYear) {
                prevButton.click();
                continue;
            }

            // MONTH navigation
            int appMonthNum = Month.valueOf(appMonth.toUpperCase()).getValue();
            int targetMonthNum = Month.valueOf(givenMonth.toUpperCase()).getValue();

            if (appMonthNum < targetMonthNum) {
                nextButton.click();
                continue;
            }
            if (appMonthNum > targetMonthNum) {
                prevButton.click();
                continue;
            }

            // Month & Year match → Select day
            for (WebElement d : dayCells) {
                if (d.getText().equals(givenDay)) {
                    d.click();
                    return;
                }
            }

            throw new RuntimeException("Day not found in calendar: " + givenDay);
        }
    }
}
