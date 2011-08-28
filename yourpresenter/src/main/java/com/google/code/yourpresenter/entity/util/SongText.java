package com.google.code.yourpresenter.entity.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.Verse;

public class SongText {

	private static final String NEW_VERSE = "\\s*((slide)|(verse))\\s*\\d*\\s*";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String HTML_LINE_SEPARATOR = "<br/>";
	private Song song;
	

	public SongText(Song song) {
		this.song = song;
	}

	private boolean isTextEmpty() {
		return null == this.song || null == this.song.getText()
				|| this.song.getText().trim().isEmpty();
	}

	public List<Verse> getVerses() {
		if (isTextEmpty()) {
			return null;
		}

		List<Verse> verses  = new ArrayList<Verse>();
		BufferedReader reader = new BufferedReader(new StringReader(
				this.song.getText()));

		try {
			StringBuilder txt = new StringBuilder();
			String line;
			
			while (null != (line = reader.readLine())) {
				// for non-empty lines
				if (!line.trim().isEmpty()) {
					// new slide indicator
					if (line.toLowerCase().matches(NEW_VERSE)) {
						// remove unneeded last HTML_LINE_SEPARATOR
						txt.setLength(txt.length() - HTML_LINE_SEPARATOR.length() - 1);
						
						// add verse read
						verses.add(new Verse(txt.toString().trim(), song));
						txt.setLength(0);
						
					// regular text case
					} else {
						txt.append(line.trim()).append(HTML_LINE_SEPARATOR);
					}
					
				// there might be explicitelly set empty line within verse => preserve it
				} else if (0 < txt.length()) {
					txt.append(line.trim()).append(HTML_LINE_SEPARATOR);
				}
			}
			
			if (0 < txt.length()) {
				// remove unneeded last HTML_LINE_SEPARATOR
				txt.setLength(txt.length() - HTML_LINE_SEPARATOR.length() - 1);
				
				// add the last verse read
				verses.add(new Verse(txt.toString().trim(), song));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return verses;
	}

	public String getNoPunctuationText() {
		if (isTextEmpty()) {
			return null;
		}
		
		// TODO Auto-generated method stub
		return null;
	}
}
