package com.google.code.yourpresenter;

@SuppressWarnings("serial")
public class YpException extends Exception {

	private YpError error;

	public YpException(YpError error) {
		this.error = error;
	}
}
