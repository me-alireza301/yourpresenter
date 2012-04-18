package com.google.code.yourpresenter.media;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.MediaType;
import com.google.code.yourpresenter.util.FileUtil;
import com.google.code.yourpresenter.util.Logger;
import com.google.code.yourpresenter.util.LoggerFactory;
import com.google.code.yourpresenter.util.StringOutputStream;
import com.google.code.yourpresenter.util.SystemUtil;

@SuppressWarnings("serial")
@Service
@Qualifier("videoImporter")
public class VideoMPlayerImporter extends AbstractMediaImporter {

	private static Logger logger = LoggerFactory
			.getLogger(VideoMPlayerImporter.class);

	private static final Pattern lengthPattern = Pattern
			.compile("ID[_]LENGTH=([\\d[.]]+)");
	private static final Pattern fpsPattern = Pattern
			.compile("ID[_]VIDEO[_]FPS=([\\d[.]]+)");

	private final Set<String> supportedExts = new HashSet<String>(
			Arrays.asList("mpg", "mpeg", "avi", "mkv", "mov"));

	private static final String[] paramsLength = new String[] { "-ao", "null",
			"-frames", "0", "-identify" };

	private static final String[] paramsThumb = new String[] { "-nosound", "-vo" };

	@Override
	public Set<String> getSupportedExts() {
		return supportedExts;
	}

	private String getMPlayerExec() throws YpException {
		final String gsHome = preferenceService
				.findStringById(IConstants.MEDIA_IMPORT_VIDEO_MPLAYER_HOME);

		StringBuilder sb = new StringBuilder();
		switch (SystemUtil.getOS()) {
		case LINUX:
		case UNIX:
		case MAC:
			sb.append("mplayer");
			break;
		case WINDOWS:
			sb.append(gsHome).append("\\mplayer.exe");
			break;
		case UNKNOWN:
			throw new YpException(YpError.FILE_IMPORT_FAILED, "unknown OS");
		}
		return sb.toString();
	}

	@Override
	public File[] importMedia(String media, File outDir) throws YpException {
		final String mediaPath = new File(media).getAbsolutePath();

		extThumbs(media, outDir);
		
		List<File> files = new ArrayList<File>();
		for (File file : outDir.listFiles()) {
			if (!file.getAbsolutePath().equals(mediaPath)) {
				files.add(file);
			}
		}
		
		// for some filesystems files are not in the expected order
		Collections.sort(files);
		
		return files.toArray(new File[files.size()]);

	}

	private int getFramestep(String media) throws YpException {
		List<String> args = new ArrayList<String>(Arrays.asList(paramsLength));
		args.add(media);

		// redirect error to string
		OutputStream err = new StringOutputStream();
		// redirect output to string
		OutputStream out = new StringOutputStream();

		SystemUtil.exec(getMPlayerExec(), args, null, out, new File(media),
				err, true, true);

		if (!err.toString().isEmpty()) {
			logger.error(err.toString());
		}

		if (out.toString().isEmpty()) {
			logger.error("Output of command empty => error importing file!");
			return -1;
		}

		logger.debug(out.toString());

		float fps = -1;
		float length = -1;
		int thumbnalCount = Integer.parseInt(preferenceService.findStringById(IConstants.MEDIA_IMPORT_VIDEO_MPLAYER_THUMBNAIL_COUNT));
		String[] lines = out.toString().split(SystemUtil.LINE_SEPARATOR);
		for (String line : lines) {
			Matcher matcher = lengthPattern.matcher(line);
			if (matcher.matches()) {
				length = Float.valueOf(matcher.group(1));
			} 
			matcher = fpsPattern.matcher(line);
			if (matcher.matches()) {
				fps = Float.valueOf(matcher.group(1));
			} 
			if ((-1 != fps) && (-1 != length)) {
				return Math.round(length * fps / thumbnalCount);
			}
		}

		logger.error("Output of command unexpected => framestep not detected => error importing file!");
		return -1;
	}

	private boolean extThumbs(final String media, final File outDir)
			throws YpException {

		final int framestep = getFramestep(media);
		if (-1 == framestep) {
			return false;
		}

		extThumbs(media, outDir, framestep);
		return true;
	}

	private void extThumbs(final String media, final File outDir,
			final int framestep) throws YpException {
		List<String> args = new ArrayList<String>(Arrays.asList(paramsThumb));

		switch (SystemUtil.getOS()) {
		case WINDOWS:
			// \"\" needed for windows, see:
			// http://groups.google.com/group/mplayer_users/browse_thread/thread/4b8527d90bcbfb8f/43570d939abaff11?pli=1
			args.add(new StringBuilder("jpeg:outdir=\"\"\"")
					.append(outDir.getAbsolutePath()).append("\"\"\"").toString());
			break;
		default:
			args.add(new StringBuilder("jpeg:outdir=").append(
					outDir.getAbsolutePath()).toString());
		}
		// we want only each n-th frame, see: http://lists.mplayerhq.hu/pipermail/mplayer-users/2009-August/077609.html
		args.addAll(Arrays.asList("-vf", "framestep=" + framestep));
		args.add(media);

		// redirect error to string
		OutputStream err = new StringOutputStream();
		// redirect output to string
		OutputStream out = new StringOutputStream();

		// this is a very specific case as quoting is handled in the code rather than on common place
		SystemUtil.exec(getMPlayerExec(), args, null, out, new File(media),
				err, false, false);

		if (!err.toString().isEmpty()) {
			logger.error(err.toString());
		}
	}

	@Override
	public String getMediaUploadDir() throws YpException {
		return FileUtil.replaceDirs(preferenceService
				.findStringById(IConstants.MEDIA_UPLOAD_DIR_VIDEO));
	}

	@Override
	public MediaType getMediaType() throws YpException {
		return mediaTypeService.findByName(IConstants.MEDIA_TYPE_VIDEO);
	}

}
