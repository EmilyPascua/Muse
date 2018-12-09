package com.cafemanager.muse.Share;

import android.content.Context;
import android.content.Intent;
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

import com.cafemanager.muse.Model.Track;
import com.cafemanager.muse.R;
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
//            Picasso.with(mContext).load(mTrack.get(listIndex).getAlbumUrl()).into(albumUrl);
            Picasso.get().load(mTrack.get(listIndex).getAlbumUrl()).into(albumUrl);
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
        //Transfers to MusicInformationActivity
        void triggerAddButton(final int listIndex){
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MuseMusicDescription.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("trackArtist",mTrack.get(listIndex).getTrackArtist());
                    intent.putExtra("trackName",mTrack.get(listIndex).getTrackName());
                    intent.putExtra("albumUrl", mTrack.get(listIndex).getAlbumUrl());

                    /**
                     *  Adding preview as intentExtra as well, since we'll need to use this to play tracks
                     *  on user's profile
                     */
                    intent.putExtra("previewUrl",mTrack.get(listIndex).getPreviewUrl());

                    mContext.startActivity(intent);
                    //Just filler information
                    Log.e("Information",mTrack.get(listIndex).getTrackName());
                    Log.e("Information",mTrack.get(listIndex).getPreviewUrl());
                    Log.e("Information",mTrack.get(listIndex).getAlbumUrl());
                    Log.e("Information",mTrack.get(listIndex).getArtistId());
                    Log.e("Information",mTrack.get(listIndex).getTrackArtist());

                }
            });
        }

        @Override
        public void onClick(View view) {
        }

    }
}
