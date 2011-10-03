package com.google.code.yourpresenter.media;

import org.apache.commons.vfs2.FileSelector;

import com.google.code.yourpresenter.YpException;

public interface IMediaFileSelector extends FileSelector {

	public void setAcceptedExts(String[] acceptedExts) throws YpException;
}
