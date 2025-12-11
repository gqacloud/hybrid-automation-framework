package com.framework.pages;

import java.util.List;

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
    WebElement homeSearchBox;

    @FindBy(xpath = "//div[@id='search']//span")
    WebElement searchBtn;

    @FindBy(xpath = "//select[@name='category_id']")
    WebElement categoryDropdown;

    @FindBy(xpath = "//label[normalize-space()='Search in product descriptions']")
    WebElement searchInProdDescCheckbox;

    @FindBy(id = "button-search")
    WebElement innerSearchBtn;

    @FindBy(xpath = "//div[@class='caption']//a")
    WebElement productName;
    
    @FindBy(xpath="//div[@class='image']//a")
    WebElement ProdLinkImage;


    // ---------------- ACTION METHODS ---------------- //

    public void enterProductToSearch(String product) {
        try {
            wait.until(ExpectedConditions.visibilityOf(homeSearchBox));
            homeSearchBox.clear();
            homeSearchBox.sendKeys(product);
        } catch (Exception e) {
            logger.error("Failed to enter product name '{}': {}", product, e.getMessage());
        }
    }

    public void clickOnSearch() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
        } catch (Exception e) {
            logger.error("Failed to click Search button: {}", e.getMessage());
        }
    }

    public void selectProductCategory(String categoryName) {
        try {
            Select prodCategory = new Select(categoryDropdown);
            List<WebElement> options = prodCategory.getOptions();

            for (WebElement option : options) {
                if (option.getText().equalsIgnoreCase(categoryName)) {
                    prodCategory.selectByVisibleText(categoryName);
                    return;
                }
            }

            logger.error("Category '{}' not found in dropdown.", categoryName);
            throw new RuntimeException("Category not found: " + categoryName);

        } catch (Exception e) {
            logger.error("Failed to select product category '{}': {}", categoryName, e.getMessage());
            throw e;
        }
    }

    public void clickOnProductDescriptionCheckbox() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(searchInProdDescCheckbox)).click();
        } catch (Exception e) {
            logger.error("Failed to click product description checkbox: {}", e.getMessage());
        }
    }

    public void clickOnInnerSearch() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(innerSearchBtn)).click();
        } catch (Exception e) {
            logger.error("Failed to click inner Search button: {}", e.getMessage());
        }
    }

    public String confirmProdName() {
        try {
            wait.until(ExpectedConditions.visibilityOf(productName));
           
        } catch (Exception e) {
            logger.error("Unable to locate the product: {}", e.getMessage());
        }
		return  productName.getText();
    }
    
    public void clickOnProduct() {
    	try {
            wait.until(ExpectedConditions.elementToBeClickable(ProdLinkImage)).click();
        } catch (Exception e) {
            logger.error("Failed to click inner Search button: {}", e.getMessage());
        }
    }
    
    
}
