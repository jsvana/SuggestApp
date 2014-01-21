package com.jsvana.music.songs;

import android.util.Log;

import java.util.Date;

/**
 * Created by jsvana on 19/1/14.
 */
public class PlayedSong {
    private long duration;
    private String artist;
    private String album;
    private String track;
    private long albumID;
    private long startTime;
    private long endTime;
    private boolean skipped;
    private long elapsed;

    public PlayedSong(long duration, String artist, String album, String track, long albumID) {
        this.duration = duration;
        this.artist = artist;
        this.album = album;
        this.track = track;
        this.albumID = albumID;
        startTime = (new Date()).getTime();
        elapsed = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof PlayedSong)) {
            return false;
        }

        PlayedSong song = (PlayedSong)o;
        return song.duration == this.duration && song.artist.equals(this.artist)
            && song.album.equals(this.album) && song.track.equals(this.track)
            && song.albumID == this.albumID;
    }

    public void pause() {
        elapsed += (new Date()).getTime() - startTime;
    }

    public void resume() {
        startTime = (new Date()).getTime();
    }

    public void setEndTime() {
        endTime = (new Date()).getTime();
        if (endTime - startTime + elapsed <= duration + 10000
                && endTime - startTime + elapsed >= duration - 10000) {
            skipped = false;
        } else {
            skipped = true;
        }
        elapsed += (endTime - startTime);
    }

    public long getElapsed() {
        return elapsed;
    }

    public long getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTrack() {
        return track;
    }

    public long getAlbumID() {
        return albumID;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean getSkipped() {
        return skipped;
    }
}
