package com.cafemanager.muse.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cafemanager.muse.Model.User;
import com.cafemanager.muse.R;
import com.cafemanager.muse.Utils.BottomNavigationViewHelper;
import com.cafemanager.muse.Utils.SearchUsersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity{
    private  static  final String TAG = "SearchActivity";
    private Context mContext = SearchActivity.this;
    private static  final int ACTIVITY_NUM = 1;

    // Widgets
    private EditText mSearchParam;
    private RecyclerView mRecyclerView;

    // Variables
    private ArrayList<User> mUserList;
    private SearchUsersAdapter mSearchUsersAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        Log.d(TAG, "onCreate: started.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRecyclerView = (RecyclerView) findViewById(R.id.view_profile_recycler_view);
        mSearchParam = (EditText) findViewById(R.id.search_user);

        mUserList = new ArrayList<>();
        mSearchUsersAdapter = new SearchUsersAdapter(SearchActivity.this, mUserList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSearchUsersAdapter);





        hideSoftKeyboard();
        initTextListener();
        setupBottomNavigationView();
    }

    private void initTextListener(){
        Log.d(TAG, "initTextListener: initializing");

        mUserList = new ArrayList<>();

        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = mSearchParam.getText().toString().toLowerCase(Locale.getDefault());
                searchMatch(text);
            }
        });
    }

    private void updateUsersList() {
        // mUserList is ArrayList
//        mSearchUsersAdapter = new SearchUsersAdapter(SearchActivity.this, mUserList);
//        mRecyclerView.setAdapter(mSearchUsersAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchUsersAdapter.mSearchUsers.clear();
        mSearchUsersAdapter.mSearchUsers.addAll(mUserList);
        mSearchUsersAdapter.notifyDataSetChanged();

    }


    private void searchMatch(String keyword) {
        Log.d(TAG, "Searching for user: " + keyword);
        // Clear list before creating new search results
        mUserList.clear();



        if(keyword.length() > 0) {
            Log.d(TAG, "Keyword length greater than 0, searching...");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Query query = databaseReference.child(getString(R.string.firebase_users))
                    .orderByChild(getString(R.string.firebase_username)).equalTo(keyword);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: Found user: " + singleSnapshot.getValue(User.class).toString());


                        // Getting User object from snapshot via "User.class" (fields must match)
                        mUserList.add(singleSnapshot.getValue(User.class));

                        updateUsersList();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void hideSoftKeyboard() {
        if(getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
