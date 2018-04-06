package com.classify.locationsharing;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

public class ContactToRequest extends AppCompatActivity {

    RecyclerView mRecyclerView;
    private ArrayList<Users> contacts = new ArrayList<>();
    ArrayList<Users> selected_contacts = new ArrayList<>();


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private adapter adap;
    String username,useremail,uid;

    DatabaseReference mDatabaseUsers;
    DatabaseReference mDatabaseContacts;
    DatabaseReference mDatabaseContacts2;
    private String User_No;
    private String User_Mobile;
    private String email;
    private String name;
    private String mobs;
    private String no;
    private String no1;
    private String photourl;
    private String photo_url;
    ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        adap = new adapter(contacts);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_contctlist_Emergency);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        refresh = (ImageView)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContactToRequest.this,SyncContacts.class);
                i.putExtra("refresh","1");
                startActivity(i);
            }
        });

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseContacts = FirebaseDatabase.getInstance().getReference().child("contacts");
        mDatabaseContacts2 = FirebaseDatabase.getInstance().getReference().child("contacts");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ContactToRequest.this, MainActivity.class));
                } else {
                    username = firebaseAuth.getCurrentUser().getDisplayName();
                    useremail = firebaseAuth.getCurrentUser().getEmail();
                    uid = firebaseAuth.getCurrentUser().getUid();
                    fetchInfo();
                }

            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    private void fetchInfo() {

        Query query = mDatabaseUsers.orderByChild("email").equalTo(useremail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                    User_Mobile = (String) postsnapshot.child("mob").getValue();
                    photourl = (String) postsnapshot.child("photourl").getValue();
                    Log.d("hey",User_Mobile + "12");

                    mDatabaseContacts = FirebaseDatabase.getInstance().getReference().child("contacts").child(uid).child(uid);
                    Query query1 = mDatabaseContacts.orderByChild("name");
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("hey","12");
                            contacts.clear();

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String email = (String) postSnapshot.child("email").getValue();
                                int flag1 = 0;

                                Users user = postSnapshot.getValue(Users.class);
                                if (!(user.getMob()+"").equals(User_Mobile))
                                {
                                    for (Users users : contacts) {
                                        if ((user.getMob()+"").equals(users.getMob())) {
                                            flag1 = 1;
                                        }
                                    }
                                    if (flag1 == 0) {
                                        Log.d("hey","13");
                                        contacts.add(user);
                                    }
                                }



                            }

                            mRecyclerView.setAdapter(adap);

                            //setUpRecyclerView(mRecyclerView);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setUpRecyclerView(RecyclerView rv) {
        Log.d("Firebase-data", "user adapter");
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new adapter(contacts));
    }


    private class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {

       // ArrayList<Users> contacts = new ArrayList<>();
        int flag;


        public adapter(ArrayList<Users> contacts) {
            //this.contacts = contacts;
            flag = 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            CardView selectView;
            TextView name, num;
            Button request;
            ImageView profilepic;


            public ViewHolder(View itemView) {
                super(itemView);
                selectView = (CardView) itemView.findViewById(R.id.cardView);
                name = (TextView) itemView.findViewById(R.id.cardname);
                num = (TextView) itemView.findViewById(R.id.number);
                profilepic = (ImageView) itemView.findViewById(R.id.profilepic);
                request = (Button) itemView.findViewById(R.id.requests);
            }
        }


        @Override
        public adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
            return new adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final adapter.ViewHolder holder, final int position) {
            holder.name.setText(contacts.get(position).getName());
            holder.num.setText(contacts.get(position).getMob());


            if(contacts.get(position).getRequest().equals("Request sent"))
            {
                holder.request.setText("Requested");
                holder.request.setEnabled(false);
                holder.request.setBackground(Drawable.createFromPath("#000000"));
                holder.request.setTextColor(Color.parseColor("#3f6af1"));
            }
            if(contacts.get(position).getRequest().equals("Granted"))
            {
                holder.request.setText("Granted");
                holder.request.setEnabled(false);
                holder.request.setBackground(Drawable.createFromPath("#000000"));
                holder.request.setTextColor(Color.parseColor("#3f6af1"));
            }
            Picasso.with(getParent()).load(contacts.get(position).getPhotourl()).into(holder.profilepic);
            /*holder.selectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.chkbox.performClick();
                }
            });*/

            holder.request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabaseContacts2.child(uid).child(uid).child(contacts.get(position).getNo()).child("request").setValue("Request sent");
                    DatabaseReference mDatabaseRequest = FirebaseDatabase.getInstance().getReference().child("Request").child(contacts.get(position).getUid());
                    DatabaseReference mref = mDatabaseRequest.push();

                    mref.child("name").setValue(username);
                    mref.child("mob").setValue(User_Mobile);
                    mref.child("email").setValue(useremail);
                    mref.child("request").setValue("Requested");
                    mref.child("uid").setValue(uid);
                    mref.child("photourl").setValue(photourl);
                    mref.child("key").setValue(mref.getKey());
                    mref.child("no").setValue(contacts.get(position).getNo());
                    adap.notifyDataSetChanged();
//                    contacts.get(position).setRequest("Requested");
                    /*holder.request.setText("Requested");
                    holder.request.setEnabled(false);
                    holder.request.setBackground(Drawable.createFromPath("#000000"));
                    holder.request.setTextColor(Color.parseColor("#3f6af1"));
*/
                }
            });

        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }


    }

}