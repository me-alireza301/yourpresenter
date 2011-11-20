package com.google.code.yourpresenter.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.Verse;

@RunWith(value = Parameterized.class)
public class SongTextGetVersesTest {

	private final Song song;
	private List<Verse> verses;

	@Parameters
    public static Collection<Object[]> data() {
    		Song song1 = new Song();
    		song1.setText("Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.\nSlide\nmalesuada id tortor.");
    		Verse verse1_1 = new Verse();
    		verse1_1.setText("Lorem ipsum dolor sit amet,<br/>consectetur adipiscing elit.");
    		Verse verse1_2 = new Verse();
    		verse1_2.setText("malesuada id tortor.");
    		
    		Song song2 = new Song();
    		song2.setText("Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.");
    		Verse verse2_1 = new Verse();
    		verse2_1.setText("Lorem ipsum dolor sit amet,<br/>consectetur adipiscing elit.");
    		
            return Arrays.asList(new Object[][] {
            		{ 
            			song1,
            			new ArrayList<Verse> ( Arrays.asList(new Verse[] { verse1_1, verse1_2}))
            		},
            		{ 
            			song2,
            			new ArrayList<Verse> ( Arrays.asList(new Verse[] { verse2_1}))
            		}
            });
    }

	public SongTextGetVersesTest(Song song, List<Verse> verses) {
		this.song = song;
		this.verses = verses;
	}

	@Test
	public void getVerses() {
		List<Verse> compare = new SongText(this.song).getVerses();
		Assert.assertEquals(this.verses.size(), compare.size());
		for (int i = 0; i < this.verses.size(); i++) {
			Assert.assertEquals(this.verses.get(i).getText(), compare.get(i)
					.getText());
		}
	}

}
