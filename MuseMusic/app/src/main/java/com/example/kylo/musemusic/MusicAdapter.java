package com.example.kylo.musemusic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        TextView albumUrl;
        TextView trackName;
        TextView trackArtist;
//        track_artist
//                track_name
//        album_url
        public MusicHolder(View itemView) {
            super(itemView);
            albumUrl = (TextView) itemView.findViewById(R.id.track_artist);
            trackName = (TextView) itemView.findViewById(R.id.track_name);
            trackArtist = (TextView) itemView.findViewById(R.id.album_url);
        }

        void bind(final int listIndex) {
            albumUrl.setText(mTrack.get(listIndex).getAlbumUrl());
            trackName.setText(mTrack.get(listIndex).getTrackName());
            trackArtist.setText(mTrack.get(listIndex).getTrackArtist());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            String urlString = mTrack.get(getAdapterPosition()).getUrl();
//            Intent intent = new Intent(mContext, WebActivity.class);
//            intent.putExtra("urlString", urlString);
//            mContext.startActivity(intent);
        }
    }
}
