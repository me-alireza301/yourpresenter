package com.google.code.yourpresenter.defaultdata;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.entity.BgImageType;
import com.google.code.yourpresenter.service.IBgImageTypeService;

@SuppressWarnings("serial")
@Service
@Repository
public class DefaultDataBgImageTypeLoader extends AbstractDefaultDataLoader implements Serializable {

	@Autowired
	private IBgImageTypeService bgImageTypeService;
	
	@Override
	public boolean isAlreadyLoaded() {
		return (0 < bgImageTypeService.findAll().size());
	}

	@Override
	public String getPropertiesFile() {
		return "default_data_bgimagetype.properties";
	}

	@Override
	public void persist(String key, String value) {
		bgImageTypeService.persist(new BgImageType(Long.parseLong(key), value));
	}
}
