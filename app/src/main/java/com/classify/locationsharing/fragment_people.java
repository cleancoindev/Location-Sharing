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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    DatabaseReference mDatabaseRequest,mDatabaseContacts;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String UserEmail,uid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseRequest = FirebaseDatabase.getInstance().getReference().child("Request");
        mDatabaseContacts = FirebaseDatabase.getInstance().getReference().child("contacts");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    UserEmail = firebaseAuth.getCurrentUser().getEmail();
                    uid = firebaseAuth.getCurrentUser().getUid();
                    fetchInfo();
                }
                else {
                    startActivity(new Intent(getContext(),MainActivity.class));
                }
            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void fetchInfo() {

        mDatabaseRequest.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contacts.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users user = snapshot.getValue(Users.class);
                    Log.d("hey123",user.getUid() + " 11");
                    Log.d("hey123",user.getNo() + " 12");
                    Log.d("hey123",user.getRequest() + " 13");
                    if(!(user.getUid()+"").equals(null))
                    {
                        contacts.add(user);
                    }


                }
                mRecyclerView.setAdapter(new adapter(getContext(),contacts,getActivity()));
                setUpRecyclerView(mRecyclerView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView)inflater.inflate(R.layout.people_fragment,container,false);
        setUpRecyclerView(mRecyclerView);
        return mRecyclerView;
    }

    public static Fragment newInstance(int i) {
        fragment_people yf = new fragment_people();
        return yf;
    }


    public void setUpRecyclerView(RecyclerView rv) {
        Log.d("Firebase-data","user adapter");
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new adapter(rv.getContext(),contacts, getActivity()) );
    }

    private class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {

        ArrayList<Users> contacts=new ArrayList<>();
        Context context;
        Activity activity;

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView name,phone,accept;
            private ImageView confirm,cancel;
            private ImageView imgView;
            private CardView cardView;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView)itemView.findViewById(R.id.cardname);
                phone = (TextView)itemView.findViewById(R.id.number);
                accept = (TextView)itemView.findViewById(R.id.accept);
                imgView = (ImageView)itemView.findViewById(R.id.profilepic);
                cardView = (CardView)itemView.findViewById(R.id.cardView);
                confirm = (ImageView) itemView.findViewById(R.id.requests);
                cancel = (ImageView) itemView.findViewById(R.id.cancels);
            }
        }

        public adapter( Context context,ArrayList<Users> contacts, Activity activity) {
            this.contacts = contacts;
            this.context = context;
            this.activity = activity;
        }

        @Override
        public adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_card, parent, false);
            return  new adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.name.setText(contacts.get(position).getName());
            holder.phone.setText(contacts.get(position).getMob());
            Picasso.with(context).load(contacts.get(position).getPhotourl()).into(holder.imgView);

            if(contacts.get(position).getRequest().equals("Accept"))
            {
                holder.accept.setVisibility(View.VISIBLE);
                holder.cancel.setVisibility(View.GONE);
                holder.confirm.setVisibility(View.GONE);
            }


            holder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabaseRequest.child(uid).child(contacts.get(position).getKey()).child("request").setValue("Accept");
                    mDatabaseContacts.child(contacts.get(position).getUid()).child(contacts.get(position).getUid()).child(contacts.get(position).getNo()).child("request").setValue("Granted");
                }
            });
        }


        @Override
        public int getItemCount() {
            return contacts.size();
        }
    }




}