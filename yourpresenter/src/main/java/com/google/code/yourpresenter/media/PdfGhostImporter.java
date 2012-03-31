package com.google.code.yourpresenter.media;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.util.Logger;
import com.google.code.yourpresenter.util.LoggerFactory;
import com.google.code.yourpresenter.util.StringOutputStream;
import com.google.code.yourpresenter.util.SystemUtil;

@SuppressWarnings("serial")
@Service
@Qualifier("pdfImporter")
public class PdfGhostImporter extends AbstractMediaImporter {

	private static Logger logger = LoggerFactory
			.getLogger(PdfGhostImporter.class);

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

		StringBuilder sb = new StringBuilder(gsHome).append("/bin/");
		switch (SystemUtil.getOS()) {
		case LINUX:
		case UNIX:
		case MAC:
			sb.append("gs");
			break;
		case WINDOWS:
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
	public File[] importMedia(final String media, File outDir) throws YpException {

		final String ext = preferenceService
				.findStringById(IConstants.MEDIA_IMPORT_PDF_IMG_TYPE);
		final File mediaFile = new File(media);
		final String mediaPath = mediaFile.getAbsolutePath();

		List<String> args = new ArrayList<String>(Arrays.asList(params));
		args.add(new StringBuilder("-sDEVICE=").append(ext).append("16m")
				.toString());
		args.add(new StringBuilder("-sOutputFile=")
				.append(outDir.getAbsolutePath()).append("/")
				.append(mediaFile.getName()).append("_%ld.").append(ext)
				.toString());
		args.add(mediaPath);

		// redirect error to string
		OutputStream err = new StringOutputStream();
		// redirect output to string
		OutputStream out = new StringOutputStream();

		SystemUtil.exec(getGsExec(), args, null, out, new File(media), err,
				false);

		if (!err.toString().isEmpty()) {
			logger.error(err.toString());
		}

		// add the extracted pages to result
		List<File> files = new ArrayList<File>();
		for (File file : outDir.listFiles()) {
			if (!file.getAbsolutePath().equals(mediaPath)) {
				files.add(file);
			}
		}
		return files.toArray(new File[files.size()]);
	}

	@Override
	public Set<String> getSupportedExts() {
		return supportedExts;
	}
}