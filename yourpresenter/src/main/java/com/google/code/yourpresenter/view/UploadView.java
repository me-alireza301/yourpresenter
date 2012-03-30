package com.google.code.yourpresenter.view;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.exception.FileUploadException;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.media.IMediaImporter;
import com.google.code.yourpresenter.service.IMediaMiscService;
import com.google.code.yourpresenter.util.IMediaImportProgressListener;
import com.google.code.yourpresenter.util.Logger;
import com.google.code.yourpresenter.util.LoggerFactory;

@Component("uploadView")
@Scope(WebApplicationContext.SCOPE_SESSION)
@SuppressWarnings("serial")
public class UploadView implements IMediaImportProgressListener, Serializable {

	private static Logger logger = LoggerFactory.getLogger(UploadView.class);

	@Autowired
	private List<IMediaImporter> mediaImporters;

	private List<File> files = new ArrayList<File>();

	private String currentImportValue = "-1";

	@Autowired
	private IMediaMiscService mediaMiscService;

	public void upload(FileUploadEvent event) throws Exception {
		UploadedFile upFile = event.getUploadedFile();

		logger.debug("uploading file: ", upFile.getName());
		String dirName = getUploadDir(upFile.getName());
		long subSubDir = System.currentTimeMillis();
		long subDir = subSubDir % 100;
		StringBuilder sb = new StringBuilder(dirName).append("/")
				.append(subDir).append("/").append(subSubDir);
		File dir = new File(sb.toString());
		dir.mkdirs();

		File file = new File(dir, upFile.getName());
		try {
			FileUtils.writeByteArrayToFile(file, upFile.getData());
		} catch (FileUploadException e) {
			throw new YpException(YpError.FILE_IMPORT_FAILED, e);
		} catch (IOException e) {
			throw new YpException(YpError.FILE_IMPORT_FAILED, e);
		}
		files.add(file);
	}

	private String getUploadDir(String file) throws YpException {
		for (IMediaImporter importer : mediaImporters) {
			if (importer.supportsMediaType(file)) {
				return importer.getMediaUploadDir();
			}
		}
		logger.error("No corresponding importer found for file: ", file);
		throw new YpException(YpError.FILE_IMPORT_FAILED);
	}

	/**
	 * @return the acceptedTypes
	 */
	public String getAcceptedTypes() {
		List<String> exts = new ArrayList<String>();
		for (IMediaImporter importer : mediaImporters) {
			exts.addAll(importer.getSupportedExts());
		}
		return StringUtils.join(exts, ",");
	}

	public void startImport() throws YpException {
		mediaMiscService.startImport(files, this);
	}

	public String getCurrentImportValue() {
		return currentImportValue;
	}

	public int getFilesSize() {
		return files.size();
	}

	@Override
	public void fireImportProgress(File file, long fileProgress,
			long overallProgress) {
		currentImportValue = Long.toString(overallProgress);
	}

	public void setCurrentImportValue(String currentImportValue) {
		this.currentImportValue = currentImportValue;
	}

}
