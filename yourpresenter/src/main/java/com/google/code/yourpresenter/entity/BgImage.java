package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	private Long id;

	/** The image. */
	@JsonIgnore
	private String image;

	/** The thumbnail. */
	@JsonIgnore
	private String thumbnail;

	@JsonIgnore
	private Media media;
	
	@JsonIgnore
	// in case of correct one: position => java.sql.SQLException: Table not found in statement [select schedule0_.name ...
	// seems like position is reserved word => can't be used
	// see: http://stackoverflow.com/questions/1442127/table-not-found-with-hibernate-and-hsqldb
	private int possition;
	
	public BgImage(String image, Media media, int position) {
		this.setImage(image);
		this.setMedia(media);
		this.setPossition(position);
	}

	/**
	 * @return the media
	 */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@Index(name = "BgImageMediaIdx")
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
	@Id
	@GeneratedValue
	@Index(name = "BgImageIdIdx")
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

	
	public int getPossition() {
		return possition;
	}

	public void setPossition(int position) {
		this.possition = position;
	}

}
