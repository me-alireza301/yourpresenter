package com.google.code.yourpresenter.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;

/**
 * The Class Presentation.
 */
@SuppressWarnings("serial")
@Entity
@ToString(exclude = "schedule")
@EqualsAndHashCode(exclude = { "id", "schedule" })
@NoArgsConstructor
public class Presentation implements Serializable {

	/** The id. */
	@Id
	@GeneratedValue
	@Index(name = "PresentationIdIdx")
	private Long id;

	/** The song. */
	@JsonIgnore
	@OneToOne(optional = true)
	@Index(name = "PresentationSongIdx")
	private Song song;

	@JsonIgnore
	@OneToOne(optional = true)
	@Index(name = "PresentationMediaIdx")
	private Media media;

	/** The schedule. */
	@JsonIgnore
//	@ManyToOne(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", insertable = false, updatable = false, nullable = true, unique = false)
	private Schedule schedule;

	/** The slides. */
//	@OneToMany(mappedBy = "presentation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//	@OrderColumn(name = "possition")
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn (name = "PRESENTATION_ID")
//	@IndexColumn(name = "SLIDE_POSITION")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true) 
    @IndexColumn(name="slide_position", base=0)
    @JoinColumn(name="presentation_id", nullable=false)
	private List<Slide> slides;

	/** The background. */
	@JsonIgnore
	@OneToOne(optional = true)
	@Index(name = "PresentationBgImageIdx")
	private BgImage bgImage;

	@NotNull
	private String name;
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
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

	public void addSlide(Slide slide) {
		if (null == this.slides) {
			this.slides = new ArrayList<Slide>();
		}

		this.slides.add(slide);
	}

	/**
	 * @return the media
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * @param media
	 *            the mediaMisc to set
	 */
	public void setMedia(Media media) {
		this.media = media;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
