package com.framework.pages;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

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

    @FindBy(xpath = "//ul[@class='list-unstyled']//h2")
    WebElement ProdPriceTxt;

    @FindBy(css = "div[id='content'] h1")
    WebElement ProdNameTxt;

    @FindBy(xpath = "//p[@class='intro']")
    WebElement ProdDescrptionTxt;

    @FindBy(xpath="//li[contains(text(),'Brand')]")
    WebElement BrandTxt;

    @FindBy(xpath="//li[contains(text(),'Product Code')]")
    WebElement ProdCodeTxt;

    @FindBy(xpath="//li[contains(text(),'Reward Points')]")
    WebElement rewardTxt;

    @FindBy(xpath="//li[contains(text(),'Availability')]")
    WebElement AvailabilityTxt;

    @FindBy(id = "button-cart")
    WebElement cartBtn;

    @FindBy(xpath = "//i[@class='fa fa-calendar']")
    WebElement datapickerIcon;

    @FindBy(css = "div[class='datepicker-days'] th[class='picker-switch']")
    WebElement DateSwtich;

    @FindBy(css = "div[class='datepicker-days'] th[class='next']")
    WebElement nextBtn;

    @FindBy(css = "div[class='datepicker-days'] th[class='prev']")
    WebElement prevBtn;

    @FindBy(xpath = "//div[@class='datepicker-days']//td[@class='day']")
    List<WebElement> dates;

    // ---------------- Getters ---------------- //

    public String getProductName() {
        wait.until(ExpectedConditions.visibilityOf(ProdNameTxt));
        return ProdNameTxt.getText().trim();
    }

    public String getBrand() {
        wait.until(ExpectedConditions.visibilityOf(BrandTxt));
        return BrandTxt.getText().replace("Brand: ", "").trim();
    }

    public String getProductCode() {
        wait.until(ExpectedConditions.visibilityOf(ProdCodeTxt));
        String[] ProdCode = ProdCodeTxt.getText().split(":");
        return ProdCode[1].trim();
    }

    public String getRewardpoints() {
        wait.until(ExpectedConditions.visibilityOf(rewardTxt));
        String[] rewardpoint = rewardTxt.getText().split(":");
        return rewardpoint[1].trim();
    }

    public String getAvailability() {
        wait.until(ExpectedConditions.visibilityOf(AvailabilityTxt));
        String[] availablity = AvailabilityTxt.getText().split(":");
        return availablity[1].trim();
    }

    public double getDisplayedPrice() {
        wait.until(ExpectedConditions.visibilityOf(ProdPriceTxt));
        String PrdPrice = ProdPriceTxt.getText();
        double prodPrice = Double.parseDouble(PrdPrice.replace("$", "").trim());
        return prodPrice;
    }

    public String getProductDescription() {
        wait.until(ExpectedConditions.visibilityOf(ProdDescrptionTxt));
        return ProdDescrptionTxt.getText().trim();
    }

    // ---------------- Action Methods ---------------- //

    public void addProductToCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cartBtn));
            cartBtn.click();
        } catch (Exception e) {
            logger.error("Failed to click cart button '{}': {}", cartBtn, e.getMessage());
        }
    }

    public void selectDeliveryDate(String month, String year, String date) {
        selectDateFromCalendar(month, year, date);
    }

    private void selectDateFromCalendar(String givenMonth, String givenYear, String givenDate) {
        wait.until(ExpectedConditions.elementToBeClickable(datapickerIcon)).click();

        LocalDate today = LocalDate.now();
        String currentMonth = today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int currentYear = today.getYear();

        while (true) {

            String displayed = DateSwtich.getText(); // Example: "December 2025"
            String[] parts = displayed.split(" ");

            String appMonth = parts[0].trim();
            int appYear = Integer.parseInt(parts[1].trim());
            int targetYear = Integer.parseInt(givenYear);

            // RULE 1: If calendar shows current month & current year
            if (appMonth.equalsIgnoreCase(currentMonth) && appYear == currentYear) {
               
                for (WebElement date : dates) {
                    if (date.getText().equals(givenDate)) {
                        date.click();
                        return;
                    }
                }
                throw new RuntimeException("Date not found in current month: " + givenDate);
            }

            // RULE 2: If calendar year < target year → click NEXT
            if (appYear < targetYear) {
                nextBtn.click();
                continue;
            }

            // RULE 3: If calendar year > target year → click PREV
            if (appYear > targetYear) {
                prevBtn.click();
                continue;
            }

            // ---- Now the YEAR matches, navigate MONTH----
            int appMonthNum = Month.valueOf(appMonth.toUpperCase()).getValue();
            int givenMonthNum = Month.valueOf(givenMonth.toUpperCase()).getValue();

            if (appMonthNum < givenMonthNum) {
                nextBtn.click();
            } else if (appMonthNum > givenMonthNum) {
                prevBtn.click();
            } else {
                // Month & Year match → Select date
                for (WebElement date : dates) {
                    if (date.getText().equals(givenDate)) {
                        date.click();
                        return;
                    }
                }
                throw new RuntimeException("Date not found: " + givenDate);
            }
        }
    }
}
