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
 * The Class Schedule.
 */
@SuppressWarnings("serial")
@Entity
@ToString (exclude="presentations")
@EqualsAndHashCode
@NoArgsConstructor
public class Schedule implements Serializable {

	@Id
	@GeneratedValue
	@Index(name = "ScheduleIdIdx")
	private Long id; 
	
	/** The presentations. */
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true) 
    @IndexColumn(name="presentation_position", base=0)
    @JoinColumn(name="schedule_id", nullable=false)
	private List<Presentation> presentations;

	/** The background. */
	@JsonIgnore
	@OneToOne
	@Index(name = "ScheduleBgImageIdx")
	private BgImage bgImage;

	/** The name. */
	@Index(name = "ScheduleNameIdx")
	@NotNull
	private String name = null;

	private boolean blank = false;
	private boolean clear = false;
	private boolean live = false;

	public Schedule(String name) {
		super();
		this.blank = false;
		this.clear = false;
		this.live = false;
		this.name = name;
	}

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
	 * Gets the presentations.
	 * 
	 * @return the presentations
	 */
	public List<Presentation> getPresentations() {
		return presentations;
	}

	/**
	 * Sets the presentations.
	 * 
	 * @param presentations
	 *            the new presentations
	 */
	public void setPresentations(List<Presentation> presentations) {
		this.presentations = presentations;
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

	public Presentation addPresentation(Presentation presentation) {
		if (null == this.presentations) {
			this.presentations = new ArrayList<Presentation>();
		}

		this.presentations.add(presentation);
		return presentation;
	}

	public boolean isBlank() {
		return blank;
	}

	public void setBlank(boolean blank) {
		this.blank = blank;
	}

	public boolean isClear() {
		return clear;
	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
}
