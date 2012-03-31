package com.google.code.yourpresenter.media;

import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.service.IPreferenceService;
import com.google.code.yourpresenter.util.SystemUtil;

@RunWith(MockitoJUnitRunner.class)
public class PdfGhostImporterTest {

	@Mock
	private IPreferenceService preferenceService;

	private File[] files;
	private final File dir = new File(PptJodConvImporterTest.RESOURCE_PATH
			+ "/ghost");

	@Test
	public void test() throws YpException {
		
		switch (SystemUtil.getOS()) {
		case WINDOWS:
			Mockito.when(
					preferenceService
							.findStringById(IConstants.MEDIA_IMPORT_PDF_GHOSTSCRIPT_HOME))
					.thenReturn("C:\\Program Files\\gs\\gs9.04");
			break;
		}
		
		Mockito.when(
				preferenceService
						.findStringById(IConstants.MEDIA_IMPORT_PDF_IMG_TYPE))
				.thenReturn("png");

		IMediaImporter importer = new PdfGhostImporter();
		((PdfGhostImporter) importer).preferenceService = preferenceService;
		File inFile = new File(PptJodConvImporterTest.RESOURCE_PATH
				+ "/appa.pdf");
		files = importer.importMedia(inFile.getAbsolutePath(), dir);

		// asserts stuff
		Assert.assertEquals(6, files.length);

		int i = 0;
		for (File file : files) {
			Assert.assertEquals(file.getName(),
					new StringBuilder(inFile.getName()).append("_").append(++i)
							.append(".png").toString());
		}
	}

	@Before
	public void setup() {
		dir.mkdirs();
	}

	@After
	public void cleanup() {
		if (null == files) {
			return;
		}

		for (File file : files) {
			file.delete();
		}
	}

}
