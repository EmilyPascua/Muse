package com.example.kylo.musemusic;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SEARCH_QUERY = "searchQuery";
    private static final String SEARCH_RESULTS = "searchResults";
    public TextView textView;
    public Button button;
    private EditText mSearchBoxEditText;
    private String musicSearchResults;

    //Make the Search into a URL
    private URL makeSearchUrl() {
        String trackSearchQuery = mSearchBoxEditText.getText().toString();
        URL trackQueryUrl = NetworkUtils.buildUrl(trackSearchQuery);
        return trackQueryUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.preview_json);
        MusicTask task = new MusicTask();
        task.execute();
    }

    public class MusicTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "Calling doInBackground");
            String query = "Numb";
            String type = "track";
            URL trackRequestUrl = NetworkUtils.buildUrl(query);

            try{
                String jsonTrackResponse = NetworkUtils.getResponseFromHttpUrl(trackRequestUrl);
                return jsonTrackResponse;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonTrackResponse) {
            textView.setText(jsonTrackResponse);
        }
    }

}
