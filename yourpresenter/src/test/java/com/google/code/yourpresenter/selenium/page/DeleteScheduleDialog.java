package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeleteScheduleDialog extends AbstractActionDialog {

	@FindBy(id = "dialogScheduleDeleteForm:buttonDelete")
	private WebElement deleteButton;

	public DeleteScheduleDialog(WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getActionButton() {
		return deleteButton;
	}
}
