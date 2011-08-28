package com.google.code.yourpresenter.entity.scheduled;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.google.code.yourpresenter.entity.Verse;

// TODO: Auto-generated Javadoc
/**
 * The Class Slide.
 */
@SuppressWarnings("serial")
@Entity
public class Slide implements Serializable {

	/** The id. */
	private Long id;

	/** The verse. */
	private Verse verse;

	/** The background. */
	private Background background;

	/** The presentation. */
	private Presentation presentation;

	public Slide() {
	}
	
	public Slide(Verse verse, Presentation presentation) {
		this.setVerse(verse);
		this.setPresentation(presentation);
	}

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
	 * Gets the verse.
	 * 
	 * @return the verse
	 */
	@OneToOne
	public Verse getVerse() {
		return verse;
	}

	/**
	 * Sets the verse.
	 * 
	 * @param verse
	 *            the new verse
	 */
	public void setVerse(Verse verse) {
		this.verse = verse;
	}

	/**
	 * Gets the background.
	 * 
	 * @return the background
	 */
	@OneToOne
	public Background getBackground() {
		return background;
	}

	/**
	 * Sets the background.
	 * 
	 * @param background
	 *            the new background
	 */
	public void setBackground(Background background) {
		this.background = background;
	}

	/**
	 * Gets the presentation.
	 * 
	 * @return the presentation
	 */
	@ManyToOne
	public Presentation getPresentation() {
		return presentation;
	}

	/**
	 * Sets the presentation.
	 * 
	 * @param presentation
	 *            the new presentation
	 */
	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}
}
