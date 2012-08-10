package com.google.code.yourpresenter.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

import com.google.code.yourpresenter.IConstants;

@SuppressWarnings("serial")
@Entity
@ToString (exclude={"medias"})
@EqualsAndHashCode (exclude={"id", "medias"})
@NoArgsConstructor
public class MediaType implements Serializable {

	/** The id. */
	private Long id;

	private String name;
	
	@JsonIgnore
	private List<Media> medias;

	/**
	 * @param id
	 * @param name
	 */
	public MediaType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Id
	@Index(name = "BgImageTypeIdIdx")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "type", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	public List<Media> getMedias() {
		return medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}

	@Transient
	public boolean isBgImageReplacible() {
		return !IConstants.MEDIA_TYPE_MISC.equals(this.getName());
	}
}
