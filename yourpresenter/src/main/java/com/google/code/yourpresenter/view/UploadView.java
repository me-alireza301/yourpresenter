package com.google.code.yourpresenter.view;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import com.google.code.yourpresenter.service.IMediaService;
import com.google.code.yourpresenter.util.IMediaImportProgressListener;

@Component("uploadView")
@Scope(WebApplicationContext.SCOPE_SESSION)
@SuppressWarnings("serial")
@Slf4j
public class UploadView implements IMediaImportProgressListener, Serializable {

	@Autowired
	private List<IMediaImporter> mediaImporters;

	private List<File> files = new ArrayList<File>();

	private String currentImportValue = "-1";

	@Autowired
	private IMediaService mediaMiscService;

	// TODO issue: http://code.google.com/p/yourpresenter/issues/detail?id=2
//	public void clear(AjaxBehaviorEvent event) throws Exception {
//		int i = 0;
//	}
	
	public void discart() {
		log.debug("Import discarted, deleting files: {}", files);
		for (File file : files) {
			try {
				FileUtils.deleteDirectory(file.getParentFile());
			} catch (IOException e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}	
		}
		files.clear();
	}
	
	public void upload(FileUploadEvent event) throws Exception {
		UploadedFile upFile = event.getUploadedFile();

		log.debug("uploading file: {}", upFile.getName());
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
		log.error("No corresponding importer found for file: {}", file);
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
		mediaMiscService.startImport(files/*, this*/);
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
