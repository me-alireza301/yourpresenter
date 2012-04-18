package com.google.code.yourpresenter.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

/**
 * The Class Song.
 */
@SuppressWarnings("serial")
@Entity
public class Song implements Serializable {

	@JsonIgnore
	@Transient
	private transient static final String NEW_VERSE = "\\s*((slide)|(verse))\\s*\\d*\\s*";
	@JsonIgnore
	@Transient
	private transient static final String HTML_LINE_SEPARATOR = "<br/>";

	/** The id. */
	private Long id;

	/** The name. */
	private String name;

	/** The verses. */
	private List<Verse> verses;

	/** The text without punctuation. */
	private String noPunctuationText;

	private/* transient */String text;

	public Song() {
	}

	public Song(String name, String text) {
		this.setName(name);
		this.setText(text);
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Index(name = "SongIdIdx")
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
	 * Gets the text.
	 * 
	 * @return the text
	 */
	// @NotNull
	// keep in mind to delete orphans:
	// http://javablog.co.uk/2009/12/27/onetomany-fixes-in-jpa-2/
	@OneToMany(mappedBy = "song", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	public List<Verse> getVerses() {
		return verses;
	}

	/**
	 * Sets the verses.
	 * 
	 * @param verses
	 *            the new verses
	 */
	public Song setVerses(List<Verse> verses) {
		this.verses = verses;
		return this;
	}

	/**
	 * Gets the text without punctuation.
	 * 
	 * @return the text without punctuation
	 */
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "VARCHAR(1000)")
	@NotNull
	public String getNoPunctuationText() {
		return noPunctuationText;
	}

	/**
	 * Sets the text without punctuation.
	 * 
	 * @param noPtext
	 *            the new text without punctuation
	 */
	public void setNoPunctuationText(String noPtext) {
		this.noPunctuationText = noPtext;
	}

	// to prevent: java.sql.SQLException: data exception: string data, right
	// truncation at
	// http://stackoverflow.com/questions/7565280/hsqlexception-data-exception
	// JPA:
	// http://stackoverflow.com/questions/2290727/jpa-hibernate-ddl-generation-char-vs-varchar
	@Column(columnDefinition = "VARCHAR(1000)")
	@Size(max = 1000)
	@NotNull
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
		this.verses = parseText();
		this.noPunctuationText = this.text;
	}

	@Transient
	private boolean isTextEmpty() {
		return null == getText() || getText().trim().isEmpty();
	}

	protected List<Verse> parseText() {
		if (isTextEmpty()) {
			return null;
		}

		List<Verse> verses = new ArrayList<Verse>();
		BufferedReader reader = new BufferedReader(new StringReader(getText()));

		try {
			StringBuilder txt = new StringBuilder();
			String line;

			while (null != (line = reader.readLine())) {
				// for non-empty lines
				if (!line.trim().isEmpty()) {
					// new slide indicator
					if (line.toLowerCase().matches(NEW_VERSE)) {
						// add verse only in case something to be added already
						// read
						if (txt.length() > 0) {
							// remove unneeded last HTML_LINE_SEPARATOR
							txt.setLength(txt.length()
									- HTML_LINE_SEPARATOR.length());

							// add verse read
							verses.add(new Verse(txt.toString().trim(), this));
							txt.setLength(0);
						}

						// regular text case
					} else {
						txt.append(line.trim()).append(HTML_LINE_SEPARATOR);
					}

					// there might be explicitelly set empty line within verse
					// => preserve it
				} else if (0 < txt.length()) {
					txt.append(line.trim()).append(HTML_LINE_SEPARATOR);
				}
			}

			if (0 < txt.length()) {
				// remove unneeded last HTML_LINE_SEPARATOR
				txt.setLength(txt.length() - HTML_LINE_SEPARATOR.length());

				// add the last verse read
				verses.add(new Verse(txt.toString().trim(), this));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return verses;
	}

}
