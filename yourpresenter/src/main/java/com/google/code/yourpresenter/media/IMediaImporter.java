package com.google.code.yourpresenter.media;

import java.io.File;
import java.util.Set;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Media;
import com.google.code.yourpresenter.entity.MediaType;

public interface IMediaImporter {

	public boolean supportsMediaType(final String media) throws YpException;

	public Set<String> getSupportedExts();
	
	public String getMediaUploadDir() throws YpException;

//	public Media importMedia(File file) throws YpException;

	public File[] importMedia(Media media) throws YpException;
	
	public File[] importMedia(final String media, File outDir) throws YpException;
	
	public MediaType getMediaType() throws YpException;

}
