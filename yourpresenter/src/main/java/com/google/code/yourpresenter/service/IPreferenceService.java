package com.google.code.yourpresenter.service;

import java.util.Collection;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Preference;

public interface IPreferenceService {

	public String findStringById(String name) throws YpException;
	
	public String[] findStringArrayById(String name) throws YpException;

	public Collection<Preference> findAll() throws YpException;
}
