package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AddChangeSongDialog extends AbstractActionDialog {

	@FindBy(id = "dialogSongEditForm:buttonOK")
	private WebElement okButton;

	@FindBy(id = "dialogSongEditForm:buttonCancel")
	private WebElement cancelButton;

	@FindBy(xpath = "//span[@class='rf-msgs-sum']")
	private WebElement errorsSum;

	@FindBy(id = "dialogSongEditForm:dialogSongEditName")
	private WebElement songNameInput;

	@FindBy(id = "dialogSongEditForm:dialogSongText")
	private WebElement songTextInput;

	public AddChangeSongDialog(WebDriver driver) {
		super(driver);
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

	public String getErrorsSumText() {
		return errorsSum.getText();
	}

	public void setSongName(String songName) {
		songNameInput.clear();
		songNameInput.sendKeys(songName);
	}

	public void setSongText(String songText) {
		songTextInput.clear();
		songTextInput.sendKeys(songText);
	}

	@Override
	protected WebElement getActionButton() {
		return okButton;
	}
}
