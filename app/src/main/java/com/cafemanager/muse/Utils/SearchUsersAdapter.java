package com.cafemanager.muse.Utils;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cafemanager.muse.Model.User;
import com.cafemanager.muse.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  Somewhere in here we're going to have to create another query to get
 *  a specific user's profile picture (Through userAccountSetting/user_account_settings(Firebase). Also
 *  need to create UserAccountSettings class bc we don't have it yet)
 */

public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.SearchUsersHolder>{

    private Context mContext;
    private ArrayList<User> mSearchUsers;

    public SearchUsersAdapter(Context context, ArrayList<User> listUsers) {

        mContext = context;
        mSearchUsers = listUsers;
    }

    @NonNull
    @Override
    public SearchUsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.layout_search_user_item, parent, shouldAttachToParentImmediately);
        SearchUsersHolder searchUsersHolder = new SearchUsersHolder(view);

        return searchUsersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUsersHolder searchUsersHolder, int position) {
        /**
         *  Picasso:
         *      Going to use Emily's approach (in Share/MusicAdapter) to load
         *      our profileImages.
         */



    }

    @Override
    public int getItemCount() {
        return mSearchUsers.size();
    }


    public static class SearchUsersHolder extends RecyclerView.ViewHolder {

        // Widgets in layout_search_user_item.xml
        private CircleImageView profileImage;
        private TextView username;
        private TextView email;

        public SearchUsersHolder(View view) {
            super(view);

            profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
            username = (TextView) view.findViewById(R.id.username);
            email = (TextView) view.findViewById(R.id.email);

        }
    }




}
