package com.google.code.yourpresenter.defaultdata;

import com.google.code.yourpresenter.YpException;

public interface IDefaultDataLoader {

	public void load() throws YpException;
	
	public boolean isAlreadyLoaded();
	
	public String getPropertiesFile();
	
	public void persist(String key, String value);
}
