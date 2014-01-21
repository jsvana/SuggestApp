package com.jsvana.music;

/**
 * Created by jsvana on 19/1/14.
 */
public class Recommendation {
    private String album;
    private String artist;
    private int artID;

    public Recommendation(String album, String artist, int artID) {
        this.album = album;
        this.artist = artist;
        this.artID = artID;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public int getArtID() {
        return artID;
    }
}
