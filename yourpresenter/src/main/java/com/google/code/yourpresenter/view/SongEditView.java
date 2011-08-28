package com.google.code.yourpresenter.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.service.ISongService;


@Component("songEditView")
@Scope("request")
@SuppressWarnings("serial")
public class SongEditView implements Serializable {
	
	private Song song;
	private ISongService songService;

	@Autowired
	public SongEditView(ISongService songService ) {
		this.songService = songService;
	}
	
	@PostConstruct
	public void onLoad() {
		String songId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("songId");
		if (null == songId) {
			this.song = this.songService.createOrEditSong(null);
		} else {
			this.song = this.songService.createOrEditSong(Long.valueOf(songId));	
		}
		
	}
	
	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}
	
	public void update() {
		songService.persistSong(song);
//		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Song is updated successfully", "OK");
//		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

}
