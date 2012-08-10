package com.google.code.yourpresenter.media;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.MediaType;
import com.google.code.yourpresenter.util.FileUtil;
import com.google.code.yourpresenter.util.StringOutputStream;
import com.google.code.yourpresenter.util.SystemUtil;

@SuppressWarnings("serial")
@Service
@Qualifier("pdfImporter")
@Slf4j
public class PdfGhostImporter extends AbstractMediaImporter {

	private final Set<String> supportedExts = new HashSet<String>(
			Arrays.asList("pdf"));

	private static final String[] params = new String[] { "-q", "-dQUIET",
			"-dPARANOIDSAFER", // Run this command in safe mode
			"-dBATCH", // Keep gs from going into interactive mode
			"-dNOPAUSE", // Do not prompt and pause for each page
			"-dNOPROMPT", // Disable prompts for user interaction
			"-dTextAlphaBits=4", "-dGraphicsAlphaBits=4", };

	private String getGsExec() throws YpException {
		final String gsHome = preferenceService
				.findStringById(IConstants.MEDIA_IMPORT_PDF_GHOSTSCRIPT_HOME);

		StringBuilder sb = new StringBuilder();
		switch (SystemUtil.getOS()) {
		case LINUX:
		case UNIX:
		case MAC:
			sb.append("gs");
			break;
		case WINDOWS:
			sb.append(gsHome).append("/bin/");
			File dir = new File(sb.toString());
			if (!dir.isDirectory()) {
				throw new YpException(YpError.FILE_IMPORT_FAILED,
						"ghostscript exec not found on path: " + sb.toString());
			}
			for (File f : dir.listFiles()) {
				// we need command line binary
				if (f.getName().contains("c.exe")) {
					sb.append(f.getName());
					break;
				}
			}
			break;
		case UNKNOWN:
			throw new YpException(YpError.FILE_IMPORT_FAILED, "unknown OS");
		}
		return sb.toString();
	}

	@Override
	public File[] importMedia(final String media, File outDir)
			throws YpException {

		final String ext = preferenceService
				.findStringById(IConstants.MEDIA_IMPORT_PDF_IMG_TYPE);
		final File mediaFile = new File(media);

		List<String> args = new ArrayList<String>(Arrays.asList(params));
		args.add(new StringBuilder("-sDEVICE=").append(ext).append("16m")
				.toString());
		args.add(new StringBuilder("-sOutputFile=")
				.append(outDir.getAbsolutePath()).append("/")
				.append(mediaFile.getName()).append("_%ld.").append(ext)
				.toString());
		args.add(media);

		// redirect error to string
		OutputStream err = new StringOutputStream();
		// redirect output to string
		OutputStream out = new StringOutputStream();

		SystemUtil.exec(getGsExec(), args, null, out, mediaFile, err, true,
				false);

		if (!err.toString().isEmpty()) {
			log.error(err.toString());
		}

		// add the extracted pages to result
		List<File> files = new ArrayList<File>();
		for (File file : outDir.listFiles()) {
			if (!file.getAbsolutePath().equals(media)) {
				files.add(file);
			}
		}

		// for some filesystems files are not in the expected order
		Collections.sort(files);

		return files.toArray(new File[files.size()]);
	}

	@Override
	public Set<String> getSupportedExts() {
		return supportedExts;
	}

	@Override
	public MediaType getMediaType() throws YpException {
		return mediaTypeService.findByName(IConstants.MEDIA_TYPE_MISC);
	}

	@Override
	public String getMediaUploadDir() throws YpException {
		return FileUtil.replaceDirs(preferenceService
				.findStringById(IConstants.MEDIA_UPLOAD_DIR_MISC));
	}
}
