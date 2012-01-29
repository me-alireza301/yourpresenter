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
     * @return 
     */
    public int delete(Song song);

	public int deleteAll();

	/**
	 * Deletes song by it's Id.
	 * 
	 * @param songId id of the song to delete.
	 * @return 
	 */
	public int deleteById(long songId);

	
	/**
	 * Finds song name by id.
	 * 
	 * @param id
	 * @return
	 */
	public String findNameById(Long id);
}
