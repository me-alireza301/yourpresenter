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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.service.IBgImageService;
import com.google.code.yourpresenter.service.IPreferenceService;
import com.google.code.yourpresenter.util.FileObjectUtil;
import com.google.code.yourpresenter.util.PostInitialize;

@SuppressWarnings("serial")
@Service("mediaCrawler")
public class MediaCrawlerImpl implements IMediaCrawler, Serializable {

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
	@Transactional
	public void crawl() throws YpException {
		final String[] dirs = preferenceService
				.findStringArrayById(IConstants.MEDIA_DIRS);

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
					media.addAll(Arrays.asList(found));
				}
			}
		} catch (FileSystemException e) {
			throw new YpException(YpError.MEDIA_CRAWLING_FAILED, e);
		}

		persistImagesDiff(media);
	}

	private void persistImagesDiff(List<FileObject> media) throws YpException {
		final String thumbDir = this.preferenceService
				.findStringById(IConstants.MEDIA_THUMBNAIL_DIR);
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

	private void persistImageDiff(final String thumbDir, final String thumbExt, final Set<BgImage> bgImagesSet,
			BgImage bgImage) throws YpException {
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

}
