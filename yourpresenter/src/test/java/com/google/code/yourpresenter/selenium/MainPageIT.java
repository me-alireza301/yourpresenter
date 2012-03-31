package com.google.code.yourpresenter.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.code.yourpresenter.selenium.page.MainPage;
import com.google.code.yourpresenter.selenium.restclient.ScheduleRestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("./../test-application-config.xml")
public class MainPageIT {

	private static WebDriver driver;

	private MainPage page;

	@Autowired
	private ScheduleRestTemplate scheduleRestTemplate;

	@BeforeClass
	public static void setUpAll() throws Exception {
//		DesiredCapabilities caps = DesiredCapabilities.firefox();
//		LoggingPreferences logs = new LoggingPreferences();
//		logs.enable(LogType.DRIVER, Level.ALL);
//		caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
//		driver = new FirefoxDriver(caps);

		// for drag and drop to be working on linux:
		// Enabling features that are disabled by default in Firefox
		// see: http://code.google.com/p/selenium/wiki/TipsAndTricks
		FirefoxProfile profile = new FirefoxProfile();
		profile.setEnableNativeEvents(true);
		driver = new FirefoxDriver(profile);

		driver.manage().timeouts()
				.implicitlyWait(ITConstant.DRIVER_WAIT, TimeUnit.SECONDS);
	}

	@Before
	public void setUp() throws Exception {
		page = new MainPage(driver);
	}

	@AfterClass
	public static void cleanUpAll() {
		driver.quit();
	}

	@After
	public void cleanUp() {
		scheduleRestTemplate.deleteAll();
	}

	@Test
	public void testMainErrScheduleCreate() throws Exception {
		driver.get("http://localhost:8081/yourpresenter");

		page.choosePresenterRole();
		page.clickOkButton();
		// check error message displayed
		Assert.assertEquals("size must be between 2 and 30",
				page.getErrorMsgText());
	}
}
