package com.cafemanager.muse.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Track {
    private String trackId;
    private String artistId;
    private String albumId;
    private int playbackInSeconds;
    private String trackName;
    private String trackArtist;
    private String previewUrl;
    private String albumUrl;

    public Track(){}


    public Track(String trackId, String albumId,  String artistId, int playbackInSeconds, String trackName, String trackArtist, String previewUrl){
        //Use the artId to get the album Url picture
        this.trackId = trackId;
        this.playbackInSeconds = playbackInSeconds;
        this.trackName = trackName;
        this.trackArtist = trackArtist;
        this.previewUrl = previewUrl;
        this.albumUrl = "http://direct.napster.com/imageserver/v2/albums/"+ albumId + "/images/500x500.jpg";
        this.artistId = artistId;
        this.albumId = albumId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("trackId", trackId);
        result.put("artistId", artistId);
        result.put("albumId", albumId);
        result.put("playbackInSeconds", playbackInSeconds);
        result.put("trackName", trackName);
        result.put("trackArtist", trackArtist);
        result.put("previewUrl", previewUrl);
        result.put("albumUrl", albumUrl);

        return result;
    }

    public String getTrackId(){
        return trackId;
    }

    public void setTrackId(String trackId){
        this.trackId = trackId;
    }

    public int getPlaybackInSeconds(){
        return playbackInSeconds;
    }

    public void setPlaybackInSeconds(int playbackInSeconds){
        this.playbackInSeconds = playbackInSeconds;
    }

    public String getTrackName(){
        return trackName;
    }

    public void setTrackName(String trackName){
        this.trackName = trackName;
    }

    public String getTrackArtist(){
        return trackArtist;
    }

    public void setTrackArtist(String trackArtist){
        this.trackArtist = trackArtist;
    }


    public String getPreviewUrl(){
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl){
        this.previewUrl = previewUrl;
    }

    public String getAlbumUrl(){
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl){
        this.albumUrl = albumUrl;
    }

    public String getArtistId(){
        return artistId;
    }

    public void setArtistId(String artistId){
        this.artistId = artistId;
    }

    public String getAlbumId(){
        return albumId;
    }

    public void setAlbumId(String albumId){ this.albumId = albumId;}

}
