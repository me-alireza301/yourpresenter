package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeletePresentationDialog extends AbstractActionDialog {

	@FindBy(id = "dialogPresentationDeleteForm:buttonDelete")
	private WebElement deleteButton;

	public DeletePresentationDialog(WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getActionButton() {
		return deleteButton;
	}
}
