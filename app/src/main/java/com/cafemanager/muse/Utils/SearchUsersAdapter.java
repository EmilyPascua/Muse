package com.cafemanager.muse.Utils;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.cafemanager.muse.Model.User;
import com.cafemanager.muse.Model.UserAccountSettings;
import com.cafemanager.muse.Profile.ProfileActivity;
import com.cafemanager.muse.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  Somewhere in here we're going to have to create another query to get
 *  a specific user's profile picture (Through userAccountSetting/user_account_settings(Firebase). Also
 *  need to create UserAccountSettings class bc we don't have it yet)
 */

public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.SearchUsersHolder>{

    private static final String TAG = "SearchUserAdapter";
    private Context mContext;
    public ArrayList<User> mSearchUsers;



    public SearchUsersAdapter(Context context, ArrayList<User> listUsers) {
        this.mContext = context;
        this.mSearchUsers = listUsers;
    }

    @NonNull
    @Override
    public SearchUsersAdapter.SearchUsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.layout_search_user_item, parent, shouldAttachToParentImmediately);
        SearchUsersHolder searchUsersHolder = new SearchUsersHolder(view);

        return searchUsersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchUsersHolder searchUsersHolder, int position) {
        searchUsersHolder.bind(position);


    }

    @Override
    public int getItemCount() {
        return mSearchUsers.size();
    }


    /**
     *  Set an onClickListener to your viewHolder so that we can navigate to
     *  ProfileActivity --> ViewProfileFragment
     */

    public class SearchUsersHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Widgets in layout_search_user_item.xml
        public CircleImageView profileImage;
        private TextView username;
        private TextView email;

        public SearchUsersHolder(View view) {
            super(view);

            profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
            username = (TextView) view.findViewById(R.id.username);
            email = (TextView) view.findViewById(R.id.email);

            view.setOnClickListener(this);

        }


        /**
         *
         * @param view
         * Will navigate to UserProfile depending on which item was clicked
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();


            Intent intent = new Intent(mContext, ProfileActivity.class);
            intent.putExtra(mContext.getString(R.string.calling_activity), mContext.getString(R.string.search_activity));
            intent.putExtra(mContext.getString(R.string.intent_user), mSearchUsers.get(position));
            mContext.startActivity(intent);

//            Toast.makeText(mContext, "Position clicked: " + position,
//                    Toast.LENGTH_LONG).show();
        }


        void bind(int position) {
            /**
             *  Picasso:
             *      Going to use Emily's approach (in Share/MusicAdapter) to load
             *      our profileImages.
             */

//            searchUsersHolder.username.setText(mSearchUsers.get(position).getUsername());
//            searchUsersHolder.email.setText(mSearchUsers.get(position).getEmail());

            username.setText(mSearchUsers.get(position).getUsername());
            email.setText(mSearchUsers.get(position).getEmail());

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Query query = databaseReference.child(mContext.getString(R.string.firebase_user_account_settings))
                    .orderByChild(mContext.getString(R.string.firebase_user_id))
                    .equalTo(mSearchUsers.get(position).getUser_id());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange found user_id: " +
                                singleSnapshot.getValue(UserAccountSettings.class).toString());

                        /**
                         *  Now we'll use Picasso to set image for our CircleImageView.
                         */

                        String profileImageUrl = singleSnapshot.getValue(UserAccountSettings.class).getProfile_photo();



//                        Picasso.with(mContext).load(profileImageUrl).into(searchUsersHolder.profileImage);

                        CircleImageView x = profileImage;

                        if (profileImageUrl.equals("") || profileImageUrl.length() == 0){
                            // Load a default if profileImageUrl String is empty
                        } else {
                            Picasso.get().load(profileImageUrl).into(profileImage);
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }




}
