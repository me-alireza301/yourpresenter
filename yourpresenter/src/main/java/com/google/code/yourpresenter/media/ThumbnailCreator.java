package com.google.code.yourpresenter.media;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.util.FileUtil;

public class ThumbnailCreator {

	private static Logger log = LoggerFactory.getLogger(ThumbnailCreator.class);

	public static void generateThumbnail(String image, File thumbnail,
			int thumbnailWidth) throws YpException {
		String ext = FileUtil.getExtension(thumbnail);
		try {
			log.debug("STARTED extracting thumbnail: ", thumbnail);
			Thumbnails.of(image).width(thumbnailWidth).outputFormat(ext)
					.keepAspectRatio(true).toFile(thumbnail);
			log.debug("DONE extracting thumbnail: ", thumbnail);
		} catch (IOException e) {
			throw new YpException(YpError.THUMBNAIL_CREATION_FAILED, e);
		}
	}
}
