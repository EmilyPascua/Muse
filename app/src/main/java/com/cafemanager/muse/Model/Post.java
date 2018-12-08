package com.cafemanager.muse.Model;

public class Post {

    /**
     Not sure if all instance variables should hold Strings.
     This is temporary.
     */
    private String artist_name;
    private String song_name;
    private String album_image;
    private String song_preview;
    private String post_description;

    public Post(){}

    public Post(String artistName, String songName, String albumImgUrl, String songPreview, String description) {
        artist_name = artistName;
        song_name = songName;
        album_image = albumImgUrl;
        song_preview = songPreview;
        post_description = description;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getAlbum_image() {
        return album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public String getSong_preview() {
        return song_preview;
    }

    public void setSong_preview(String song_preview) {
        this.song_preview = song_preview;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }
}