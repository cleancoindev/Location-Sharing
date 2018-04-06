package com.classify.locationsharing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AddMember extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ArrayList<Users> contacts = new ArrayList<>();
    DatabaseReference mDatabaseContacts,mDatabseUsers;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String UserEmail;
    private String User_No;
    private String User_Mobile;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        back = (ImageView)findViewById(R.id.backs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMember.this,SingleMap.class));
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.add_mem);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()== null)
                {
                    startActivity(new Intent(AddMember.this,MainActivity.class));
                }
                else
                {
                    UserEmail = firebaseAuth.getCurrentUser().getEmail();
                    fetchInfo();
                }
            }
        };
    }


    public void fetchInfo()
    {
        mDatabseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = mDatabseUsers.orderByChild("email").equalTo(UserEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postsnapshot : dataSnapshot.getChildren())
                {
                    User_Mobile = (String)postsnapshot.child("mob").getValue();
                    contacts.clear();
                    mDatabaseContacts = FirebaseDatabase.getInstance().getReference().child("contacts").child(Globalshare.uid).child(Globalshare.uid);
                    Query query1 = mDatabaseContacts.orderByChild("name");
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                            {
                                String email = (String)postSnapshot.child("email").getValue();
                                final String reqs = (String)postSnapshot.child("request").getValue();
                                Log.d("hey1232",reqs + " he");
                                /*if(reqs.equals("Granted"))
                                {*/
                                Query query2 = mDatabseUsers.orderByChild("email").equalTo(email);
                                query2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for(DataSnapshot postsnapshot:dataSnapshot.getChildren())
                                        {
                                            int flag1=0;
                                            Users user = postsnapshot.getValue(Users.class);

                                            if(!(user.getMob()+"").equals(User_Mobile) && (reqs+"").equals("Granted"))
                                            {
                                                for(Users users:contacts)
                                                {
                                                    if(user.getMob().equals(users.getMob()))
                                                    {
                                                        flag1 = 1;
                                                    }
                                                }
                                                if(flag1==0) {
                                                    contacts.add(user);
                                                    Log.d("Firebasestate", user.getEmail());
                                                }
                                            }
                                        }
                                        mRecyclerView.setAdapter(new adapter(mRecyclerView.getContext(),contacts,AddMember.this));
                                        setUpRecyclerView(mRecyclerView);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                //}
                            }

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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void setUpRecyclerView(RecyclerView rv) {
        Log.d("Firebase-data","user adapter");
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new adapter(rv.getContext(),contacts, this));
    }

    private class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {

        ArrayList<Users> contacts=new ArrayList<>();
        Context context;
        Activity activity;

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView name;
            private TextView phoneNumber;
            private ImageView imgView;
            private CardView cardView;
            private CheckBox chk;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView)itemView.findViewById(R.id.cardname);
                phoneNumber = (TextView)itemView.findViewById(R.id.msg);
                chk = (CheckBox)itemView.findViewById(R.id.chkbox);
                imgView = (ImageView)itemView.findViewById(R.id.profilepic);
                cardView = (CardView)itemView.findViewById(R.id.cardView_select);
            }
        }

        public adapter( Context context,ArrayList<Users> contacts, Activity activity) {
            this.contacts = contacts;
            this.context = context;
            this.activity = activity;
        }

        @Override
        public adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.select_list, parent, false);
            return  new adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.name.setText(contacts.get(position).getName());
            holder.phoneNumber.setText(contacts.get(position).getMob());
            Picasso.with(context).load(contacts.get(position).getPhotourl()).into(holder.imgView);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Globalshare.uid_global.contains(contacts.get(position).getUid()))
                    {
                        Globalshare.uid_global.remove(contacts.get(position).getUid());
                        holder.chk.setChecked(false);
                    }
                    else
                    {
                        Globalshare.uid_global.add(contacts.get(position).getUid());
                        holder.chk.setChecked(true);
                    }


                }
            });

            if(Globalshare.uid_global.contains(contacts.get(position).getUid()))
            {
                holder.chk.setChecked(true);
            }

        }


        @Override
        public int getItemCount() {
            return contacts.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddMember.this,SingleMap.class));
    }
}
