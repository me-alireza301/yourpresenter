/**
 * 
 */
package com.google.code.yourpresenter.media;

import java.io.File;
import java.util.Arrays;

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

/**
 * @author Peter Butkovic
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class VideoMPlayerImporterTest {

	@Mock
	private IPreferenceService preferenceService;

	private File[] files;
	private final File dir = new File(PptJodConvImporterTest.RESOURCE_PATH
			+ "/vid");
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dir.mkdirs();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (null == files) {
			return;
		}

		for (File file : files) {
			file.delete();
		}
	}

	@Test
	public void test() throws YpException {
		switch (SystemUtil.getOS()) {
		case WINDOWS:
			Mockito.when(
					preferenceService
							.findStringById(IConstants.MEDIA_IMPORT_VIDEO_MPLAYER_HOME))
					.thenReturn("c:\\Program Files\\SMPlayer\\mplayer");
			break;
		}
		
		Mockito.when(
				preferenceService
						.findStringById(IConstants.MEDIA_IMPORT_VIDEO_MPLAYER_THUMBNAIL_COUNT))
				.thenReturn("10");
		
		IMediaImporter importer = new VideoMPlayerImporter();
		((VideoMPlayerImporter) importer).preferenceService = preferenceService;
		File inFile = new File(PptJodConvImporterTest.RESOURCE_PATH
				+ "/Blue-Drizzle.mov");
		files = importer.importMedia(inFile.getAbsolutePath(), dir);

		// asserts stuff
		Assert.assertTrue(10 <= files.length);

		// for some filesystems files are not in the expected order
		Arrays.sort(files);
		
		int i = 0;
		for (File file : files) {
			Assert.assertEquals(file.getName(),
					// add leading zeros, see: http://www.kodejava.org/examples/174.html
					String.format("%08d.jpg", ++i));
		}
	}

}
