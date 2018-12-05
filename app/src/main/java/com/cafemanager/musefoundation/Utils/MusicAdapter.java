package com.cafemanager.musefoundation.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafemanager.musefoundation.Models.Post;
import com.cafemanager.musefoundation.R;

import java.util.ArrayList;
import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder>{

    Context mContext;
//    private ArrayList<String> tempList = new ArrayList<>();
    private List<Post> mPosts;

    // Pass in any necessary data to constructor so Adapter can attach the data to Music Player widget
    // mContext is used so that we can access LayoutInflater in onCreateViewHolder()
    public MusicAdapter(Context context, List<Post> posts) {
        mContext = context;

        mPosts = posts;

//        for(int i = 0; i < 10; i++) {
//            String tempString = "Temp string just to populate RecyclerView";
//
//            tempList.add(tempString);
//        }
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

        holder.mTrackNameTextView.setText(mPosts.get(position).getSongName());
        holder.mTrackArtistTextView.setText(mPosts.get(position).getArtistName());

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
