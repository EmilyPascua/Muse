package com.example.kylo.musemusic;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    public static ArrayList<Track> parseNews (String JSONString){
        ArrayList<Track> trackList = new ArrayList<>();
        Log.d("track", JSONString);
        try{
            JSONObject mainJSONObject = new JSONObject(JSONString);
            JSONArray data = mainJSONObject.getJSONArray("data");

            for(int i = 0; i < data.length(); i ++){
                JSONObject item = data.getJSONObject(i);

                //RawtrackId returns tra.Id, I just want the Id so I parse it.
                String rawTrackId = item.getString("id");
                int trackId = Integer.parseInt(rawTrackId.substring(rawTrackId.lastIndexOf(".") + 1));

                //RawAlbumId returns alb.Id, I just want the Id so I parse it.
                String rawAlbumId = item.getString("albumId");
                int albumId = Integer.parseInt(rawAlbumId.substring(rawAlbumId.lastIndexOf(".") + 1));

                //RawArtistId returns art.Id, I just want the Id so I parse it.
                String rawArtistId = item.getString("artistId");
                int artistId = Integer.parseInt(rawArtistId.substring(rawArtistId.lastIndexOf(".") + 1));

                int playbackInSeconds = item.getInt("playbackSeconds");
                String trackName = item.getString("name");
                String trackArtist = item.getString("artistName");
                String previewUrl = item.getString("previewURL");

                trackList.add(new Track( trackId, albumId, artistId, playbackInSeconds, trackName, trackArtist, previewUrl));
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
        return trackList;
    }
}
