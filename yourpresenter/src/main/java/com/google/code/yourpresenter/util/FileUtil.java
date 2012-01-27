package com.google.code.yourpresenter.util;

import org.apache.commons.lang3.StringUtils;

public class FileUtil {

	public static final String USER_HOME = System.getProperty("user.home");
	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String JAVA_IO_TMPDIR = System.getProperty("java.io.tmpdir");
	
	/**
	 * Replaces system properties in files.
	 * Following are replaced:
	 *  - java.io.tmpdir
	 *  - user.dir
	 *  - user.home
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
	 * Following are replaced:
	 *  - java.io.tmpdir
	 *  - user.dir
	 *  - user.home
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
	
}
