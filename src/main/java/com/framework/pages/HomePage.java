package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    

    @FindBy(xpath = "//ul[@class='list-inline']//li[@class='dropdown']")
    private WebElement myAccountLink;

    @FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-right']//a[normalize-space()='Register']")
    private WebElement registerLink;

    @FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-right']//a[text()='Login']")
    private WebElement loginLink;

    public void clickOnMyAccount() {
        logger.info("Clicking My Account");
        click(myAccountLink);
    }

    public void clickOnRegister() {
        logger.info("Clicking Register");
        click(registerLink);
    }

    public void clickOnLogin() {
        logger.info("Clicking Login");
        click(loginLink);
    }
}
