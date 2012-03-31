package com.google.code.yourpresenter.media;

import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.service.IPreferenceService;

@RunWith(MockitoJUnitRunner.class)
public class PptJodConvImporterTest {

	@Mock
	private IPreferenceService preferenceService;

	public static final String RESOURCE_PATH = "target/test-classes/com/google/code/yourpresenter/media";

	private File file;

	@Test
	public void test() throws YpException {
		PptJodConvImporter pptImporter = new PptJodConvImporter();

		Mockito.when(
				preferenceService
						.findStringById(IConstants.MEDIA_IMPORT_PPT_OFFICE_HOME))
				.thenReturn("C:/Program Files/LibreOffice 3.4");

		pptImporter.preferenceService = preferenceService;
		file = pptImporter.pptToPdf(RESOURCE_PATH + "/Canada sample.ppt",
				new File(RESOURCE_PATH));

		Assert.assertEquals("Canada sample.ppt.pdf", file.getName());
	}

	@After
	public void cleanup() {
		if (null != file) {
			file.delete();
		}
	}

}
