package com.google.code.yourpresenter.util;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilTest {

	@Test
	public void testReplaceDirs() {
		Assert.assertNull(FileUtil.replaceDirs((String[]) null));
		Assert.assertNull(FileUtil.replaceDirs((String) null));
		
		String[] in = new String[] { "${user.home}", "${user.dir}",
				"${java.io.tmpdir}" };
		String[] out = new String[] { FileUtil.USER_HOME, FileUtil.USER_DIR,
				FileUtil.JAVA_IO_TMPDIR };
		Assert.assertArrayEquals(FileUtil.replaceDirs(in), out);

		in = new String[0];
		out = new String[0];
		Assert.assertArrayEquals(FileUtil.replaceDirs(in), out);
		
		in = new String[] { "aaa" };
		out = new String[] { "aaa" };
		Assert.assertArrayEquals(FileUtil.replaceDirs(in), out);
		
		in = new String[] { "${user.home}/aaa" };
		out = new String[] { FileUtil.USER_HOME + "/aaa" };
		Assert.assertArrayEquals(FileUtil.replaceDirs(in), out);
	}

}
