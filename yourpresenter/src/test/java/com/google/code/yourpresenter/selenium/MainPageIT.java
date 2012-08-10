package com.google.code.yourpresenter.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("./../test-application-config.xml")
public class MainPageIT extends AbstractIT {

	@Test
	public void testMainErrScheduleCreate() throws Exception {
		driver.get(ITConstant.MAIN_URL);

		mainPage.choosePresenterRole();
		mainPage.waitOkButtonReady();
		mainPage.clickOkButton();
		// check error message displayed
		Assert.assertEquals("size must be between 2 and 30",
				mainPage.getErrorMsgText());
	}
	
	@After
	public void after() {
	}
}
