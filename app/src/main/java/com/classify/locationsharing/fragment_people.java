package com.classify.locationsharing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
// } interface
 * to handle interaction events.
 * Use the {@link fragment_people#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_people extends Fragment {
    RecyclerView mRecyclerView;
    ArrayList<Users> contacts = new ArrayList<>();
    DatabaseReference mDatabaseContacts, mDatabseUsers;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String UserEmail;
    private String User_No;
    private String User_Mobile;


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    UserEmail = firebaseAuth.getCurrentUser().getEmail();
                    Log.d("Firebasestate", UserEmail);

                }
            }
        };/*
        MainActivity.menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"hi2",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(),SyncContacts.class);
                i.putExtra("refresh","1");
                startActivity(i);
            }
        });*/
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.people_fragment, container, false);
        return v;
    }

    public static Fragment newInstance(int i) {
        fragment_people yf = new fragment_people();
        return yf;
    }



}