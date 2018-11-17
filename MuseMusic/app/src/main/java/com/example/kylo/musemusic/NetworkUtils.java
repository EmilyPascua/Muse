package com.example.kylo.musemusic;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    //Base URL, the query search is always going to be a track (even if it's from the database)
    private static final String MUSIC_BASE_URL = "http://api.napster.com/v2.2/search";

    //API Key
    private static final String API_KEY = "Yjc0MjQ5ODItMDA5ZC00NzlhLWFjNjAtZTgyYTVkOWNlNmVi";

    //Query Paramters
    final static String API_KEY_PARAM = "apikey";

    //Name of Track
    final static String QUERY_PARAM = "query";

    public static URL buildUrl(String query){
        Log.e(" Query", query);

        Uri builtUri = Uri.parse(MUSIC_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(QUERY_PARAM, query)
                .appendQueryParameter("type", "track")
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        Log.e("Built Query", url.toString());
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else
                return null;
        }finally {
            urlConnection.disconnect();
        }
    }
}
