package com.cafemanager.musefoundation.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.cafemanager.musefoundation.R;
import com.cafemanager.musefoundation.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileActivity extends AppCompatActivity{

    private  static  final String TAG = "ProfileActivity";
    private Context mContext = ProfileActivity.this;

    private static  final int ACTIVITY_NUM = 4;

    private ProgressBar mProgressBar;
    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");
//        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
//        mProgressBar.setVisibility(View.GONE);


        init();

        // Move these 2 calls along with their method definitions over to ProfileFragment
//        setupBottomNavigationView();
//        setupToolbar();
    }

    private  void  setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: Navigating to account settings.");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

    }
    //BottomNavigationView setup
    private void setupBottomNavigationView(){

        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
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
//            Log.d(TAG, "init: searching for user object attached as intent extra");
//            if(intent.hasExtra(getString(R.string.intent_user))){
//                User user = intent.getParcelableExtra(getString(R.string.intent_user));
//                if(!user.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//
//                    // Used in situation where user clicks on profile that is NOT his/her own
//                    Log.d(TAG, "init: inflating view profile");
//                    ViewProfileFragment fragment = new ViewProfileFragment();
//                    Bundle args = new Bundle();
//                    args.putParcelable(getString(R.string.intent_user),
//                            intent.getParcelableExtra(getString(R.string.intent_user)));
//                    fragment.setArguments(args);
//
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.container, fragment);
//                    transaction.addToBackStack(getString(R.string.view_profile_fragment));
//                    transaction.commit();
//                }else{
//
//                    // Used in situation where user might click on his/her own profile via the
//                    // profileImage that is displayed with every post (Check mprofileImage onClick in
//                    // MainfeedListAdapter)

//                    // (Since users can also see their own posts on the Main Feed)
//                    Log.d(TAG, "init: inflating Profile");
//                    ProfileFragment fragment = new ProfileFragment();
//                    FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.container, fragment);
//                    transaction.addToBackStack(getString(R.string.profile_fragment));
//                    transaction.commit();
//                }
//            }else{
//                Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
//            }

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



}

