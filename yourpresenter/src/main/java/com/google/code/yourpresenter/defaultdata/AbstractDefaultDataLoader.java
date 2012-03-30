package com.google.code.yourpresenter.defaultdata;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.util.PostInitialize;

public abstract class AbstractDefaultDataLoader implements IDefaultDataLoader {

	@Transactional
	@PostInitialize(order = IConstants.POST_INIT_IDX_DEFAULT_DATA_LOAD)
	public void load() throws YpException {
		// check if properties are already loaded
		if (isAlreadyLoaded()) {
			return;
		}

		// Read properties file.
		Properties prefsDefault = new Properties();
		try {
			prefsDefault.load(getClass().getResourceAsStream(
					"/" + getPropertiesFile()));
		} catch (IOException e) {
			throw new YpException(YpError.DEFAULT_DATA_LOADING_FAILURE, e);
		}

		// store default vals in DB
		for (final Object key : prefsDefault.keySet()) {
			final String value = (String) prefsDefault
					.getProperty((String) key);
			if (!StringUtils.isEmpty(value)) {
				persist((String) key, value);
			}
		}
	}

}
