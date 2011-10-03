package com.google.code.yourpresenter.media;

import java.io.File;

import com.google.code.yourpresenter.YpException;


public interface IThumbnailCreator {

	public void setThumbnailWidth(int thumbWidth);
	
	public void generateThumbnail(String image, File thumbnail) throws YpException;
}
