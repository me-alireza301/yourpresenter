package com.google.code.yourpresenter;


public interface IConstants {
	
	// preferences keys
	public static final String MEDIA_DIRS = "media.dirs";
	public static final String MEDIA_IMPORT_PDF_GHOSTSCRIPT_DPI = "media.import.pdf.ghostscript.dpi";
	public static final String MEDIA_IMPORT_PDF_GHOSTSCRIPT_HOME = "media.import.pdf.ghostscript.home";
	public static final String MEDIA_IMPORT_PDF_IMG_TYPE = "media.import.pdf.img.type";
	public static final String MEDIA_IMPORT_PPT_OFFICE_HOME = "media.import.ppt.office.home";
	public static final String MEDIA_IMPORT_VIDEO_MPLAYER_HOME = "media.import.video.mplayer.home";
	public static final String MEDIA_IMPORT_VIDEO_MPLAYER_THUMBNAIL_COUNT = "media.import.video.mplayer.thumbnail.count";
	public static final String MEDIA_IMAGE_CONTENT_TYPE = "media.image.content.type";
	public static final String MEDIA_THUMBNAIL_DIR = "media.thumbnail.dir";
	public static final String MEDIA_THUMBNAIL_EXT = "media.thumbnail.ext";
	public static final String MEDIA_THUMBNAIL_WIDTH = "media.thumbnail.width";
	public static final String MEDIA_THUMBNAIL_CONTENT_TYPE = "media.thumbnail.content.type";
	public static final String MEDIA_UPLOAD_DIR_IMG = "media.upload.dir.img";
	public static final String MEDIA_UPLOAD_DIR_MISC = "media.upload.dir.misc";
	public static final String MEDIA_UPLOAD_DIR_VIDEO = "media.upload.dir.video";
	public static final String VIEW_FONT_MAXSIZE_PRESENTER = "view.font.maxsize.presenter";
	public static final String VIEW_FONT_MAXSIZE_PROJECTOR = "view.font.maxsize.projector";
	public static final String VIEW_MEDIA_HEIGHT = "view.media.height";
	public static final String VIEW_MEDIA_WIDTH = "view.media.width";
	public static final String VIEW_SLIDE_HEIGHT = "view.slide.height";
	public static final String VIEW_SLIDE_WIDTH = "view.slide.width";
	
	// notifications
	public static final String VIEW_NOTIFY_NONBLOCKING = "view.notify.nonblocking";
	public static final String VIEW_NOTIFY_NONBLOCKINGOPACITY = "view.notify.nonblockingOpacity";
	public static final String VIEW_NOTIFY_SHOWCLOSEBUTTON = "view.notify.showCloseButton";
	public static final String VIEW_NOTIFY_SHOWSHADOW = "view.notify.showShadow";
	public static final String VIEW_NOTIFY_STAYTIME = "view.notify.stayTime";
	public static final String VIEW_NOTIFY_STICKY = "view.notify.sticky";
	
	// MediaType names
	public static final String MEDIA_TYPE_IMG = "img";
	public static final String MEDIA_TYPE_VIDEO = "vid";
	public static final String MEDIA_TYPE_MISC = "misc";
	
	// @PostInitialize indexes
	public static final int POST_INIT_IDX_DEFAULT_DATA_LOAD = 1;
	public static final int POST_INIT_IDX_MEDIA_CRAWLER = 10;
	
	
}
