package com.cafemanager.musefoundation.Share;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.cafemanager.musefoundation.Model.Track;
import com.cafemanager.musefoundation.R;
import com.cafemanager.musefoundation.Utils.NetworkUtils;
import com.cafemanager.musefoundation.Utils.api.JsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private  static  final String TAG = "ShareActivity";
    private Context mContext = ShareActivity.this;
    private static  final int ACTIVITY_NUM = 2;
    private static final int LOADER_ID = 1;
    private static final String SEARCH_QUERY_URL_EXTRA = "searchQuery";
    private static final String SEARCH_QUERY_RESULTS = "searchResults";

    private RecyclerView mRecyclerView;
    private MusicAdapter mAdapter;
    private EditText mSearchBoxEditText;
    private ProgressBar mProgressBar;

    private String musicQueryResults;
    private ArrayList<Track> tracks = new ArrayList<>();

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState){
        Log.d(TAG, "onCreate: started.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mAdapter = new MusicAdapter(this, tracks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(savedInstanceState != null && savedInstanceState.containsKey(SEARCH_QUERY_RESULTS)){
            tracks.clear();
            String searchResults = savedInstanceState.getString(SEARCH_QUERY_RESULTS);
            populateRecyclerView(searchResults);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY_RESULTS, musicQueryResults);

    }

    private URL makeSearchUrl() {
        String napsterQuery = mSearchBoxEditText.getText().toString();
        URL napsterSearchUrl = NetworkUtils.buildUrl(napsterQuery);
        return napsterSearchUrl;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            URL url = makeSearchUrl();
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_QUERY_URL_EXTRA, url.toString());
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> searchLoader = loaderManager.getLoader(LOADER_ID);
            if(searchLoader == null){
                loaderManager.initLoader(LOADER_ID, bundle, this).forceLoad();
            }else{
                loaderManager.restartLoader(LOADER_ID, bundle, this).forceLoad();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_submit, menu);
        return true;
    }

    public void populateRecyclerView(String searchResults){
        Log.d("mycode", searchResults);
        tracks = JsonUtils.parseNews(searchResults);
        mAdapter.mTrack.clear();
        mAdapter.mTrack.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                Log.d(TAG, "onStartLoading called");
                super.onStartLoading();
                if(args == null){
                    Log.d(TAG, "bundle null");
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public String loadInBackground() {
                Log.d(TAG, "loadInBackground called");

                String napsterSearchQuery = args.getString(SEARCH_QUERY_URL_EXTRA);
                if(napsterSearchQuery == null || napsterSearchQuery.isEmpty()){
                    return null;
                }
                try {
                    Log.d(TAG, "begin network call");
                    musicQueryResults = NetworkUtils.getResponseFromHttpUrl(new URL(napsterSearchQuery));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, musicQueryResults);
                return musicQueryResults;
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        Log.d("mycode", data);
        mProgressBar.setVisibility(View.GONE);
        populateRecyclerView(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {}
}