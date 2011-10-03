package com.google.code.yourpresenter.service;

import java.util.Collection;

import com.google.code.yourpresenter.entity.BgImage;

public interface IBgImageService {

	public Collection<BgImage> findAll();
	
	public BgImage findById(Long id);
	
	public void persist(BgImage bgImage);
}
