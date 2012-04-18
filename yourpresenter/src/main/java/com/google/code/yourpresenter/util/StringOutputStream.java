package com.google.code.yourpresenter.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * String backed-up OutputStream. To get the output string value call toString()
 * method.
 * 
 * @author Peter Butkovic
 * 
 */
public class StringOutputStream extends OutputStream {

	private StringBuilder string = new StringBuilder();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		this.string.append((char) b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.string.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] abyte0) throws IOException {
		this.string.append(new String(abyte0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] abyte0, int i, int j) throws IOException {
		this.string.append(new String(Arrays.copyOfRange(abyte0, i, j)));
	}
}
