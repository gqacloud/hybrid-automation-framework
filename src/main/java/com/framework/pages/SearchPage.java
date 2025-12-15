package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * SearchPage
 * ----------
 * Page Object representing product search functionality.
 * Uses centralized actions from BasePage.
 */
public class SearchPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(SearchPage.class);

    // ❌ No WebDriver constructor
    // ❌ No waits / ExpectedConditions
    // ❌ No direct click/sendKeys
    // ✅ BasePage constructor is used automatically

    // ===================================
    // Web Elements
    // ===================================

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
        logger.info("Entering product name: {}", product);
        type(homeSearchBox, product);
    }

    public void clickOnSearch() {
        logger.info("Clicking main Search button");
        click(searchButton);
    }

    public void selectProductCategory(String categoryName) {
        logger.info("Selecting product category: {}", categoryName);
        Select select = new Select(categoryDropdown);
        select.selectByVisibleText(categoryName);
    }

    public void clickOnProductDescriptionCheckbox() {
        logger.info("Clicking 'Search in product descriptions' checkbox");
        click(searchInProductDescriptionCheckbox);
    }

    public void clickOnInnerSearch() {
        logger.info("Clicking Inner Search button");
        click(innerSearchButton);
    }

    // ===================================
    // VALIDATION / NAVIGATION
    // ===================================

    public String confirmProdName() {
        logger.info("Fetching product name from search result");
        return getText(productName);
    }

    public void clickOnProduct() {
        logger.info("Clicking on product image/link");
        click(productLinkImage);
    }

    // ===================================
    // BUSINESS FLOW (OPTIONAL BUT CLEAN)
    // ===================================

    public void searchProduct(
            String product,
            String categoryName,
            boolean searchInDescription) {

        enterProductToSearch(product);
        clickOnSearch();

        if (categoryName != null && !categoryName.isBlank()) {
            selectProductCategory(categoryName);
        }

        if (searchInDescription) {
            clickOnProductDescriptionCheckbox();
        }

        clickOnInnerSearch();
    }
}
