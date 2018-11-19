package com.example.kylo.musemusic;

public class Track {
    private String trackId;
    private String artistId;
    private String albumId;
    private int playbackInSeconds;
    private String trackName;
    private String trackArtist;
    private String previewUrl;
    private String albumUrl;

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
