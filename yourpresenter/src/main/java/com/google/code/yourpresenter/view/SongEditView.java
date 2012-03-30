package com.google.code.yourpresenter.view;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.service.ISongService;


@Component("songEditView")
@Scope(WebApplicationContext.SCOPE_REQUEST)
@SuppressWarnings("serial")
public class SongEditView implements Serializable {
	
	private long currentSongId;
	
	private Song song = new Song();
	private ISongService songService;

	@Autowired
	public SongEditView(ISongService songService ) {
		this.songService = songService;
	}
	
	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}
	
	public void update() {
		songService.update(song);
	}

	/**
	 * @return the currentSongId
	 */
	public long getCurrentSongId() {
		return currentSongId;
	}

	/**
	 * @param currentSongId the currentSongId to set
	 */
	public void setCurrentSongId(long currentSongId) {
		this.currentSongId = currentSongId;
		// moreover find the song based on it's id
		this.song = this.songService.findById(getCurrentSongId());
	}
}
