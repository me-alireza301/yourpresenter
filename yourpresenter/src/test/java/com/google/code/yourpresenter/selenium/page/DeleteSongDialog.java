package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeleteSongDialog extends AbstractActionDialog {

	@FindBy(id = "dialogSongDeleteForm:buttonDelete")
	private WebElement deleteButton;

	public DeleteSongDialog(WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getActionButton() {
		return deleteButton;
	}
}
