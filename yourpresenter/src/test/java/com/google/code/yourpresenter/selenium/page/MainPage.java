package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.google.code.yourpresenter.selenium.ITConstant;

public class MainPage {

//	private WebDriver driver;

	@FindBy(xpath = "//input[contains(@id, ':presenter')]")
	private WebElement presenterButton;
	
	@FindBy(xpath = "//span[@class='rf-msgs-sum']")
	private WebElement errorsSum;

	@FindBy(xpath = "//input[contains(@id, ':scheduleNameInput')]")
	private WebElement scheduleNameInput;
	
	public MainPage(WebDriver driver) {
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver,
				ITConstant.DRIVER_WAIT);
		PageFactory.initElements(finder, this);
//		this.driver = driver;
	}

	public void clickPresenterButton() {
		presenterButton.click();
	}
	
	public String getErrorsSumText() {
		return errorsSum.getText();
	}

	public void setScheduleName(String scheduleName) {
		scheduleNameInput.clear();
		scheduleNameInput.sendKeys(scheduleName);
		scheduleNameInput.sendKeys(Keys.ESCAPE);
	}
}
