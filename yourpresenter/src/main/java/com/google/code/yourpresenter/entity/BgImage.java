package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

/**
 * The Class BgImage.
 */
@SuppressWarnings("serial")
@Entity
@ToString (exclude={"media"})
@EqualsAndHashCode (exclude={"id", "media"})
@NoArgsConstructor
public class BgImage implements Serializable {

	/** The id. */
	@Id
	@GeneratedValue
	@Index(name = "BgImageIdIdx")
	private Long id;

	/** The image. */
	@JsonIgnore
	private String image;

	/** The thumbnail. */
	@JsonIgnore
	private String thumbnail;

	@JsonIgnore
//	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@Index(name = "BgImageMediaIdx")
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", insertable = false, updatable = false, nullable = true, unique = false)
	private Media media;
	
	@Column(name = "bgimage_position", insertable=false, updatable=false)
    private int bgImagePosition;
	
	public BgImage(String image, Media media) {
		this.setImage(image);
		this.setMedia(media);
	}

	/**
	 * @return the media
	 */
	public Media getMedia() {
		return media;
	}
	
	public void setMedia(Media media) {
		this.media = media;
	}

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
	 * Gets the image.
	 * 
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Sets the image.
	 * 
	 * @param image
	 *            the new image
	 */
	public void setImage(String image) {
		this.image = image;
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

	public int getBgImagePosition() {
		return bgImagePosition;
	}

	public void setBgImagePosition(int bgImagePosition) {
		this.bgImagePosition = bgImagePosition;
	}

}
