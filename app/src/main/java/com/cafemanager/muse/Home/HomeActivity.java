package com.cafemanager.muse.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cafemanager.muse.Login.LoginActivity;
import com.cafemanager.muse.Model.Post;
import com.cafemanager.muse.Model.User;
import com.cafemanager.muse.R;
import com.cafemanager.muse.Utils.BottomNavigationViewHelper;
import com.cafemanager.muse.Utils.NetworkUtils;
import com.cafemanager.muse.Utils.api.JsonUtils;

//firebase imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import com.cafemanager.muse.Utils.MusicAdapter;
import com.cafemanager.muse.Model.Track;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    // check

    private Context mContext = HomeActivity.this;
    private  static  final String TAG = "HomeActivity";
    private static  final int ACTIVITY_NUM = 0;

    //music feed variables
    private ProgressBar mProgressBar;
    private static final int LOADER_ID = 1;
    private static final String SEARCH_QUERY_URL_EXTRA = "searchQuery";
    private static final String SEARCH_QUERY_RESULTS = "searchResults";
    private String musicQueryResults;
    private RecyclerView mRecyclerView;
    private MusicAdapter mAdapter;
    private List<String> mUserIds = new ArrayList<>();
    private List<Post> mPosts = new ArrayList<>();

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: starting.");

        //firebase auth and database ref
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        //music feed code
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mRecyclerView = (RecyclerView)findViewById(R.id.home_feed_view);
        mAdapter = new MusicAdapter(mContext, mPosts);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        if(savedInstanceState != null && savedInstanceState.containsKey(SEARCH_QUERY_RESULTS)){
//            mPosts.clear();
//            String searchResults = savedInstanceState.getString(SEARCH_QUERY_RESULTS);
//            populateRecyclerView(searchResults);
//        }

        setupBottomNavigationView();

        //listen for changes from database
        listenAndLoad();

//        setupViewPager();
    }



    public void listenAndLoad() {


            Query query = mDatabaseReference.child(getString(R.string.firebase_user_posts) );


            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChange:" + dataSnapshot.getKey());

                    for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                        for(DataSnapshot singlePostSnapshot : singleSnapshot.getChildren()) {

                            Post post = singlePostSnapshot.getValue(Post.class);
//
                            if (post != null) {
                                // Create Map (from your DataSnapshot) to get values you need easily
                                Map<String, Object> firebasePostMap = (HashMap<String, Object>) singlePostSnapshot.getValue();

                                // Configure Post object
                                post.setArtist_name(firebasePostMap.get(getString(R.string.firebase_artist_name)).toString());
                                post.setSong_name(firebasePostMap.get(getString(R.string.firebase_song_name)).toString());

                                post.setAlbum_image(firebasePostMap.get(getString(R.string.firebase_album_image)).toString());
                                post.setPost_description(firebasePostMap.get(getString(R.string.firebase_post_description)).toString());
                                post.setSong_preview(firebasePostMap.get(getString(R.string.firebase_song_preview)).toString());

                                mPosts.add(post);
                            }


                        }


                    }
                    mAdapter.setPosts(mPosts);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postTracks:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load tracks.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }



//    //adds the  3 tabs at the top: camera, home, and messages
//    private void setupViewPager(){
//        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        adapter.addFragment(new HomeFragment());  // index 0
//
//        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
//        viewPager.setAdapter(adapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//
//        tabLayout.getTabAt(0).setText("Feed");
////        tabLayout.getTabAt(0).setIcon(R.drawable.ic_instagramph);
//    }

    //BottomNavigationView setup
    private void setupBottomNavigationView(){

        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    //music code

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(SEARCH_QUERY_RESULTS, musicQueryResults);
//
//    }
//
//    private URL makeSearchUrl() {
//        String napsterQuery = mSearchBoxEditText.getText().toString();
//        URL napsterSearchUrl = NetworkUtils.buildUrl(napsterQuery);
//        return napsterSearchUrl;
//    }

    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(mContext) {
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
//        populateRecyclerView(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {}

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        if (itemThatWasClickedId == R.id.action_search) {
//            URL url = makeSearchUrl();
//            Bundle bundle = new Bundle();
//            bundle.putString(SEARCH_QUERY_URL_EXTRA, url.toString());
//            LoaderManager loaderManager = getSupportLoaderManager();
//            Loader<String> searchLoader = loaderManager.getLoader(LOADER_ID);
//            if(searchLoader == null){
//                loaderManager.initLoader(LOADER_ID, bundle, this).forceLoad();
//            }else{
//                loaderManager.restartLoader(LOADER_ID, bundle, this).forceLoad();
//            }
            Toast.makeText(mContext, "Item Selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

//    public void populateRecyclerView(String searchResults){
//        Log.d("mycode", searchResults);
//        mTracks = JsonUtils.parseNews(searchResults);
//        mAdapter.mPosts.clear();
//        mAdapter.mPosts.addAll(mPosts);
//        mAdapter.notifyDataSetChanged();
//    }

    /*
    ---------------------------------------- Firebase ------------------------------
    */

    private void checkCurrentUser(FirebaseUser currentUser){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in. ");

        if (currentUser == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        checkCurrentUser(currentUser);


        if (currentUser != null){
            Log.d(TAG, "onAuthStateChanged:signed_in: " + currentUser.getUid());
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }
}
