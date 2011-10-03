package com.google.code.yourpresenter.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.vfs2.FileObject;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;

public class FileObjectUtil {

	public static String getAbsolutePath(final FileObject fileObject) throws YpException {
		// make sure that spaces are encoded
		URI uri;
		try {
			uri = new URI(fileObject.getName().getURI().toString()
					.replace(" ", "%20"));
		} catch (URISyntaxException e) {
			throw new YpException(YpError.FILEOBJECT_NAME_ERROR, fileObject.toString());
		}
		
		StringBuilder file = new StringBuilder();
		// on linux there is no authority
		if (null != uri.getAuthority()) {
			file.append(uri.getAuthority());
		}
		file.append(uri.getPath());
		return file.toString();
	}
}
