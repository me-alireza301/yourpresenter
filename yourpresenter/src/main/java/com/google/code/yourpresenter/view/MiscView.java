package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.google.code.yourpresenter.entity.MediaMisc;
import com.google.code.yourpresenter.service.IMediaMiscService;

@Component("miscView")
@Scope(WebApplicationContext.SCOPE_SESSION)
@SuppressWarnings("serial")
public class MiscView implements Serializable {

	@Autowired
	IMediaMiscService mediaMiscService;
	
	public Collection<MediaMisc> getMediaMisc() {
		return this.mediaMiscService.findAll();
	}
}
