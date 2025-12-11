package com.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage{

	public CheckoutPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	@FindBy(xpath="//input[@id='input-payment-lastname']") WebElement lnameTxtBox;
	@FindBy(xpath="//input[@id='input-payment-firstname']") WebElement FnameTxtBox;
	@FindBy(xpath="//input[@id='input-payment-address-1']") WebElement Add1TxtBox;
	@FindBy(xpath="//input[@id='input-payment-address-2']") WebElement Add2TxtBox;
	@FindBy(xpath="//input[@id='input-payment-city']") WebElement cityTxtBox;
	@FindBy(xpath="//input[@id='input-payment-postcode']") WebElement zipcodeTxtBox;
	@FindBy(xpath="//select[@id='input-payment-country']") WebElement countryDrop;
	@FindBy(xpath="//select[@id='input-payment-zone']") WebElement zoneDrop;
	 
	

	
	
	
	
	
	
}
