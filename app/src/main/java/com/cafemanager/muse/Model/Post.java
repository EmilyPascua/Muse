package com.cafemanager.muse.Models;

public class Post {

    /**
     Not sure if all instance variables should hold Strings.
     This is temporary.
     */
    private String mArtistName;
    private String mSongName;
    private String mAlbumImgUrl;
    private String mSongPreview;
    private String mDescription;

    public Post(){}

    public Post(String artistName, String songName, String albumImgUrl, String songPreview, String description) {
        mArtistName = artistName;
        mSongName = songName;
        mAlbumImgUrl = albumImgUrl;
        mSongPreview = songPreview;
        mDescription = description;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
    }

    public String getAlbumImgUrl() {
        return mAlbumImgUrl;
    }

    public void setAlbumImgUrl(String albumImgUrl) {
        mAlbumImgUrl = albumImgUrl;
    }

    public String getSongPreview() {
        return mSongPreview;
    }

    public void setSongPreview(String songPreview) {
        mSongPreview = songPreview;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}