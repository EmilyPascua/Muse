package com.cafemanager.muse.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cafemanager.muse.Home.HomeActivity;
import com.cafemanager.muse.Model.User;
import com.cafemanager.muse.R;
import com.cafemanager.muse.Utils.FirebaseMethods.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods mFirebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMyRef;

    private Context mContext;
    private String email, username, password;
    private EditText mEmail, mPassword, mUsername;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    private String append = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        mFirebaseMethods = new FirebaseMethods(mContext);
        Log.d(TAG, "onCreate: started.");

        initWidgets();
        setupFirebaseAuth();
        init();
    }

    private void init(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                if(checkInputs(email, username, password)){
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadingPleaseWait.setVisibility(View.VISIBLE);

                    mFirebaseMethods.registerNewEmail(email, password, username);
                }
            }
        });
    }

    private boolean checkInputs(String email, String username, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * Initialize the activity widgets
     */
    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initializing Widgets.");
        mEmail = (EditText) findViewById(R.id.input_email);
        mUsername = (EditText) findViewById(R.id.input_username);
        btnRegister = (Button) findViewById(R.id.btn_register);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingPleaseWait = (TextView) findViewById(R.id.pleaseWait);
        mPassword = (EditText) findViewById(R.id.input_password);
        mContext = RegisterActivity.this;
        mProgressBar.setVisibility(View.GONE);
        loadingPleaseWait.setVisibility(View.GONE);

    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Check is @param username already exists in teh database
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.firebase_users))
                .orderByChild(getString(R.string.firebase_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        append = mMyRef.push().getKey().substring(3,10);
                        Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
                    }
                }

                String mUsername = "";
                mUsername = username + append;

                //add new user to the database
                mFirebaseMethods.addNewUser(email, mUsername, "", "", "https://d30womf5coomej.cloudfront.net/ua/defaultuser.png");

                Toast.makeText(mContext, "Signup successful.", Toast.LENGTH_SHORT).show();

                mAuth.signOut();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMyRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    mMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkIfUsernameExists(username);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//         Log.d(TAG, "onCreated: started.");
//        mContext = RegisterActivity.this;
//
//        mFirebaseMethods = new FirebaseMethods(mContext);
//
//
//        initWidgets();
//        setupFirebaseAuth();
//        initRegisterButton();
//    }
//
//    private void initRegisterButton(){
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                email = mEmail.getText().toString();
//                username = mUsername.getText().toString();
//                password = mPassword.getText().toString();
//
//                if(checkInputs(email,username,password)){
//                    mProgressBar.setVisibility(View.VISIBLE);
//                    loadingPleaseWait.setVisibility(View.VISIBLE);
//
//                    mFirebaseMethods.registerNewEmail(email, password, username);
//                }
//
//            }
//        });
//    }
//
//    private boolean checkInputs(String email, String username, String password){
//        Log.d(TAG, "checkInputs: checking inputs for null values.");
//        if(email.equals("") || username.equals("") || password.equals("")){
//            Toast.makeText(mContext, "All Fields Must Be Filled Out", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private void initWidgets(){
//        Log.d(TAG, "initWidgets: initializing widgets");
//        mEmail = (EditText) findViewById(R.id.input_email);
//        mPassword = (EditText) findViewById(R.id.input_password);
//        mUsername = (EditText) findViewById(R.id.input_username);
//        loadingPleaseWait = (TextView) findViewById(R.id.pleaseWait);
//        btnRegister = (Button) findViewById(R.id.btn_register);
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//        mProgressBar.setVisibility(View.GONE);
//        loadingPleaseWait.setVisibility(View.GONE);
//
//    }
//
//    private boolean isStringNull(String string){
//        Log.d(TAG, "isStringNull: checking if string is null");
//
//        if(string.equals("")){
//            return true;
//        }
//        return false;
//    }
//
//        /*
//    ---------------------------------------- Firebase ------------------------------
//    */
//
//    /**
//     * Check is @param username already exists in teh database
//     * @param username
//     */
//    private void checkIfUsernameExists(final String username) {
//        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference
//                .child(getString(R.string.firebase_users))
//                .orderByChild(getString(R.string.firebase_username))
//                .equalTo(username);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
//                    if (singleSnapshot.exists()){
//                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
//                        append = mMyRef.push().getKey().substring(3,10);
//                        Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
//                    }
//                }
//                String mUsername = "";
//                mUsername = username + append;
//                //add new user to the database
//                mFirebaseMethods.addNewUser(email, mUsername, "", "", "");
//                Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();
//                mAuth.signOut();
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }
//
//
//
//
//    private void setupFirebaseAuth(){
//        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
//
//        mAuth = FirebaseAuth.getInstance();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mMyRef = mFirebaseDatabase.getReference();
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if (user != null){
//                    Log.d(TAG, "onAuthStateChanged: signed_in");
//
//
//                    mMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            //check if username is already in use
//                            checkIfUsernameExists(username);
//
//                            //add new user to database
//                            mFirebaseMethods.addNewUser(email, username, "", "",
//                                    "https://d30womf5coomej.cloudfront.net/ua/defaultuser.png");
//
//                            //add new user account_settings to database
//                            Toast.makeText(mContext, "Registration successful", Toast.LENGTH_SHORT).show();
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                    finish();
//                } else {
//                    Log.d(TAG, "onAuthStateChanged: signed_out");
//                }
//
//
//            }
//        };
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//        // Check if user is signed in (non-null) and update UI accordingly.
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        if (mAuthListener != null){
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//
//    }
//
//
//
//}
