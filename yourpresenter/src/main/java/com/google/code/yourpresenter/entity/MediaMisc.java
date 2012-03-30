package com.google.code.yourpresenter.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
public class MediaMisc implements Serializable {

	/** The id. */
	private Long id;

	/** The name. */
	private String name;

	private String original;
	
	private List<MediaMiscImage> mediaMiscImages;

	public MediaMisc() {
	}
	
	/**
	 * @param name
	 * @param original
	 */
	public MediaMisc(String name, String original) {
		super();
		this.name = name;
		this.original = original;
	}
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Index(name = "MediaMiscIdIdx")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@NotNull
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mediaMiscImages
	 */
	// keep in mind to delete prphans:
	// http://javablog.co.uk/2009/12/27/onetomany-fixes-in-jpa-2/
	@OneToMany(mappedBy = "mediaMisc", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	public List<MediaMiscImage> getMediaMiscImages() {
		return mediaMiscImages;
	}

	/**
	 * @param mediaMiscImages
	 *            the mediaMiscImages to set
	 */
	public void setMediaMiscImages(List<MediaMiscImage> mediaMiscImages) {
		this.mediaMiscImages = mediaMiscImages;
	}

	/**
	 * @param mediaMiscImages
	 *            the mediaMiscImages to set
	 */
	public void addMediaMiscImage(MediaMiscImage mediaMiscImage) {
		if (null == mediaMiscImages) {
			mediaMiscImages = new ArrayList<MediaMiscImage>();
		}
		this.mediaMiscImages.add(mediaMiscImage);
	}
	
	/**
	 * @return the original
	 */
	public String getOriginal() {
		return original;
	}

	/**
	 * @param original the original to set
	 */
	public void setOriginal(String original) {
		this.original = original;
	}

	/**
	 * Returns the 1.st mediaMiscImage from the mediaMiscImages.
	 */
	@Transient
	public MediaMiscImage getCoverArt() {
		if (getMediaMiscImages().size() == 0) {
			return null;
		}
		return getMediaMiscImages().get(0);
	}

}
