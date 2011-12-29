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

public class AddChangeSongDialog {

	private WebDriver driver;

	@FindBy(id = "dialogSongEditForm:buttonOK")
	private WebElement okButton;

	public static final String cancelButtonId = "dialogSongEditForm:buttonCancel";
	
	@FindBy(id = cancelButtonId)
	private WebElement cancelButton;

	@FindBy(xpath = "//span[@class='rf-msgs-sum']")
	private WebElement errorsSum;

	@FindBy(id = "dialogSongEditForm:dialogSongEditName")
	private WebElement songNameInput;

	@FindBy(id = "dialogSongEditForm:dialogSongText")
	private WebElement songTextInput;

	public AddChangeSongDialog(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver,
				ITConstant.DRIVER_WAIT);
		PageFactory.initElements(finder, this);
	}

	public boolean isDisplayed() {
		return cancelButton.isDisplayed();
	}

	public void clickCancelButton() {
		cancelButton.click();
	}

	public void clickOkButton() {
		okButton.click();
	}

	public void waitDialogNotDisplayed() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT, ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return !cancelButton.isDisplayed();
					}
				});
	}

	public void waitDialogDisplayed() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT, ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return cancelButton.isDisplayed();
					}
				});
	}

	public String getErrorsSumText() {
		return errorsSum.getText();
	}

	public void setSongName(String songName) {
		songNameInput.sendKeys(songName);
	}

	public void setSongText(String songText) {
		songTextInput.sendKeys(songText);
	}
}
