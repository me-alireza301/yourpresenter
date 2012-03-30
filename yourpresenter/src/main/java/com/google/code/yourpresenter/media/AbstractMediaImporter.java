package com.google.code.yourpresenter.media;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.BgImageType;
import com.google.code.yourpresenter.entity.MediaMisc;
import com.google.code.yourpresenter.entity.MediaMiscImage;
import com.google.code.yourpresenter.service.IBgImageService;
import com.google.code.yourpresenter.service.IBgImageTypeService;
import com.google.code.yourpresenter.service.IPreferenceService;
import com.google.code.yourpresenter.util.FileUtil;

@SuppressWarnings("serial")
public abstract class AbstractMediaImporter implements IMediaImporter, Serializable {

	@Autowired
	protected IPreferenceService preferenceService;
	
	@Autowired
	protected IBgImageTypeService bgImageTypeService;

	@Autowired
	private IBgImageService bgImageService;

	@Override
	public boolean supportsMediaType(final String media) throws YpException {
		String ext = FileUtil.getExtension(media);
		if (StringUtils.isEmpty(ext)) {
			throw new YpException(YpError.FILE_IMPORT_FAILED, "File extension unavailable: " + media);
		}
		return getSupportedExts().contains(ext);
	}
	
	@Override
	public MediaMisc importMedia(MediaMisc mediaMisc, File outDir) throws YpException {
		outDir.mkdirs();
		
		File[] files = importMedia(mediaMisc.getOriginal(), outDir);

		BgImageType bgImageType = bgImageTypeService.findByName(IConstants.BG_IMAGE_TYPE_MISC);
		for (File file : files) {
			BgImage bgImage = new BgImage(file.getAbsolutePath(), file.lastModified(), true, bgImageType);
			bgImageService.persist(bgImage);
			// generate and store the thumbnail
			bgImageService.handleThumbnail(bgImage);
			
			MediaMiscImage mediaMiscImage = new MediaMiscImage(mediaMisc, bgImage);
			mediaMisc.addMediaMiscImage(mediaMiscImage);
		}
		return mediaMisc;
	}
	
	@Override
	public String getMediaUploadDir() throws YpException {
		return FileUtil.replaceDirs(preferenceService.findStringById(IConstants.MEDIA_UPLOAD_DIR_MISC));
	}

}
