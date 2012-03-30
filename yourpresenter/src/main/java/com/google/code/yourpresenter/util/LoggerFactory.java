package com.google.code.yourpresenter.util;

public class LoggerFactory {

	public static Logger getLogger(@SuppressWarnings("rawtypes") Class clazz) {
		return new Logger(clazz); 
	}
}
