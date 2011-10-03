package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.service.ISongService;

@Component("songTableView")
@Scope("session")
@SuppressWarnings("serial")
public class SongTableView implements Serializable {

	private ISongService songService;
	
	private List<Song> songs;
	
	private Song selectedSong;
	
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

	public Song getSelectedSong() {
		return selectedSong;
	}

	public void setSelectedSong(Song selectedSong) {
		this.selectedSong = selectedSong;
	}
	
}
