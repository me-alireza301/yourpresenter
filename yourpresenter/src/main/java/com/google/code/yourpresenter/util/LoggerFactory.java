package com.google.code.yourpresenter.util;

public class LoggerFactory {

	public static Logger getLogger(Class clazz) {
		return new Logger(clazz); 
	}
}
