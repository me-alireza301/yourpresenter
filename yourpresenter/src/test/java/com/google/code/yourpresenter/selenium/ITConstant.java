package com.google.code.yourpresenter.selenium;

public interface ITConstant {

	/**
	 * Timeout for web request (in seconds).
	 */
	public static int DRIVER_WAIT = 30;

	/**
	 * Pool time for repeated checking (in miliseconds).
	 */
	public static int DRIVER_CYCLIC_SLEEP = 50;

	public static final String RESOURCE_PATH = "target/test-classes/com/google/code/yourpresenter/selenium/";
	public static final String MAIN_URL = "http://localhost:8081/yourpresenter/";
	public static final String PRESENTER_URL = MAIN_URL
			+ "presenter/presenter.jsf";
	public static final String ADMIN_URL = MAIN_URL + "admin/admin.jsf";
	
}
