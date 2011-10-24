package com.google.code.yourpresenter.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

/**
 * The Class Schedule.
 */
@SuppressWarnings("serial")
@Entity
public class Schedule implements Serializable {

	/** The presentations. */
	private List<Presentation> presentations;

	/** The background. */
	private BgImage bgImage;

	/** The name. */
	private String name;

	public Schedule() {
		this(null);
	}
	
	public Schedule(String name) {
		super();
		this.name = name;	
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@Id
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
	 * Gets the presentations.
	 * 
	 * @return the presentations
	 */
	// if having FetchType.EAGER => got exception: 
	// Caused by: org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags 
	@OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderColumn(name = "possition")
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
	@OneToOne
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
}
