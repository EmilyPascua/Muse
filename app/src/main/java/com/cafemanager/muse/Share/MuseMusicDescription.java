package com.cafemanager.muse.Share;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cafemanager.muse.Model.Post;
import com.cafemanager.muse.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MuseMusicDescription extends AppCompatActivity {

    private static final String TAG = "MuseMusicDescription";

    private EditText mPostDescription;
    private String mTrackArtist;
    private String mTrackName;
    private String mAlbumUrl;
    private String mPreviewUrl;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicdescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Post");
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        setupFirebaseAuth();

        mPostDescription = (EditText) findViewById(R.id.post_description);

        Bundle trackArtist = getIntent().getExtras();
        Bundle trackName = getIntent().getExtras();
        Bundle albumUrl = getIntent().getExtras();

        Bundle previewUrl = getIntent().getExtras();

        mTrackArtist = trackArtist.getString("trackArtist");
        mTrackName = trackName.getString("trackName");
        mAlbumUrl = albumUrl.getString("albumUrl");

        mPreviewUrl = previewUrl.getString("previewUrl");


        final TextView trackArtistText = (TextView) findViewById(R.id.track_artist_description);

        trackArtistText.setText(mTrackArtist);

        final TextView trackNameText = (TextView) findViewById(R.id.track_name_description);

        trackNameText.setText(mTrackName);

        final ImageView albumPicture = (ImageView) findViewById(R.id.track_album_artist_description);

//        Picasso.with(getApplicationContext()).load(sAlbumUrl).into(albumPicture);
        Picasso.get().load(mAlbumUrl).into(albumPicture);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.submit_description, menu);
        return true;
    }


    /**
     *
     * @param item
     * @return
     *
     * Will create Post object using member variables for this specific music description.
     * Then use hard-coded user_id:"1111" to push this Post Object under "user_posts --> 1111".
     *
     * Will display Toast then finish the Activity for both Success/Failure scenarios.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_post) {

            // Create/Configure Post
            Post post = new Post();
            post.setPost_description(mPostDescription.getText().toString());
            post.setArtist_name(mTrackArtist);
            post.setSong_name(mTrackName);
            post.setAlbum_image(mAlbumUrl);
            post.setSong_preview(mPreviewUrl);

            // Push new Post object to Firebase
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            // Push/obtain key of new Post
            String postKey = databaseReference.child(getString(R.string.firebase_user_posts)).child(mAuth.getCurrentUser().getUid()).push().getKey();


            databaseReference.child("user_posts")
                    .child(mAuth.getCurrentUser().getUid())
                    .child(postKey).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MuseMusicDescription.this, "Post Created!",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failed to create post");

                            Toast.makeText(MuseMusicDescription.this, "Post Failed...",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });;

            // Create Post and push to "user_post" under "1111" (later on we'll use Auth
            // to push post to correct user_id node)

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();

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
