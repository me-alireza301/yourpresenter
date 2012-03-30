package com.google.code.yourpresenter.defaultdata;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Preference;
import com.google.code.yourpresenter.service.IPreferenceService;

@SuppressWarnings("serial")
@Service
@Repository
public class DefaultDataPreferenceLoader extends AbstractDefaultDataLoader implements Serializable {

	@Autowired
	private IPreferenceService preferenceService;
	
	@Override
	public boolean isAlreadyLoaded() {
		try {
			preferenceService.findStringById(IConstants.MEDIA_THUMBNAIL_DIR);
		} catch (YpException e) {
			return false;
		}
		return true;
	}

	@Override
	public String getPropertiesFile() {
		return "default_data_preference.properties";
	}

	@Override
	public void persist(String key, String value) {
		preferenceService.persist(new Preference((String) key, value));
	}

}
