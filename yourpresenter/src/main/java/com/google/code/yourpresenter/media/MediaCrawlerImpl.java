package com.google.code.yourpresenter.media;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Preference;
import com.google.code.yourpresenter.service.IMediaService;
import com.google.code.yourpresenter.service.IPreferenceService;
import com.google.code.yourpresenter.util.FileObjectUtil;
import com.google.code.yourpresenter.util.FileUtil;
import com.google.code.yourpresenter.util.IPreferenceChangedListener;
import com.google.code.yourpresenter.util.PostInitialize;

@SuppressWarnings("serial")
@Service
@Slf4j
public class MediaCrawlerImpl implements IMediaCrawler,
		IPreferenceChangedListener, Serializable {

	@Autowired
	private List<IMediaImporter> mediaImporters;
	
	@Autowired
	private IPreferenceService preferenceService;
	@Autowired
	private IMediaFileSelector mediaFileSelectorImpl;
	@Autowired
	private IMediaService mediaService;

	public MediaCrawlerImpl() {
	}

	@PostInitialize(order = IConstants.POST_INIT_IDX_MEDIA_CRAWLER)
	public void registerForPreferenceChange() throws YpException {
		preferenceService.registerPreferenceChangedListener(this,
				IConstants.MEDIA_DIRS);
	}

	@Transactional
	public void crawl(String... dirs) throws YpException {
		if (null == dirs) {
			dirs = preferenceService.findStringArrayById(IConstants.MEDIA_DIRS);
		}

		// replace special dir refs ${XXX}
		dirs = FileUtil.replaceDirs(dirs);

		List<File> files = new ArrayList<File>();
		try {
			this.mediaFileSelectorImpl.setAcceptedExts(this.getAcceptedExts());

			FileSystemManager fsManager = VFS.getManager();
			for (String dir : dirs) {
				FileObject root;
				root = fsManager.resolveFile(new File(dir).getAbsolutePath());
				final FileObject[] found = root
						.findFiles(this.mediaFileSelectorImpl);
				if (null != found) {
					for (FileObject fileObject : found) {
						log.debug("Found media file: {}", fileObject);
						files.add(new File(FileObjectUtil.getAbsolutePath(fileObject)));
					}

					mediaService.startImport(files/*, this*/);
				}
			}
		} catch (FileSystemException e) {
			throw new YpException(YpError.MEDIA_CRAWLING_FAILED, e);
		}
	}

	@Override
	public void preferenceChanged(Preference preference) throws YpException {
		String dirs = preference.getValue();
		crawl(dirs.split(","));
	}

	@PostInitialize(order = IConstants.POST_INIT_IDX_MEDIA_CRAWLER)
//	@Async
	@Override
	public void crawl() throws YpException {
		crawl((String[]) null);
	}
	
	
	/**
	 * @return the accepted extensions
	 */
	protected String[] getAcceptedExts() {
		List<String> exts = new ArrayList<String>();
		for (IMediaImporter importer : mediaImporters) {
			exts.addAll(importer.getSupportedExts());
		}
		return exts.toArray(new String [exts.size()]);
	}
}
