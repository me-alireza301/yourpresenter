package com.google.code.yourpresenter.util;

public class Logger {

	private final org.slf4j.Logger sl4jLogger;
	
	public Logger(@SuppressWarnings("rawtypes") Class clazz) {
		this.sl4jLogger = org.slf4j.LoggerFactory.getLogger(clazz);
	}
	
	public void trace(String...strings) {
		if (this.sl4jLogger.isTraceEnabled()) {
			this.sl4jLogger.trace(concat(strings));
		}
	}
	
	public void debug(String...strings) {
		if (this.sl4jLogger.isDebugEnabled()) {
			this.sl4jLogger.debug(concat(strings));
		}
	}
	
	public void debug(Object...strings) {
		if (this.sl4jLogger.isDebugEnabled()) {
			this.sl4jLogger.debug(concat(strings));
		}
	}

	public void info(String...strings) {
		if (this.sl4jLogger.isInfoEnabled()) {
			this.sl4jLogger.info(concat(strings));
		}
	}
	
	public void warn(String...strings) {
		if (this.sl4jLogger.isWarnEnabled()) {
			this.sl4jLogger.warn(concat(strings));
		}
	}
	
	public void error(String...strings) {
		if (this.sl4jLogger.isErrorEnabled()) {
			this.sl4jLogger.error(concat(strings));
		}
	}
	
	public void error(Object...strings) {
		if (this.sl4jLogger.isErrorEnabled()) {
			this.sl4jLogger.error(concat(strings));
		}
	}
	
//	private static String concat(String... strings) {
//		StringBuilder sb = new StringBuilder();
//		for (String string : strings) {
//			sb.append(string);
//		}
//		return sb.toString();
//	}
	
	private String concat(Object[] strings) {
		StringBuilder sb = new StringBuilder();
		for (Object string : strings) {
			sb.append(string);
		}
		return sb.toString();
	}
}
