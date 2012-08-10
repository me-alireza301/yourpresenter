package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Preference implements Serializable {
	
	/** The name. */
	private String name;

	/**
	 * The comma separated values.
	 * (can't be called values as it causes problems in JPA table creation)
	 */
	private String value;

	public Preference(String name, String value) {
		super();
		this.setName(name);
		this.setValue(value);
	}
	
	@Id
	@Index(name="PreferenceNameIdx")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}	
