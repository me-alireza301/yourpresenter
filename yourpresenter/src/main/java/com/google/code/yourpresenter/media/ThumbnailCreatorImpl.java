package com.google.code.yourpresenter.media;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import net.coobird.thumbnailator.Thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;

@SuppressWarnings("serial")
@Service
public class ThumbnailCreatorImpl implements IThumbnailCreator, Serializable {

	private static Logger log = LoggerFactory.getLogger(ThumbnailCreatorImpl.class);
	
	private int thumbnailWidth;
	
	public ThumbnailCreatorImpl() {
	}
	
	public void generateThumbnail(String image, File thumbnail) throws YpException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("STARTED extracting thumbnail: " + thumbnail);
			}
			Thumbnails.of(image).width(this.thumbnailWidth).keepAspectRatio(true).toFile(thumbnail);
			if (log.isDebugEnabled()) {
				log.debug("DONE extracting thumbnail");
			}
		} catch (IOException e) {
			throw new YpException(YpError.THUMBNAIL_CREATION_FAILED, e);
		}	
	}

	@Override
	public void setThumbnailWidth(int thumbWidth) {
		this.thumbnailWidth = thumbWidth;
	}


}
