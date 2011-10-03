package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;


// TODO: Auto-generated Javadoc
/**
 * The Class Background.
 */
@SuppressWarnings("serial")
@Entity
public class Background implements Serializable {

	/** The id. */
	private Long id;

	/** The bg image. */
	private BgImage bgImage;

	/** The thumbnail. */
	private String thumbnail;

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
	 * Gets the bg image.
	 * 
	 * @return the bg image
	 */
	@OneToOne
	public BgImage getBgImage() {
		return bgImage;
	}

	/**
	 * Sets the bg image.
	 * 
	 * @param bgImage
	 *            the new bg image
	 */
	public void setBgImage(BgImage bgImage) {
		this.bgImage = bgImage;
	}

	/**
	 * Gets the thumbnail.
	 * 
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * Sets the thumbnail.
	 * 
	 * @param thumbnail
	 *            the new thumbnail
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}
