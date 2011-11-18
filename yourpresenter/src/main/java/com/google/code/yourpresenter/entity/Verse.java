package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

/**
 * The Class Verse.
 */
@SuppressWarnings("serial")
@Entity
public class Verse implements Serializable {

	/** The id. */
	private Long id;

	/** The text. */
	private String text;

	/** The song. */
	@JsonIgnore
	private Song song;

	public Verse(String text, Song song) {
		super();
		this.text = text;
		this.song = song;
	}
	
	// default constructor needed to prevent Exception:
	// org.springframework.orm.hibernate3.HibernateSystemException: No default constructor for entity: com.google.code.yourpresenter.entity.Verse
	public Verse() {
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@Index(name="VerseIdIdx")
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
	 * Gets the text.
	 * 
	 * @return the text
	 */
	// keep in sync with song.text
	@NotNull
	@Column(columnDefinition = "VARCHAR(1000)")
	@Size(max = 1000)
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the song.
	 * 
	 * @return the song
	 */
	@ManyToOne(optional = false)
	@Index(name="VerseSongIdx")
	public Song getSong() {
		return song;
	}

	/**
	 * Sets the song.
	 * 
	 * @param song
	 *            the new song
	 */
	public void setSong(Song song) {
		this.song = song;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
