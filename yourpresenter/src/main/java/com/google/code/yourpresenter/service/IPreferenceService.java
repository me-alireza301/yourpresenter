package com.google.code.yourpresenter.service;

import java.util.Collection;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Preference;
import com.google.code.yourpresenter.util.IPreferenceChangedListener;

public interface IPreferenceService {

	public String findStringById(String name) throws YpException;

	public String[] findStringArrayById(String name) throws YpException;

	public Collection<Preference> findAll() throws YpException;

	public void update(Collection<Preference> preferences) throws YpException;

	public void registerPreferenceChangedListener(
			IPreferenceChangedListener listener, String... preferences)
			throws YpException;

	public void unregisterPreferenceChangedListener(
			IPreferenceChangedListener listener) throws YpException;
}
