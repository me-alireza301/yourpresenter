package com.google.code.yourpresenter.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class FileUtil {

	public static final String USER_HOME = System.getProperty("user.home");
	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String JAVA_IO_TMPDIR = System
			.getProperty("java.io.tmpdir");

	/**
	 * Replaces system properties in files.
	 * <p>
	 * Following are replaced:
	 * <ul>
	 * <li>java.io.tmpdir
	 * <li>user.dir
	 * <li>user.home
	 * </ul>
	 * </p>
	 * 
	 * @param files
	 * @return
	 */
	public static String[] replaceDirs(String[] files) {
		if (null == files) {
			return files;
		}

		int size = files.length;
		for (int i = 0; i < size; i++) {
			files[i] = replaceDirs(files[i]);
		}
		return files;
	}

	/**
	 * Replaces system properties in file.
	 * <p>
	 * Following are replaced:
	 * <ul>
	 * <li>java.io.tmpdir
	 * <li>user.dir
	 * <li>user.home
	 * </ul>
	 * </p>
	 * 
	 * @param file
	 * @return
	 */
	public static String replaceDirs(String file) {
		if (StringUtils.isBlank(file)) {
			return file;
		}

		file = file.replace("${user.home}", USER_HOME);
		file = file.replace("${java.io.tmpdir}", JAVA_IO_TMPDIR);
		file = file.replace("${user.dir}", USER_DIR);
		return file;
	}

	/**
	 * Returns file extension or null if no extension exists or file is
	 * null.
	 * 
	 * @param file
	 * @return
	 */
	public static String getExtension(File file) {
		if (null == file) {
			return null;
		}
		return getExtension(file.getName());
	}
	
	/**
	 * Returns file extension or null if no extension exists or filename is
	 * empty.
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename) {
		if (StringUtils.isEmpty(filename)) {
			return null;
		}
		int dotIdx = filename.lastIndexOf(".");
		if (-1 == dotIdx) {
			return null;
		}
		return filename.substring(dotIdx + 1);
	}

}
