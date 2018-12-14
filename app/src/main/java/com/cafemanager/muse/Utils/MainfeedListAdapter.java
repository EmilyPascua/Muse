package com.cafemanager.muse.Utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cafemanager.muse.Model.Post;
import com.cafemanager.muse.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainfeedListAdapter extends RecyclerView.Adapter<MainfeedListAdapter.MainfeedHolder> {

    private static final String TAG = "MusicAdapter";
    Context mContext;
    private List<Post> mPosts;


    // Pass in any necessary data to constructor so Adapter can attach the data to Music Player widget
    // mContext is used so that we can access LayoutInflater in onCreateViewHolder()
    public MainfeedListAdapter(Context context, List<Post> posts) {
        mContext = context;

        mPosts = posts;

    }

    @Override
    public MainfeedListAdapter.MainfeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.post_item, parent, shouldAttachToParentImmediately);
        MainfeedListAdapter.MainfeedHolder viewHolder = new MainfeedListAdapter.MainfeedHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(MainfeedListAdapter.MainfeedHolder holder, int position) {

        // To make something apppear on the Album ImageView, you're gonna need to do
        // a bit more work.

        Log.d(TAG, "position: " + position);

        holder.bind(position);



    }




    @Override
    public int getItemCount() {
        return mPosts.size();
    }



    public class MainfeedHolder extends RecyclerView.ViewHolder{

        private ImageView mAlbumImageView;
        private TextView mTrackNameTextView;
        private TextView mTrackArtistTextView;
        private TextView mDescription;
        private Button playButton;
        MediaPlayer mediaPlayer = new MediaPlayer();

        public MainfeedHolder(View itemView) {
            super(itemView);

            mAlbumImageView = (ImageView) itemView.findViewById(R.id.album_url);
            mTrackNameTextView = (TextView) itemView.findViewById(R.id.track_name);
            mTrackArtistTextView = (TextView) itemView.findViewById(R.id.track_artist);
            mDescription = (TextView) itemView.findViewById(R.id.description);
            playButton = (Button) itemView.findViewById(R.id.post_play_button);


        }

        void bind(final int position) {
//            Picasso.with(mContext).load(mTrack.get(listIndex).getAlbumUrl()).into(albumUrl);

//            Picasso.get().load(mPosts.get(position).getAlbum_image()).into(mAlbumImageView);

            Picasso.get()
                    .load(mPosts.get(position).getAlbum_image())
                    .placeholder(R.drawable.ic_music)
                    .error(R.drawable.ic_error)
                    .into(mAlbumImageView);

            createMediaPlayer(mPosts.get(position).getSong_preview());
            triggerPlayButton();

            mTrackNameTextView.setText(mPosts.get(position).getSong_name());
            mTrackArtistTextView.setText(mPosts.get(position).getArtist_name());
            mDescription.setText(mPosts.get(position).getPost_description());

        }

        void createMediaPlayer(String track){
            Toast.makeText(mContext,"Play Song", Toast.LENGTH_LONG);
            try {
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(track);
                mediaPlayer.prepareAsync();
                mediaPlayer.start();

                // Changing Button Image to pause image
                playButton.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        void triggerPlayButton(){
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mediaPlayer.isPlaying()){
                        if(mediaPlayer!=null){
                            mediaPlayer.pause();
                            // Changing button image to play button
                            playButton.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                        }
                    }else{
                        // Resume song
                        if(mediaPlayer!=null){
                            mediaPlayer.start();
                            // Changing button image to pause button
                            playButton.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                        }
                    }
                }
            });
        }
    }
}
