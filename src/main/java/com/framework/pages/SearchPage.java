package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class SearchPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(SearchPage.class);

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//div[@id='search']//input")
    private WebElement homeSearchBox;

    @FindBy(xpath = "//div[@id='search']//span")
    private WebElement searchButton;

    @FindBy(xpath = "//select[@name='category_id']")
    private WebElement categoryDropdown;

    @FindBy(xpath = "//label[normalize-space()='Search in product descriptions']")
    private WebElement searchInProductDescriptionCheckbox;

    @FindBy(id = "button-search")
    private WebElement innerSearchButton;

    @FindBy(xpath = "//div[@class='caption']//a")
    private WebElement productName;

    @FindBy(xpath = "//div[@class='image']//a")
    private WebElement productLinkImage;

    // ===================================
    // SEARCH ACTIONS
    // ===================================

    public void enterProductToSearch(String product) {
        try {
            logger.info("Entering product name: {}", product);
            wait.until(ExpectedConditions.visibilityOf(homeSearchBox));
            homeSearchBox.clear();
            homeSearchBox.sendKeys(product);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter product name: " + product, e);
        }
    }

    public void clickOnSearch() {
        try {
            logger.info("Clicking main Search button");
            wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click main Search button", e);
        }
    }

    public void selectProductCategory(String categoryName) {
        try {
            logger.info("Selecting product category: {}", categoryName);
            wait.until(ExpectedConditions.visibilityOf(categoryDropdown));
            categoryDropdown.click();
            Select categorySelect = new Select(categoryDropdown);
            categorySelect.selectByVisibleText(categoryName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to select category: " + categoryName, e);
        }
    }

    public void clickOnProductDescriptionCheckbox() {
        try {
            logger.info("Checking 'Search in product descriptions'");
            wait.until(ExpectedConditions.elementToBeClickable(searchInProductDescriptionCheckbox)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click 'Search in product descriptions' checkbox", e);
        }
    }

    public void clickOnInnerSearch() {
        try {
            logger.info("Clicking Inner Search button");
            wait.until(ExpectedConditions.elementToBeClickable(innerSearchButton)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Inner Search button", e);
        }
    }

    // ===================================
    // VALIDATION
    // ===================================

    public String confirmProdName() {
        try {
            wait.until(ExpectedConditions.visibilityOf(productName));
            String name = productName.getText().trim();
            logger.info("Product found: {}", name);
            return name;
        } catch (Exception e) {
            throw new RuntimeException("Unable to locate product name", e);
        }
    }

    public void clickOnProduct() {
        try {
            logger.info("Clicking on product image/link");
            wait.until(ExpectedConditions.elementToBeClickable(productLinkImage)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click product link", e);
        }
    }
}
