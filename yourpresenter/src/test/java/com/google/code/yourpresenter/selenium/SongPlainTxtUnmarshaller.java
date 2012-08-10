package com.google.code.yourpresenter.selenium;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import com.google.code.yourpresenter.entity.Song;

public class SongPlainTxtUnmarshaller {

	public Song unmarshall(File file, String encoding) throws IOException {
		Song song = new Song();
		StringBuilder txt = new StringBuilder();
		LineIterator it = null;
		if (StringUtils.isEmpty(encoding)) {
			it = FileUtils.lineIterator(file);
		} else {
			it = FileUtils.lineIterator(file, encoding);
		}

		try {
			while (it.hasNext()) {
				String line = it.nextLine();
				if (StringUtils.isEmpty(song.getName())) {
					song.setName(line);
				} else {
					txt.append(line).append("\n");
				}
			}
		} finally {
			it.close();
		}
		song.setText(txt.toString());
		return song;
	}
}
