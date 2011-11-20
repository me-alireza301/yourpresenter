package com.google.code.yourpresenter;


public interface IConstants {
	
	// preferences keys
	public static final String MEDIA_ACCEPTED_EXTS = "media.accepted.exts";
	public static final String MEDIA_DIRS = "media.dirs";
	public static final String MEDIA_IMAGE_CONTENT_TYPE = "media.image.content.type";
	public static final String MEDIA_THUMBNAIL_DIR = "media.thumbnail.dir";
	public static final String MEDIA_THUMBNAIL_EXT = "media.thumbnail.ext";
	public static final String MEDIA_THUMBNAIL_WIDTH = "media.thumbnail.width";
	public static final String MEDIA_THUMBNAIL_CONTENT_TYPE = "media.thumbnail.content.type";
	
	public static final String VIEW_FONT_MAXSIZE_PRESENTER = "view.font.maxsize.presenter";
	public static final String VIEW_FONT_MAXSIZE_PROJECTOR = "view.font.maxsize.projector";
	
	// @PostInitialize indexes
	public static final int POST_INIT_IDX_PREFERENCE_SERVICE = 1;
	public static final int POST_INIT_IDX_MEDIA_CRAWLER = 10;
	
}
