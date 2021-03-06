package com.cafemanager.muse.Utils;


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
import com.cafemanager.muse.Profile.AccountSettingsActivity;
import com.cafemanager.muse.Profile.ProfileActivity;
import com.cafemanager.muse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  Copy and Paste. Emily-MergedKevin doesn't have this file.
 */

public class ViewProfileFragment extends Fragment {

    private static final String TAG = "ViewProfileFragment";
    private static final int ACTIVITY_NUM = 4;

    private Context mContext;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    // Widgets
    private TextView mPosts, mFollowers, mFollowing, mFollow, mUnfollow;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;
    private TextView mProfileName;
    TextView editProfile;




    // Member Variables
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;

    private RecyclerView mRecyclerView;
    private MusicAdapter mMusicAdapter;

    private User mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        mContext = getActivity();

        // Gonna use this member variable's user_id attribute to populate ViewProfileFragment
        mUser = getUserFromBundle();

        // Widgets
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        mFollow = (TextView) view.findViewById(R.id.follow);
        mUnfollow = (TextView) view.findViewById(R.id.unfollow);

        mProfileName = (TextView) view.findViewById(R.id.username);
        mProfileName.setText(mUser.getUsername());


        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.music_recycler_view);



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


        editProfile = (TextView) view.findViewById(R.id.textEditProfile);
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

        isFollowing();
        //Follow and Unfollow buttons
        mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: following: " + mUser.getUsername());
                FirebaseDatabase.getInstance().getReference()
                        .child(getString(R.string.firebase_following))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(mUser.getUser_id())
                        .child(getString(R.string.firebase_user_id))
                        .setValue(mUser.getUser_id());

                FirebaseDatabase.getInstance().getReference()
                        .child(getString(R.string.firebase_followers))
                        .child(mUser.getUser_id())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getString(R.string.firebase_user_id))
                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                followUser();
            }
        });

        mUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: unfollowing: " + mUser.getUsername());

                FirebaseDatabase.getInstance().getReference()
                        .child(getString(R.string.firebase_following))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(mUser.getUser_id())
                        .removeValue();

                FirebaseDatabase.getInstance().getReference()
                        .child(getString(R.string.firebase_followers))
                        .child(mUser.getUser_id())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .removeValue();

                unfollowUser();
            }
        });


        return view;
    }






    /**
     *  Retrieve user that was passed from SearchActivity
     */
    public User getUserFromBundle() {
        Log.d(TAG, "Getting user from Bundle: " + getArguments());

        Bundle bundle = this.getArguments();
        return bundle.getParcelable(getString(R.string.intent_user));
    }

    private void isFollowing(){
        Log.d(TAG, "isFollowing: follow checker");
        unfollowUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.firebase_following))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild(getString(R.string.firebase_user_id)).equalTo(mUser.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found user: " + snap.getValue());
                    followUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void followUser(){
        Log.d(TAG, "followUser: updating UI for following");
        mFollow.setVisibility(View.GONE);
        mUnfollow.setVisibility(View.VISIBLE);
        editProfile.setVisibility(View.GONE);

    }

    private void unfollowUser(){
        Log.d(TAG, "followUser: updating UI for unfollowing");
        mFollow.setVisibility(View.VISIBLE);
        mUnfollow.setVisibility(View.GONE);
        editProfile.setVisibility(View.GONE);
    }

    private void currentProfile(){
        Log.d(TAG, "followUser: updating UI showing own profile");
        mFollow.setVisibility(View.GONE);
        mUnfollow.setVisibility(View.VISIBLE);
        editProfile.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView() {
        // RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get reference to database and populate a list of Posts
        final ArrayList<Post> userPosts = new ArrayList<>();

        /**
         *  Attempting to user mUser's user_id attribute to get all the Posts
         *  that he/she has posted
         */

        Query query = myRef.child(getString(R.string.firebase_user_posts)).child(mUser.getUser_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: Found post:" + singleSnapshot.getValue());

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
        Query query = reference.child(getString(R.string.firebase_user_posts)).child(mUser.getUser_id());

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
                .child(mUser.getUser_id());
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
                .child(mUser.getUser_id());
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


    // BottomNavigationView setup
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}