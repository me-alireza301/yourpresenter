package com.google.code.yourpresenter.service;

import java.util.List;

import com.google.code.yourpresenter.entity.BgImageType;

public interface IBgImageTypeService {

	public List<BgImageType> findAll();
	
	public BgImageType findByName(String name);
	
	public void persist(BgImageType bgImageType);
}
