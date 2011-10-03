package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.service.IBgImageService;

@Component("imageView")
@Scope("session")
@SuppressWarnings("serial")
public class ImageView implements Serializable {

	@Autowired
	IBgImageService bgImageService;
	
	public Collection<BgImage> getBgImage() {
		return this.bgImageService.findAll();
	}
}
