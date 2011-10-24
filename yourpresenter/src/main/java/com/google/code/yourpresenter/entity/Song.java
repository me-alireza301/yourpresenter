package com.google.code.yourpresenter.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.google.code.yourpresenter.util.SongText;

/**
 * The Class Song.
 */
@SuppressWarnings("serial")
@Entity
public class Song implements Serializable {

	/** The id. */
	private Long id;

	/** The name. */
	private String name;

	/** The verses. */
	private List<Verse> verses;

	/** The text without punctuation. */
	private String noPunctuationText;

	private transient String text;
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@NotNull
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
//	@NotNull
	// keep in mind to delete prphans: http://javablog.co.uk/2009/12/27/onetomany-fixes-in-jpa-2/
	@OneToMany(mappedBy = "song", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	public List<Verse> getVerses() {
		return verses;
	}

	/**
	 * Sets the verses.
	 * 
	 * @param verses
	 *            the new verses
	 */
	public void setVerses(List<Verse> verses) {
		this.verses = verses;
	}
	
	/**
	 * Gets the text without punctuation.
	 * 
	 * @return the text without punctuation
	 */
	@Basic(fetch = FetchType.LAZY)
	@NotNull
	public String getNoPunctuationText() {
		return noPunctuationText;
	}

	/**
	 * Sets the text without punctuation.
	 * 
	 * @param noPtext
	 *            the new text without punctuation
	 */
	public void setNoPunctuationText(String noPtext) {
		this.noPunctuationText = noPtext;
	}

	public String getText() {
//		if (null == verses) {
//			return "";
//		}
//		
//		StringBuilder sb = new StringBuilder();
//		for (Verse verse : verses) {
//			sb.append(verse.getText());
//			sb.append(File.pathSeparator);
//		}
//		
//		return sb.toString();
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
		
		SongText songText = new SongText(this);
		this.verses = songText.getVerses();
//		this.noPunctuationText = songText.getNoPunctuationText();
		this.noPunctuationText = this.text;
	}
}
