package com.jsvana.music.songs;

import java.util.ArrayList;

/**
 * Created by jsvana on 19/1/14.
 */
public class SongManager {
    private ArrayList<PlayedSong> songs;

    public SongManager() {
        songs = new ArrayList<PlayedSong>();
    }

    public boolean hasCurrentSong() {
        return songs.size() >= 1;
    }

    public PlayedSong getCurrentSong() {
        if (!hasCurrentSong()) {
            return null;
        }

        return songs.get(songs.size() - 1);
    }

    public void pause() {
        if (hasCurrentSong()) {
            getCurrentSong().pause();
        }
    }

    public void resume() {
        if (hasCurrentSong()) {
            getCurrentSong().resume();
        }
    }

    public boolean hasPreviousSong() {
        return songs.size() >= 2;
    }

    public PlayedSong getPreviousSong() {
        if (!hasPreviousSong()) {
            return null;
        }

        return songs.get(songs.size() - 2);
    }

    public boolean addSong(long duration, String artist, String album, String track, long albumID) {
        PlayedSong song = new PlayedSong(duration, artist, album, track, albumID);
        if (hasCurrentSong()) {
            PlayedSong prevSong = getCurrentSong();
            if (!prevSong.equals(song)) {
                prevSong.setEndTime();
                songs.add(song);
                return true;
            }
        } else {
            songs.add(song);
            return true;
        }

        return false;
    }

    public ArrayList<PlayedSong> getPlayedSongs() {
        return songs;
    }
}
