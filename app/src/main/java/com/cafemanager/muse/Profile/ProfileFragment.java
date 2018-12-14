package com.cafemanager.muse.Profile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafemanager.muse.Model.Post;
import com.cafemanager.muse.Model.Track;
import com.cafemanager.muse.Model.User;
import com.cafemanager.muse.Model.UserAccountSettings;
import com.cafemanager.muse.Model.UserSettings;
import com.cafemanager.muse.R;
import com.cafemanager.muse.Utils.BottomNavigationViewHelper;
import com.cafemanager.muse.Utils.MusicAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import com.cafemanager.muse.Utils.FirebaseMethods.FirebaseMethods;
import com.cafemanager.muse.Utils.UniversalImageLoader;
import com.cafemanager.muse.Model.User;
import com.cafemanager.muse.Model.UserAccountSettings;
import com.cafemanager.muse.Model.UserSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  Copy and Paste. Emily-MergedKevin doesn't have this file.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final int ACTIVITY_NUM = 4;

    private Context mContext;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseMethods mFirebaseMethods;


    // Widgets
    private TextView mPosts, mFollowers, mFollowing;

    private TextView mDisplayName, mUsername, mWebsite, mDescription;

    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;

    private CircleImageView mProfilePhoto;





    // Member Variables
    //vars
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;

    private RecyclerView mRecyclerView;
    private MusicAdapter mMusicAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Widgets
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);

        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        mUsername = (TextView) view.findViewById(R.id.username);
        mWebsite = (TextView) view.findViewById(R.id.website);
        mDescription = (TextView) view.findViewById(R.id.description);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mPosts = (TextView) view.findViewById(R.id.tvPosts);

        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);

        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.music_recycler_view);
        mContext = getActivity();

        mFirebaseMethods = new FirebaseMethods(getActivity());


        setupFirebaseAuth();

        setupToolbar();
        setupBottomNavigationView();


        // This should be moved to a separate method once we implement Firebase authentication
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        // This should be moved to a separate method once we implement Firebase authentication


        /**
         *  Anyone trying to test out Profile page needs to comment out
         *      1) setupRecyclerView();
         *      2) getPostsCount();
         *      3) getFollowersCount();
         *      4) getFollowingCount();
         *      5) Firebase references above
         *
         *  If you wanna test Profile page w/ Firebase, add Dependencies and
         *  and google-services.json.
         *
         */

        setupRecyclerView();

        getPostsCount();
        getFollowersCount();
        getFollowingCount();


        TextView editProfile = (TextView) view.findViewById(R.id.textEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to " + mContext.getString(R.string.edit_profile_fragment));
                Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        return view;
    }



    private void setProfileWidgets(UserSettings userSettings){
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getSettings().getUsername());
        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();
//        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        Picasso.get()
                .load(settings.getProfile_photo())
                .placeholder(R.drawable.ic_androidplaceholder)
                .into(mProfilePhoto);

        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mPosts.setText(String.valueOf(settings.getPosts()));

        //  Don't need these 2 because it's already handled in methods below
//        mFollowing.setText(String.valueOf(settings.getFollowing()));
//        mFollowers.setText(String.valueOf(settings.getFollowers()));
//        mProgressBar.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        // RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get reference to database and populate a list of Posts
        final ArrayList<Post> userPosts = new ArrayList<>();


        /**
         *  Get all posts for a specific user. Rn we are hardcoding "1111" until
         *  we can get the Auth state of the current user.
         */
        Query query = myRef.child(getString(R.string.firebase_user_posts)).child(mAuth.getCurrentUser().getUid());



        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: Found user_post:" + singleSnapshot.getValue());

                    // Use singleSnapshot to get all the values you need for a single Post object
                    Post post = new Post();

                    // Create Map (from your DataSnapshot) to get values you need easily
                    Map<String, Object> firebasePostMap = (HashMap<String, Object>) singleSnapshot.getValue();


                    // Configure Post object
                    post.setArtist_name(firebasePostMap.get(getString(R.string.firebase_artist_name)).toString());
                    post.setSong_name(firebasePostMap.get(getString(R.string.firebase_song_name)).toString());
                    post.setAlbum_image(firebasePostMap.get(getString(R.string.firebase_album_image)).toString());
                    post.setPost_description(firebasePostMap.get(getString(R.string.firebase_post_description)).toString());
                    post.setSong_preview(firebasePostMap.get(getString(R.string.firebase_song_preview)).toString());

                    userPosts.add(post);

                }

                // End Result:
                //   mMusicAdapter = new MusicAdapter(getActivity(), userPosts);
                mMusicAdapter = new MusicAdapter(getActivity(), userPosts);

                mRecyclerView.setAdapter(mMusicAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    // In the future, we will be getting specific Auth user node with
    // .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    // I think he used a Query because it's faster. And since Firebase
    // demands that you pull the children of the node you're targeting...
    // this seems like the best option. Should read up on it though.

    private void getPostsCount(){
        mPostsCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference.child(getString(R.string.firebase_user_posts)).child("1111");
        Query query = reference.child(getString(R.string.firebase_user_posts)).child(mAuth.getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: Found post:" + singleSnapshot.getValue());
                    mPostsCount++;
                }
                mPosts.setText(String.valueOf(mPostsCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // In the future, we will be getting specific Auth user node with
    // .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    private void getFollowersCount(){
        mFollowersCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.firebase_followers)) // followers  node
                .child(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.getValue());
                    mFollowersCount++;
                }
                mFollowers.setText(String.valueOf(mFollowersCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFollowingCount(){
        mFollowingCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.firebase_following))
                .child(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found following user:" + singleSnapshot.getValue());
                    mFollowingCount++;
                }
                mFollowing.setText(String.valueOf(mFollowingCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     So as of rn, there is no way to update followers_count and following_count
     for a specific user (Viewing other's profile and "following" them hasn't
     been implemented yet).
     In the meantime, we will use dummy data under "following" and "followers"
     nodes so that a user's profile will at least be pulling data from firebase.
     Once we implement a way to "follow" someone, we can simply update a user's
     "followers_count" and "following_count". (dunno if that makes sense. It's 3AM...)
     Mitch explains this in (Part 87) @ 6:50.
     Notice how he's just counting the id's under nodes "following" and "followers".
     It seems like "user_account_settings" node isn't being used to set the text
     for TextViews mFollowing and mFollowers
     */


    /**
     * Responsible for setting up the profile toolbar
     */
    private void setupToolbar(){

        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings.");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }


    //BottomNavigationView setup
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    Log.d(TAG, "onAuthStateChanged: signed_in");
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                Log.d(TAG, "Value of snapshot " + dataSnapshot.getValue());
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
    }


    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

}