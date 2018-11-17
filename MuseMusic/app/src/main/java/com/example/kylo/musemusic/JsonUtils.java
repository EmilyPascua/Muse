package com.example.kylo.musemusic;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    public static ArrayList<Track> parseNews (String JSONString){
        ArrayList<Track> trackList = new ArrayList<>();
        Log.e("track", JSONString);
        try{
            JSONObject mainJSONObject = new JSONObject(JSONString);
            JSONObject search = mainJSONObject.getJSONObject("search");
            JSONObject data = search.getJSONObject("data");
            JSONArray tracks = data.getJSONArray("tracks");
            for(int i = 0; i < tracks.length(); i++){
                JSONObject item = tracks.getJSONObject(i);
                int playbackInSeconds = item.getInt("playbackSeconds");
                String trackId = item.getString("id");
                String albumId = item.getString("albumId");
                String trackName = item.getString("name");
                String trackArtist = item.getString("artistName");
                String artistId = item.getString("artistId");
                String previewUrl = item.getString("previewURL");
                trackList.add(new Track( trackId, albumId, artistId, playbackInSeconds, trackName, trackArtist, previewUrl));


            }

        }catch(JSONException e){
            e.printStackTrace();
        }
        return trackList;
    }
}
