package com.google.code.yourpresenter.media;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.vfs2.FileSelectInfo;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.YpException;

@Service
public class MediaFileSelectorImpl implements IMediaFileSelector {

	private Set<String> acceptedExts;
	
	public void setAcceptedExts(String[] acceptedExts) throws YpException {
		this.acceptedExts = new HashSet<String>(Arrays.asList(acceptedExts));
	}
	
	@Override
	public boolean includeFile(FileSelectInfo fsInfo) throws Exception {
		return acceptedExts.contains(fsInfo.getFile().getName().getExtension().toLowerCase());
	}

	@Override
	public boolean traverseDescendents(FileSelectInfo arg0) throws Exception {
		return true;
	}
}
