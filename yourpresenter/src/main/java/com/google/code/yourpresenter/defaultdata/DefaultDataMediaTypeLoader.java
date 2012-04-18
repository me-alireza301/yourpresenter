package com.google.code.yourpresenter.defaultdata;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.entity.MediaType;
import com.google.code.yourpresenter.service.IMediaTypeService;

@SuppressWarnings("serial")
@Service
@Repository
public class DefaultDataMediaTypeLoader extends AbstractDefaultDataLoader implements Serializable {

	@Autowired
	private IMediaTypeService mediaTypeService;
	
	@Override
	public boolean isAlreadyLoaded() {
		return (0 < mediaTypeService.findAll().size());
	}

	@Override
	public String getPropertiesFile() {
		return "default_data_mediatype.properties";
	}

	@Override
	public void persist(String key, String value) {
		mediaTypeService.persist(new MediaType(Long.parseLong(key), value));
	}
}
