package com.google.code.yourpresenter.service;

import java.util.Collection;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Media;

public interface IBgImageService {

	public Collection<BgImage> findAllByType(String type);
	
	public Collection<BgImage> findAll();
	
	public BgImage findById(Long id);
	
	public void persist(BgImage bgImage);
	
	public BgImage handleThumbnail(BgImage bgImage) throws YpException;

	public Collection<BgImage> findFirstBgImageByType(String type);
}
