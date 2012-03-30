package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
public class MediaMiscImage implements Serializable {

	/** The id. */
	private Long id;

	private MediaMisc mediaMisc;

	private BgImage bgImage;

	public MediaMiscImage() {
	}

	public MediaMiscImage(MediaMisc mediaMisc, BgImage bgImage) {
		super();
		this.mediaMisc = mediaMisc;
		this.bgImage = bgImage;
	}

	/**
	 * @return the mediaMisc
	 */
	@ManyToOne(optional = false)
	@Index(name = "MediaMiscImageMediaMiscIdx")
	public MediaMisc getMediaMisc() {
		return mediaMisc;
	}

	/**
	 * @param mediaMisc
	 *            the mediaMisc to set
	 */
	public void setMediaMisc(MediaMisc mediaMisc) {
		this.mediaMisc = mediaMisc;
	}

	/**
	 * @return the id
	 */
	@Id
	@Index(name = "MediaMiscImageIdIdx")
	@GeneratedValue
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
	 * @return the bgImage
	 */
	// these 2 are very tight
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@Index(name = "MediaMiscImageBgImageIdx")
	public BgImage getBgImage() {
		return bgImage;
	}

	/**
	 * @param bgImage
	 *            the bgImage to set
	 */
	public void setBgImage(BgImage bgImage) {
		this.bgImage = bgImage;
	}
}
