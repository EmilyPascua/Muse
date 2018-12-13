package com.cafemanager.muse.Utils.FirebaseMethods;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.cafemanager.muse.Model.User;
import com.cafemanager.muse.Model.UserAccountSettings;
import com.cafemanager.muse.Model.UserSettings;
import com.cafemanager.muse.R;
import com.cafemanager.muse.Utils.StringManipulation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.support.constraint.Constraints.TAG;

public class FirebaseMethods {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mUserID;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMyRef;

    private Context mContext;

    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMyRef = mFirebaseDatabase.getReference();


        if(mAuth.getCurrentUser() != null){
            mUserID = mAuth.getCurrentUser().getUid();

        }
    }


    public boolean checkIfUsernameExists(String username, DataSnapshot dataSnapshot){
        Log.d(TAG, "checkIfUsernameExists: checking if " + username + " already exists");

        User user = new User();

        for (DataSnapshot ds: dataSnapshot.child(mUserID).getChildren()){
            Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);

            user.setUsername(ds.getValue(User.class).getUsername());

            if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
                Log.d(TAG, "checkIfUsernameExists: found an existing username " + user.getUsername() );

                return  true;
            }
        }
        return false;
    }


    /**
     * Register email and password to Firebase Authentication
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mUserID = mAuth.getCurrentUser().getUid();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }



    public void addNewUser(String email, String username, String description, String website, String profile_picture){

        User user = new User(mUserID, 1, email, StringManipulation.condenseUsername(username));

        mMyRef.child(mContext.getString(R.string.firebase_users))
                .child(mUserID)
                .setValue(user);


        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                "",
                StringManipulation.condenseUsername(username),
                website,
                mUserID);

        mMyRef.child(mContext.getString(R.string.firebase_user_account_settings))
                .child(mUserID)
                .setValue(settings);


    }

    /**
     * Retrieves the account settings for teh user currently logged in
     * Database: user_acount_Settings node
     * @param dataSnapshot
     * @return
     */
    private UserSettings getUserAccountSettings(DataSnapshot dataSnapshot){
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase.");
        UserAccountSettings settings  = new UserAccountSettings();
        User user = new User();
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            // user_account_settings node
            if(ds.getKey().equals(mContext.getString(R.string.firebase_user_account_settings))){
                Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);
                try{
                    settings.setDisplay_name(
                            ds.child(mUserID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUsername(
                            ds.child(mUserID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(mUserID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );
                    settings.setDescription(
                            ds.child(mUserID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(mUserID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(mUserID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowing(
                            ds.child(mUserID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFollowers(
                            ds.child(mUserID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );
                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: " + settings.toString());
                }catch (NullPointerException e){
                    Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage() );
                }
            }

            // users node
            if(ds.getKey().equals(mContext.getString(R.string.firebase_users))) {
                Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);
                user.setUsername(
                        ds.child(mUserID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(mUserID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(mUserID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(mUserID)
                                .getValue(User.class)
                                .getUser_id()
                );
                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
            }
        }
        return new UserSettings(user, settings);
    }

}
