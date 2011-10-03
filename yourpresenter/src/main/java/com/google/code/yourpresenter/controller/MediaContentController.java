package com.google.code.yourpresenter.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.service.IBgImageService;
import com.google.code.yourpresenter.service.IPreferenceService;

// for file download sample, see: http://stackoverflow.com/questions/5673260/downloading-a-file-from-spring-controllers
@Controller
public class MediaContentController {

	@Autowired
	IBgImageService bgImageService;
	@Autowired
	private IPreferenceService preferenceService;
	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		binder.setValidator(new Validator() {
//
//			@Override
//			public boolean supports(Class<?> clazz) {
//				return clazz.getEnclosingClass().equals(String.class);
//			}
//
//			@Override
//			public void validate(Object param, Errors errors) {
//				if (StringUtils.isEmpty((String) param)) {
//					errors.reject("id can't be empty");
//				}
//			}});
//	}

	@RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
	public void getFile(@PathVariable("id") String bgImageId,
			HttpServletResponse response) throws YpException {
		BgImage bgImage = this.bgImageService.findById(Long.valueOf(bgImageId));
		
		InputStream is = null;
		try {
			is = new FileInputStream(bgImage.getThumbnail());
			setHeader(response, bgImage);
			
			// copy it to response's OutputStream
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			throw new YpException(YpError.THUMBNAIL_READING_FAILED, e);
		} catch (IOException e) {
			throw new YpException(YpError.THUMBNAIL_READING_FAILED, e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					throw new YpException(YpError.THUMBNAIL_READING_FAILED, e);
				}
				is = null;
			}
		}
	}
	
	private void setHeader(HttpServletResponse response, BgImage bgImage)
			throws YpException {
		response.setContentType(this.preferenceService
				.findStringById(IConstants.MEDIA_THUMBNAIL_CONTENT_TYPE));
	}
}
