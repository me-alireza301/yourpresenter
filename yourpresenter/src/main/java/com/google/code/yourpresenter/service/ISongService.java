package com.google.code.yourpresenter.service;

import java.util.List;

import com.google.code.yourpresenter.entity.Song;

/**
 * The Interface ISongService.
 */
public interface ISongService {

    /**
     * Find songs.
     * 
     * @param pattern the pattern
     * @return the list
     */
    public List<Song> findByPattern(String pattern);

    /**
     * Find song by id.
     * 
     * @param id the id
     * @return the song
     */
    public Song findById(Long id);

    /**
     * Persist song.
     * 
     * @param song the song
     */
    public void persist(Song song);

    /**
     * Delete song.
     * 
     * @param song the song
     */
    public void delete(Song song);

    /**
     * Creates new or edits existing song.
     * 
     * @param id the id
     * @return the song
     */
    public Song createOrEditSong(Long id);

	public int deleteAll();
}
