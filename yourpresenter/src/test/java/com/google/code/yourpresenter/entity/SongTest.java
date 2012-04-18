package com.google.code.yourpresenter.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class SongTest {

	private final Song song;
	private List<Verse> verses;

	@Parameters
    public static Collection<Object[]> data() {
    		Song song1 = new Song();
    		song1.setText("Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.\nSlide\nmalesuada id tortor.");
    		Verse verse1_1 = new Verse("Lorem ipsum dolor sit amet,<br/>consectetur adipiscing elit.", song1);
    		Verse verse1_2 = new Verse("malesuada id tortor.", song1);
    		
    		Song song2 = new Song();
    		song2.setText("Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.");
    		Verse verse2_1 = new Verse("Lorem ipsum dolor sit amet,<br/>consectetur adipiscing elit.", song2);
    		
    		Song song3 = new Song();
    		song3.setText("\n\nLorem ipsum dolor sit amet,\nconsectetur adipiscing elit.\n");
    		Verse verse3_1 = new Verse("Lorem ipsum dolor sit amet,<br/>consectetur adipiscing elit.", song3);
    		
            return Arrays.asList(new Object[][] {
            		{ 
            			song1,
            			new ArrayList<Verse> ( Arrays.asList(new Verse[] { verse1_1, verse1_2}))
            		},
            		{ 
            			song2,
            			new ArrayList<Verse> ( Arrays.asList(new Verse[] { verse2_1}))
            		},
            		{ 
            			song3,
            			new ArrayList<Verse> ( Arrays.asList(new Verse[] { verse3_1}))
            		}
            });
    }

	public SongTest(Song song, List<Verse> verses) {
		this.song = song;
		this.verses = verses;
	}

	@Test
	public void parseText() {
		Assert.assertEquals(this.verses.size(), song.getVerses().size());
		for (int i = 0; i < this.verses.size(); i++) {
			Assert.assertEquals(this.verses.get(i).getText(), song.getVerses().get(i)
					.getText());
		}
	}

}
