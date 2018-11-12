package com.example.kylo.musemusic;

public class Track {
    private int trackId;
    private int artistId;
    private int playbackInSeconds;
    private String trackName;
    private String trackArtist;
    private String previewUrl;
    private String albumUrl;

    public Track(int trackId, int albumId,  int artistId, int playbackInSeconds, String trackName, String trackArtist, String previewUrl){
        //Use the artId to get the album Url picture
        this.trackId = trackId;
        this.playbackInSeconds = playbackInSeconds;
        this.trackName = trackName;
        this.trackArtist = trackArtist;
        this.previewUrl = previewUrl;
        this.albumUrl = "http://direct.napster.com/imageserver/v2/albums/Alb."+ albumId + "/images/500x500.jpg";
        this.artistId = artistId;
    }

    private int getTrackId(){
        return trackId;
    }

    private void setTrackId(int trackId){
        this.trackId = trackId;
    }

    private int getPlaybackInSeconds(){
        return playbackInSeconds;
    }

    private void setPlaybackInSeconds(int playbackInSeconds){
        this.playbackInSeconds = playbackInSeconds;
    }

    private String getTrackName(){
        return trackName;
    }

    private void setTrackName(String trackName){
        this.trackName = trackName;
    }

    private String getTrackArtist(){
        return trackArtist;
    }

    private void setTrackArtist(String trackArtist){
     this.trackArtist = trackArtist;
    }


    private String getPreviewUrl(){
        return previewUrl;
    }

    private void setPreviewUrl(String previewUrl){
        this.previewUrl = previewUrl;
    }

    private String getAlbumUrl(){
        return albumUrl;
    }

    private void setAlbumUrl(String albumUrl){
        this.albumUrl = albumUrl;
    }

}
