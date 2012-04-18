package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.service.IBgImageService;

@Component("imageView")
@Scope(WebApplicationContext.SCOPE_SESSION)
@SuppressWarnings("serial")
public class ImageView implements Serializable {

	@Autowired
	private IBgImageService bgImageService;
	
	public Collection<BgImage> getMisc() {
		return this.get(IConstants.MEDIA_TYPE_MISC);
	}
	
	public Collection<BgImage> getImage() {
		return this.get(IConstants.MEDIA_TYPE_IMG);
	}
	
	public Collection<BgImage> getVideo() {
		return this.get(IConstants.MEDIA_TYPE_VIDEO);
	}
	
	protected Collection<BgImage> get(String mediaType) {
		return this.bgImageService.findFirstBgImageByType(mediaType);
	}
}
