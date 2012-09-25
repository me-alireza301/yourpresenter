package com.google.code.yourpresenter.service;

import java.util.Collection;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;

public interface IBgImageService {

	public BgImage findById(Long id);
	
	public BgImage generateThumbnail(BgImage bgImage) throws YpException;

	public Collection<BgImage> findFirstBgImageByType(String type);
}
