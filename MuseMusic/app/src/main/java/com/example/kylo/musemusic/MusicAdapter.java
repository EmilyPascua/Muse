package com.example.kylo.musemusic;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder>{
    Context mContext;
    ArrayList<Track> mTrack;

    public MusicAdapter(Context context, ArrayList<Track> track){
        this.mContext = context;
        this.mTrack = track;
    }

    @Override
    public MusicAdapter.MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.music_item, parent, shouldAttachToParentImmediately);
        MusicHolder viewHolder = new MusicHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MusicAdapter.MusicHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mTrack.size();
    }

    public class MusicHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView albumUrl;
        TextView trackName;
        TextView trackArtist;
        Button playButton;
        Button addButton;
        MediaPlayer mediaPlayer = new MediaPlayer();

        public MusicHolder(View itemView) {
            super(itemView);
            playButton = (Button) itemView.findViewById(R.id.play_button);
            albumUrl = (ImageView) itemView.findViewById(R.id.album_url);
            trackName = (TextView) itemView.findViewById(R.id.track_name);
            trackArtist = (TextView) itemView.findViewById(R.id.track_artist);
            addButton = (Button) itemView.findViewById(R.id.add_button);
        }

        void bind(final int listIndex) {
            Picasso.with(mContext).load(mTrack.get(listIndex).getAlbumUrl()).into(albumUrl);
            createMediaPlayer(mTrack.get(listIndex).getPreviewUrl());
            triggerPlayButton();
            triggerAddButton(listIndex);
            trackName.setText(mTrack.get(listIndex).getTrackName());
            trackArtist.setText("By " + mTrack.get(listIndex).getTrackArtist());
            itemView.setOnClickListener(this);
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

        void triggerAddButton(final int listIndex){
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Information",mTrack.get(listIndex).getTrackArtist());
                }
            });
            addButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        addButton.setBackgroundResource(R.drawable.ic_add_circle_black_24dp);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        addButton.setBackgroundResource(R.drawable.ic_add_circle_outline_black_24dp);
                    }
                    return false;
                }
            });
        }

        @Override
        public void onClick(View view) {
            String in1 = mTrack.get(getAdapterPosition()).getTrackArtist();
            Log.d("Infomration", in1);
        }

    }
}
