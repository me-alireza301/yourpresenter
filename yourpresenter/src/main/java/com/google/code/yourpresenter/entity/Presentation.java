package com.google.code.yourpresenter.entity;

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
import javax.persistence.OrderColumn;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;


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
	@JsonIgnore
	private Song song;

	/** The schedule. */
	@JsonIgnore
	private Schedule schedule;

	/** The slides. */
	private List<Slide> slides;

	/** The background. */
	@JsonIgnore
	private BgImage bgImage;

	@JsonIgnore
	// in case of correct one: position => java.sql.SQLException: Table not found in statement [select schedule0_.name ...
	// seems like position is reserved word => can't be used
	// see: http://stackoverflow.com/questions/1442127/table-not-found-with-hibernate-and-hsqldb
	private int possition;
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Index(name="PresentationIdIdx")
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
	@Index(name="PresentationSongIdx")
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
	@Index(name="PresentationScheduleIdx")
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
	@OrderColumn(name = "possition")
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
	@Index(name="PresentationBgImageIdx")
	public BgImage getBgImage() {
		return bgImage;
	}

	/**
	 * Sets the background.
	 * 
	 * @param background
	 *            the new background
	 */
	public void setBgImage(BgImage background) {
		this.bgImage = background;
	}

	public int getPossition() {
		return possition;
	}

	public void setPossition(int position) {
		this.possition = position;
	}
	
	public void increasePossition() {
		this.possition++;
	}

	public void addSlide(Slide slide) {
		if (null == this.slides) {
			this.slides = new ArrayList<Slide>();	
		}
		
		this.slides.add(slide);
	}
}
