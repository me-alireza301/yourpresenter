package com.google.code.yourpresenter.entity.scheduled;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.Verse;

// TODO: Auto-generated Javadoc
/**
 * The Class Presentation.
 */
@SuppressWarnings("serial")
@Entity
public class Presentation implements Serializable {

	/** The id. */
	private Long id;

	/** The song. */
	private Song song;

	/** The schedule. */
	private Schedule schedule;

	/** The slides. */
	private List<Slide> slides;

	/** The background. */
	private Background background;

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
	 * Gets the song.
	 * 
	 * @return the song
	 */
	@OneToOne(optional = true)
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

	/**
	 * Gets the schedule.
	 * 
	 * @return the schedule
	 */
	@ManyToOne(optional = false)
	public Schedule getSchedule() {
		return schedule;
	}

	/**
	 * Sets the schedule.
	 * 
	 * @param schedule
	 *            the new schedule
	 */
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	/**
	 * Gets the slides.
	 * 
	 * @return the slides
	 */
	@OneToMany(mappedBy = "presentation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
	public List<Slide> getSlides() {
		return slides;
	}

	/**
	 * Sets the slides.
	 * 
	 * @param slides
	 *            the new slides
	 */
	public void setSlides(List<Slide> slides) {
		this.slides = slides;
	}

	/**
	 * Gets the background.
	 * 
	 * @return the background
	 */
	@OneToOne(optional = true)
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
}
