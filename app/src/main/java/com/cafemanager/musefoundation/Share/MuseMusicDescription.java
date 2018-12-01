package com.cafemanager.musefoundation.Share;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafemanager.musefoundation.R;
import com.squareup.picasso.Picasso;

public class MuseMusicDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicdescription);

        Bundle trackArtist = getIntent().getExtras();
        Bundle trackName = getIntent().getExtras();
        Bundle albumUrl = getIntent().getExtras();

        String sTrackArtist = trackArtist.getString("trackArtist");
        String sTrackName = trackName.getString("trackName");
        String sAlbumUrl = albumUrl.getString("albumUrl");

        final TextView trackArtistText = (TextView) findViewById(R.id.track_artist_description);

        trackArtistText.setText(sTrackArtist);

        final TextView trackNameText = (TextView) findViewById(R.id.track_name_description);

        trackNameText.setText(sTrackName);

        final ImageView albumPicture = (ImageView) findViewById(R.id.track_album_artist_description);

        Picasso.with(getApplicationContext()).load(sAlbumUrl).into(albumPicture);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.submit_description, menu);
        return true;
    }
}
