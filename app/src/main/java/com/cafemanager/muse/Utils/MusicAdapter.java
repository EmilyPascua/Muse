package com.cafemanager.muse.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafemanager.muse.Model.Track;
import com.cafemanager.muse.R;

import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder>{

    Context mContext;
    //    private ArrayList<String> tempList = new ArrayList<>();
    private List<Track> mPosts;

    // Pass in any necessary data to constructor so Adapter can attach the data to Music Player widget
    // mContext is used so that we can access LayoutInflater in onCreateViewHolder()
    public MusicAdapter(Context context, List<Track> posts) {
        mContext = context;

        mPosts = posts;

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
    public void onBindViewHolder(MusicHolder holder, int position) {

        // To make something apppear on the Album ImageView, you're gonna need to do
        // a bit more work.

        holder.mTrackNameTextView.setText(mPosts.get(position).getTrackName());
        holder.mTrackArtistTextView.setText(mPosts.get(position).getTrackArtist());

    }



    @Override
    public int getItemCount() {
        return mPosts.size();
    }



    public class MusicHolder extends RecyclerView.ViewHolder{

        private ImageView mAlbumImageView;
        private TextView mTrackNameTextView;
        private TextView mTrackArtistTextView;

        public MusicHolder(View itemView) {
            super(itemView);

            mAlbumImageView = (ImageView) itemView.findViewById(R.id.album_url);
            mTrackNameTextView = (TextView) itemView.findViewById(R.id.track_name);
            mTrackArtistTextView = (TextView) itemView.findViewById(R.id.track_artist);


        }
    }
}