package com.google.code.yourpresenter.media;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Preference;
import com.google.code.yourpresenter.service.IBgImageService;
import com.google.code.yourpresenter.service.IPreferenceService;
import com.google.code.yourpresenter.util.FileObjectUtil;
import com.google.code.yourpresenter.util.FileUtil;
import com.google.code.yourpresenter.util.IPreferenceChangedListener;
import com.google.code.yourpresenter.util.Logger;
import com.google.code.yourpresenter.util.LoggerFactory;
import com.google.code.yourpresenter.util.PostInitialize;

@SuppressWarnings("serial")
@Service
public class MediaCrawlerImpl implements IMediaCrawler,
		IPreferenceChangedListener, Serializable {

	private static Logger logger = LoggerFactory.getLogger(MediaCrawlerImpl.class);
	
	@Autowired
	private IPreferenceService preferenceService;
	@Autowired
	private IBgImageService bgImageService;
	@Autowired
	private IThumbnailCreator thumbnailCreatorService;
	@Autowired
	private IMediaFileSelector mediaFileSelectorImpl;

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

		List<FileObject> media = new ArrayList<FileObject>();
		try {
			this.mediaFileSelectorImpl.setAcceptedExts(preferenceService
					.findStringArrayById(IConstants.MEDIA_ACCEPTED_EXTS));

			FileSystemManager fsManager = VFS.getManager();
			for (String dir : dirs) {
				FileObject root;
				root = fsManager.resolveFile(new File(dir).getAbsolutePath());
				final FileObject[] found = root
						.findFiles(this.mediaFileSelectorImpl);
				if (null != found) {
					logger.debug("Found media files:", Arrays.toString(found));
					media.addAll(Arrays.asList(found));
				}
			}
		} catch (FileSystemException e) {
			throw new YpException(YpError.MEDIA_CRAWLING_FAILED, e);
		}

		persistImagesDiff(media);
	}

	private void persistImagesDiff(List<FileObject> media) throws YpException {
		String thumbDir = this.preferenceService
				.findStringById(IConstants.MEDIA_THUMBNAIL_DIR);
		thumbDir = FileUtil.replaceDirs(thumbDir);
		
		// init thumbnail dir
		new File(thumbDir).mkdirs();
		
		final String thumbExt = this.preferenceService
				.findStringById(IConstants.MEDIA_THUMBNAIL_EXT);
		final int thumbWidth = Integer.parseInt(this.preferenceService
				.findStringById(IConstants.MEDIA_THUMBNAIL_WIDTH));

		this.thumbnailCreatorService.setThumbnailWidth(thumbWidth);

		Collection<BgImage> bgImages = this.bgImageService.findAll();
		Set<BgImage> bgImagesSet = null;
		if (!CollectionUtils.isEmpty(bgImages)) {
			bgImagesSet = new HashSet<BgImage>(bgImages);
		}

		// get modified/new files only
		for (FileObject fileObject : media) {
			logger.debug("fileObject: ", fileObject);
			BgImage bgImage;
			try {
				bgImage = new BgImage(
						FileObjectUtil.getAbsolutePath(fileObject), fileObject
								.getContent().getLastModifiedTime());
			} catch (FileSystemException e) {
				throw new YpException(YpError.MEDIA_CRAWLING_FAILED, e);
			}

			persistImageDiff(thumbDir, thumbExt, bgImagesSet, bgImage);
		}
	}

	private void persistImageDiff(final String thumbDir, final String thumbExt,
			final Set<BgImage> bgImagesSet, BgImage bgImage) throws YpException {
		// if not imported yet
		if ((null == bgImagesSet)
				|| (null != bgImagesSet && !bgImagesSet.contains(bgImage))) {
			this.bgImageService.persist(bgImage);

			final String thumbnailName = new StringBuilder(
					Long.toString(bgImage.getId())).append(".")
					.append(thumbExt).toString();
			final File thumbnail = new File(thumbDir, thumbnailName);

			this.thumbnailCreatorService.generateThumbnail(bgImage.getImage(),
					thumbnail);

			// TODO change so that it can be served as static content possibly
			bgImage.setThumbnail(thumbnail.getAbsolutePath());
			this.bgImageService.persist(bgImage);
		}
	}

	@Override
	public void preferenceChanged(Preference preference) throws YpException {
		String dirs = preference.getValue();
		crawl(dirs.split(","));
	}

	@PostInitialize(order = IConstants.POST_INIT_IDX_MEDIA_CRAWLER)
	@Async
	@Override
	public void crawl() throws YpException {
		crawl((String[]) null);
	}
}
