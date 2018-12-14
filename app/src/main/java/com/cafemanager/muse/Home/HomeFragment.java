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
    private MainfeedListAdapter mMainfeedListAdapter;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_feed_recycler_view);
        setupMainfeedList();

        return view;
    }

    private void setupMainfeedList() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final ArrayList<Post> userPosts = new ArrayList<>();

        Query query = myRef.child(getString(R.string.firebase_user_posts));

        /**
         *  This will grab all the Posts of every user. Even the ones you aren't following
         */
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: Found post:" + singleSnapshot.getValue());

                    for(DataSnapshot singlePostSnapshot : singleSnapshot.getChildren()) {
                        // Use singleSnapshot to get all the values you need for a single Post object
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



                }


                mMainfeedListAdapter = new MainfeedListAdapter(getActivity(), userPosts);


                mRecyclerView.setAdapter(mMainfeedListAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
