package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.code.yourpresenter.selenium.ITConstant;

public class DeleteSongDialog {

	private WebDriver driver;
	
	@FindBy(id = "dialogSongDeleteForm:buttonDelete")
	private WebElement deleteButton;
	
	public DeleteSongDialog(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver,
				ITConstant.DRIVER_WAIT);
		PageFactory.initElements(finder, this);
	}

	public void waitDialogNotDisplayed() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT, ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return !deleteButton.isDisplayed();
					}
				});
	}

	public void waitDialogDisplayed() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT, ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return deleteButton.isDisplayed();
					}
				});
	}

	public void clickDeleteButton() {
		deleteButton.click();
	}

}
