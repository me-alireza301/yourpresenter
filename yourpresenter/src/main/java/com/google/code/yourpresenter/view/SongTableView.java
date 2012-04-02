package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;

import org.richfaces.component.UIExtendedDataTable;
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

	private Song editedSong;
	private List<Song> songs;
	private Collection<Object> selection;
	private List<Song> selectedSongs = new ArrayList<Song>();

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
		songService.deleteById(getSelectedSong().getId());
	}

	public Collection<Object> getSelection() {
		return selection;
	}

	public void setSelection(Collection<Object> selection) {
		this.selection = selection;
	}

	public Song getSelectedSong() {
		if (selectedSongs == null || selectedSongs.isEmpty()) {
			return null;
		}
		return selectedSongs.get(0);
	}

	public void update() {
		songService.update(editedSong);
		editedSong = null;
	}

	public void selectionListener(AjaxBehaviorEvent event) {
		UIExtendedDataTable dataTable = (UIExtendedDataTable) event
				.getComponent();
		Object originalKey = dataTable.getRowKey();
		selectedSongs.clear();
		for (Object selectionKey : selection) {
			dataTable.setRowKey(selectionKey);
			if (dataTable.isRowAvailable()) {
				selectedSongs.add((Song) dataTable.getRowData());
			}
		}
		dataTable.setRowKey(originalKey);
	}

	public Song getEditedSong() {
		if (null != getSelectedSong()) {
			editedSong = getSelectedSong();
		} else if (null == editedSong) {
			editedSong = new Song();
		}

		return editedSong;
	}

	public void setEditedSong(Song editedSong) {
		this.editedSong = editedSong;
	}

	public void setEditedSong(String editedSong) {
		this.editedSong = null;
	}

	public void setResetEditedSong(String editedSong) {
		this.editedSong = null;
	}
}
