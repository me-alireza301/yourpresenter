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

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.service.ISongService;

@Component("songTableView")
@Scope(WebApplicationContext.SCOPE_SESSION)
@SuppressWarnings("serial")
public class SongTableView implements Serializable {

	@Autowired
	private ISongService songService;

	private Song editedSong;
	private Collection<Object> selection;
	private List<Song> selectedSongs = new ArrayList<Song>();

	public List<Song> getSongs() {
		return this.songService.findAll();
	}

	public void delete() throws YpException {
		Song song = getSelectedSong();
		if (null == song) {
			throw new YpException(YpError.SONG_DELETE_FAILED, "Song to be deleted is null!");
		}
		
		songService.delete(song);
		clearState();
	}
	
	private void clearState() {
		// make sure we get rid of selection
		selectedSongs.clear();
		selection = null;
		editedSong = null;
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
//		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Song saved : " + editedSong.getName()));
		
		// make sure we get rid of state
		clearState();
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

	public void resetEditedSong() {
		clearState();
	}
}
