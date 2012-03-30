package com.google.code.yourpresenter.util;

import java.io.File;

public interface IMediaImportProgressListener {

	/**
	 * @param file				File beeing imported.
	 * @param fileProgress		Percentual per file progress.
	 * @param overallProgress 	Percentual overall progress.
	 */
	public void fireImportProgress(File file, long fileProgress, long overallProgress);
}
