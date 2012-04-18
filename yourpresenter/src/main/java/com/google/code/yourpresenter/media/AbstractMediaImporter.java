package com.google.code.yourpresenter.media;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Media;
import com.google.code.yourpresenter.service.IMediaTypeService;
import com.google.code.yourpresenter.service.IPreferenceService;
import com.google.code.yourpresenter.util.FileUtil;

@SuppressWarnings("serial")
public abstract class AbstractMediaImporter implements IMediaImporter,
		Serializable {

	@Autowired
	protected IPreferenceService preferenceService;

	@Autowired
	protected IMediaTypeService mediaTypeService;

	@Override
	public boolean supportsMediaType(final String media) throws YpException {
		String ext = FileUtil.getExtension(media);
		if (StringUtils.isEmpty(ext)) {
			throw new YpException(YpError.FILE_IMPORT_FAILED,
					"File extension unavailable: " + media);
		}
		return getSupportedExts().contains(ext);
	}

	@Override
	public File[] importMedia(Media media) throws YpException {
		File outDir = new File(media.getOriginal()).getParentFile();
		outDir.mkdirs();
		return importMedia(media.getOriginal(), outDir);
	}

}
