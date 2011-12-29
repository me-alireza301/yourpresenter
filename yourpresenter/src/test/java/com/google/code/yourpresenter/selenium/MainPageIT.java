package com.google.code.yourpresenter.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.code.yourpresenter.selenium.page.MainPage;
import com.google.code.yourpresenter.selenium.restclient.PresentationRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.ScheduleRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.SongRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.VerseRestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-application-config.xml")
public class MainPageIT {

	private WebDriver driver;
	
	private MainPage page;
	
	@Autowired
	private ScheduleRestTemplate scheduleRestTemplate;
	@Autowired
	private PresentationRestTemplate presenationRestTemplate;
	@Autowired
	private SongRestTemplate songRestTemplate;
	@Autowired
	private VerseRestTemplate verseRestTemplate;

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		page = PageFactory.initElements(driver, MainPage.class);
		driver.get("http://localhost:8081/yourpresenter/main.jsf");
	}

	@After
	public void cleanUp() {
		driver.close();
		
		scheduleRestTemplate.deleteAll();
		presenationRestTemplate.deleteAll();
		verseRestTemplate.deleteAll();
		songRestTemplate.deleteAll();
	}

	@Test
	public void testMainErrScheduleCreate() throws Exception {
		// click Presenter button
		page.clickPresenterButton();
		// check error message displayed
		Assert.assertEquals("size must be between 2 and 30", page.getErrorsSumText());
	}
}
