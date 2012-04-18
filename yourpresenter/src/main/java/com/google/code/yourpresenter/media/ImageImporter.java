package com.google.code.yourpresenter.media;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.MediaType;
import com.google.code.yourpresenter.util.FileUtil;

@SuppressWarnings("serial")
@Service
@Qualifier("imageImporter")
public class ImageImporter extends AbstractMediaImporter {

	private final Set<String> supportedExts = new HashSet<String>(
			Arrays.asList("jpg", "jpeg", "png", "bmp"));
	
	@Override
	public Set<String> getSupportedExts() {
		return supportedExts;
	}

	@Override
	public File[] importMedia(String media, File outDir) throws YpException {
//		File file = new File(media);
//		try {
//			FileUtils.moveFileToDirectory(file, outDir, true);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return new File[] { new File(media) };
	}

	@Override
	public String getMediaUploadDir() throws YpException {
		return FileUtil.replaceDirs(preferenceService.findStringById(IConstants.MEDIA_UPLOAD_DIR_IMG));
	}
	
	@Override
	public MediaType getMediaType() throws YpException {
		return mediaTypeService.findByName(IConstants.MEDIA_TYPE_IMG);
	}
}
