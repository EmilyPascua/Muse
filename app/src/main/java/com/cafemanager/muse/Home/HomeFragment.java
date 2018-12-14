package com.cafemanager.muse.Home;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cafemanager.muse.Model.Post;
import com.cafemanager.muse.R;
import com.cafemanager.muse.Utils.MainfeedListAdapter;
import com.cafemanager.muse.Utils.MusicAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static  final String TAG = "HomeFragment";

    private RecyclerView mRecyclerView;
    MainfeedListAdapter mMainfeedListAdapter;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ArrayList<String> mFollowingIds;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_feed_recycler_view);
        mFollowingIds = new ArrayList<>();

        setupFirebaseAuth();

        getListOfFollowing();

        Log.d(TAG, "Attempting to set up FollowingFeed");
        setupFollowingFeed();

        return view;
    }

    private void setupFollowingFeed() {


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final ArrayList<Post> userPosts = new ArrayList<>();


        /**
         *  Do NOT for the love of god, move the line that initializes the adapter and sets
         *  it to the RecyclerView
         */

        for(String followingID : mFollowingIds) {

            Log.d(TAG, "setupFollowingFeed() userID: " + followingID);
            Query query = myRef.child(getString(R.string.firebase_user_posts)).child(followingID);


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot singlePostSnapshot : dataSnapshot.getChildren()) {
                        Post post = new Post();

                        // Create Map (from your DataSnapshot) to get values you need easily
                        Map<String, Object> firebasePostMap = (HashMap<String, Object>) singlePostSnapshot.getValue();


                        // Configure Post object
                        post.setArtist_name(firebasePostMap.get(getString(R.string.firebase_artist_name)).toString());
                        post.setSong_name(firebasePostMap.get(getString(R.string.firebase_song_name)).toString());
                        post.setAlbum_image(firebasePostMap.get(getString(R.string.firebase_album_image)).toString());
                        post.setPost_description(firebasePostMap.get(getString(R.string.firebase_post_description)).toString());
                        post.setSong_preview(firebasePostMap.get(getString(R.string.firebase_song_preview)).toString());

                        userPosts.add(post);
                    }
                    mMainfeedListAdapter = new MainfeedListAdapter(getActivity(), userPosts);
                    mRecyclerView.setAdapter(mMainfeedListAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }








    }

    private void getListOfFollowing() {


        Query query = myRef.child(getString(R.string.firebase_following)).child(mAuth.getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found following user:" + singleSnapshot.getKey());

                    singleSnapshot.child(getString(R.string.firebase_user_id)).getValue();

                    // Adding the userIDs of all the users we're following.
                    // Going to use this list of IDs in a separate method to populate HomeFragment feed
                    mFollowingIds.add(singleSnapshot.child(getString(R.string.firebase_user_id)).getValue().toString());
                }

                setupFollowingFeed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "Done getting userIDs. Following count: " + mFollowingIds.size());

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
