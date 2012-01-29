package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.service.ISongService;

@Component("songTableView")
@Scope(WebApplicationContext.SCOPE_SESSION)
@SuppressWarnings("serial")
public class SongTableView implements Serializable {

	private ISongService songService;
	
	private List<Song> songs;

	private long currentSongId;
	
	@Autowired
	public SongTableView(ISongService songService) {
		this.songService = songService;
	}

	public List<Song> getSongs() {
		songs = this.songService.findByPattern("*");
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public void delete() {
		songService.deleteById(getCurrentSongId());
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
	}

	/**
	 * Returns the name of currently selected song.
	 * 
	 * @return
	 */
	public String getCurrentSongName() {
		return songService.findNameById(getCurrentSongId());
	}
}
