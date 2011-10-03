package com.google.code.yourpresenter.service;

import com.google.code.yourpresenter.YpException;

public interface IPreferenceService {

	public String findStringById(String name) throws YpException;
	
	public String[] findStringArrayById(String name) throws YpException;
}
