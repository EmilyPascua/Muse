package com.cafemanager.musefoundation.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cafemanager.musefoundation.R;

import java.util.ArrayList;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder>{

    Context mContext;
    private ArrayList<String> tempList = new ArrayList<>();

    // Pass in any necessary data to constructor so Adapter can attach the data to Music Player widget
    // mContext is used so that we can access LayoutInflater in onCreateViewHolder()
    public MusicAdapter(Context context) {
        mContext = context;
        for(int i = 0; i < 10; i++) {
            String tempString = "Temp string just to populate RecyclerView";

            tempList.add(tempString);
        }
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

    }

    @Override
    public int getItemCount() {
        return tempList.size();
    }

    public class MusicHolder extends RecyclerView.ViewHolder{

        public MusicHolder(View itemView) {
            super(itemView);

        }
    }
}
