package com.cafemanager.muse.Likes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cafemanager.muse.R;
import com.cafemanager.muse.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class LikesActivity extends AppCompatActivity{
    private  static  final String TAG = "LikesActivity";
    private Context mContext = LikesActivity.this;
    private static  final int ACTIVITY_NUM = 3;

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        /**
         *  This Activity is sharing the same layout as HomeActivity
         */
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started.");

        setupBottomNavigationView();
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
}