package com.cafemanager.muse.Home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cafemanager.muse.Login.LoginActivity;
import com.cafemanager.muse.R;
import com.cafemanager.muse.Utils.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private Context mContext = HomeActivity.this;
    private  static  final String TAG = "HomeActivity";
    private static  final int ACTIVITY_NUM = 0;

    //firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: starting.");

        mAuth = FirebaseAuth.getInstance();


        setupBottomNavigationView();
        setupViewPager();
    }


    //adds the  3 tabs at the top: camera, home, and messages
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new CameraFragment()); // index 0
        adapter.addFragment(new HomeFragment());  // index 1
        adapter.addFragment(new MessagesFragment());  // index 2

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_cameraph);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_instagramph);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrowph);
    }

    //BottomNavigationView setup
    private void setupBottomNavigationView(){

        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    /*
    ---------------------------------------- Firebase ------------------------------
    */

    private void checkCurrentUser(FirebaseUser currentUser){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in. ");

        if (currentUser == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        checkCurrentUser(currentUser);


        if (currentUser != null){
            Log.d(TAG, "onAuthStateChanged:signed_in: " + currentUser.getUid());
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }









}
