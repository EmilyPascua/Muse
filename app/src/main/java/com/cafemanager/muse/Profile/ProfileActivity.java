package com.cafemanager.muse.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cafemanager.muse.Model.User;
import com.cafemanager.muse.R;
import com.cafemanager.muse.Utils.BottomNavigationViewHelper;
import com.cafemanager.muse.Utils.ViewProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileActivity extends AppCompatActivity{
    private  static  final String TAG = "ProfileActivity";
    private Context mContext = ProfileActivity.this;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static  final int ACTIVITY_NUM = 4;

    private ProgressBar mProgressBar;
    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");
        //mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        //mProgressBar.setVisibility(View.GONE);

        setupFirebaseAuth();
        init();
    }

    private void init(){
        Log.d(TAG, "init: inflating " + getString(R.string.profile_fragment));
        Intent intent = getIntent();

        /**
         Ignore "if" block portion for now. The purpose of it is to check whether user is coming
         from HomeActivity. HomeActivity will host the main feed feature, which will give users
         the option to look at a user's profile (or his/her own) via the post's profile picture.

         The "else" block is executed if the user is simply using the BottomNavigationView tab
         to start ProfileActivity (which means the intent won't have an extra. Refer to
         BottomNavigationViewHelper.java)

         */
        if(intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG, "init: searching for user object attached as intent extra");
            if(intent.hasExtra(getString(R.string.intent_user))){
                User user = intent.getParcelableExtra(getString(R.string.intent_user));
                /**
                 * "1111" was hardcoded!!!
                 * Later on we need to change it to "FirebaseAuth.getInstance().getCurrentUser().getUid())"
                 *
                 * So we are checking if the User passed to this Activity is the currently signed in user.
                 * If-Not, then we go to ViewProfileFragment, and pass along the User object.
                 *
                 * If the User's id DOES match the currently signed in user, then we simply go to
                 * ProfileFragment
                 */
                if(!user.getUser_id().equals(mAuth.getCurrentUser().getUid())){

                    // Used in situation where user clicks on profile that is NOT his/her own
                    Log.d(TAG, "init: inflating view profile");
                    ViewProfileFragment fragment = new ViewProfileFragment();
                    Bundle args = new Bundle();
                    args.putParcelable(getString(R.string.intent_user),
                            intent.getParcelableExtra(getString(R.string.intent_user)));
                    fragment.setArguments(args);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(getString(R.string.view_profile_fragment));
                    transaction.commit();
                }else{

                    // Used in situation where user might click on his/her own profile via the
                    // profileImage that is displayed with every post (Check mprofileImage onClick in
                    // MainfeedListAdapter)

                    // (Since users can also see their own posts on the Main Feed)
                    Log.d(TAG, "init: inflating Profile");
                    ProfileFragment fragment = new ProfileFragment();
                    FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(getString(R.string.profile_fragment));
                    transaction.commit();
                }
            }else{
                Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            }

        }else{
            // Used when user clicks on BottomNavigationView tab that starts ProfileActivity
            // In this situation there will be NO intent extra (By looking at BottomNavigationViewHelper)
            // Thus, we'll enter this "else" block
            Log.d(TAG, "init: inflating Profile");
            ProfileFragment fragment = new ProfileFragment();
            FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(getString(R.string.profile_fragment));
            transaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            finish();    // Finish the activity
    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    Log.d(TAG, "onAuthStateChanged: signed_in");
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
    }


    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

}

