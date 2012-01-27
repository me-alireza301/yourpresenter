package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.google.code.yourpresenter.selenium.ITConstant;

public class AdminPage {

	@FindBy(xpath = "//input[contains(@id, ':mediaDirs')]")
	private WebElement mediaDirsInput;
	
	@FindBy(xpath = "//input[contains(@id, ':saveButton')]")
	private WebElement saveButton;
	
	public AdminPage(WebDriver driver) {
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver,
				ITConstant.DRIVER_WAIT);
		PageFactory.initElements(finder, this);
	}
	
	public void setMediaDirs(String dirs) {
		mediaDirsInput.sendKeys(dirs);
	}
	
	public void clickSaveButton() {
		saveButton.click();
	}
}
