package com.example.kylo.musemusic;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;
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
        TextView previewUrl;
        Button playButton;
        MediaPlayer mediaPlayer = new MediaPlayer();

        //        track_artist
//                track_name
//        album_url
        public MusicHolder(View itemView) {
            super(itemView);
            playButton = (Button) itemView.findViewById(R.id.play_button);
            albumUrl = (ImageView) itemView.findViewById(R.id.album_url);
            trackName = (TextView) itemView.findViewById(R.id.track_name);
            trackArtist = (TextView) itemView.findViewById(R.id.track_artist);
            previewUrl = (TextView) itemView.findViewById(R.id.preview_url);
        }

        void bind(final int listIndex) {
            Picasso.with(mContext).load(mTrack.get(listIndex).getAlbumUrl()).into(albumUrl);
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                        playButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    }else{
                        mediaPlayer.pause();
                        playButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                }
            });

            trackName.setText(mTrack.get(listIndex).getTrackName());
            trackArtist.setText(mTrack.get(listIndex).getTrackArtist());
            previewUrl.setText(mTrack.get(listIndex).getPreviewUrl());
            itemView.setOnClickListener(this);
        }

        void createMediaPlayer(String track, int playBack){
            try{
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(track);
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
//            mediaPlayer.start();
        }
    }
}
