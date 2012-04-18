package com.google.code.yourpresenter.media;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.MediaType;
import com.google.code.yourpresenter.util.FileUtil;
import com.google.code.yourpresenter.util.SystemUtil;

@SuppressWarnings("serial")
@Service
@Qualifier("pptImporter")
public class PptJodConvImporter extends AbstractMediaImporter {

	@Autowired
	@Qualifier("pdfImporter")
	protected IMediaImporter pdfImporter;

	private final Set<String> supportedExts = new HashSet<String>(
			Arrays.asList("ppt", "pptx", "pptm", "odp", "sxi", "ppsx", "pps"));

	@Override
	public File[] importMedia(final String media, File outDir)
			throws YpException {
		File pdf = pptToPdf(media, outDir);
		return pdfToPng(pdf.getAbsolutePath(), outDir);
	}

	private File[] pdfToPng(String pdf, File outDir) throws YpException {
		return pdfImporter.importMedia(pdf, outDir);
	}

	protected File pptToPdf(final String media, File outDir) throws YpException {
		OfficeManager officeManager = null;
		try {
			DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();

			switch (SystemUtil.getOS()) {
			case WINDOWS:
				String officeHome = preferenceService
						.findStringById(IConstants.MEDIA_IMPORT_PPT_OFFICE_HOME);
				
				if (!StringUtils.isEmpty(officeHome)) {
					config.setOfficeHome(officeHome);
				}
				break;
			}

			officeManager = config.buildOfficeManager();
		} catch (IllegalStateException e) {
			throw new YpException(YpError.FILE_IMPORT_FAILED, e);
		}

		officeManager.start();
		OfficeDocumentConverter converter = new OfficeDocumentConverter(
				officeManager);

		File mediaFile = new File(media);
		
		File pdf = new File(outDir, mediaFile.getName() + ".pdf");
		converter.convert(mediaFile, pdf);
		officeManager.stop();
		return pdf;
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
		return FileUtil.replaceDirs(preferenceService.findStringById(IConstants.MEDIA_UPLOAD_DIR_MISC));
	}
}
