package com.google.code.yourpresenter;


@SuppressWarnings("serial")
public class YpException extends Exception {

	private YpError error;

	public YpException(YpError error) {
		this.error = error;
	}
	
	public YpException(YpError error, String str) {
		super(str);
		this.error = error;
	}

	public YpException(YpError error, Exception e) {
		super(e);
		this.error = error;
	}

	public YpError getError() {
		return error;
	}

	public void setError(YpError error) {
		this.error = error;
	}
}
