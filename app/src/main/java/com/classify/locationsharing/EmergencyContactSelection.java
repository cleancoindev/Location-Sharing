package com.classify.locationsharing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class EmergencyContactSelection extends AppCompatActivity {


    String userEmail,uidd;
    private ArrayList<Users> Trustedcontacts = new ArrayList<>();

    CardView mCardView;
    CardView mCard1;
    CardView mCard2;
    CardView mCard3;

    ImageView imag1;
    ImageView imag2;
    ImageView imag3;

    TextView textName1;
    TextView textName2;
    TextView textName3;

    TextView textNum1;
    TextView textNum2;
    TextView textNum3;

    Button submit;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabaseUsers;
    DatabaseReference mDatabaseTrustedContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact_selection);


        submit = (Button)findViewById(R.id.button);

        mDatabaseTrustedContacts = FirebaseDatabase.getInstance().getReference().child("TrustedContacts");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(EmergencyContactSelection.this,MainActivity.class));
                }
                else
                {
                    userEmail = firebaseAuth.getCurrentUser().getEmail();
                    uidd = firebaseAuth.getCurrentUser().getUid();
                    retrieveTustedContactsOnCard();
                }
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseTrustedContacts.child(uidd).child("1").child("msg").setValue("Emergency Message");
                mDatabaseTrustedContacts.child(uidd).child("2").child("msg").setValue("Emergency Message");
                mDatabaseTrustedContacts.child(uidd).child("3").child("msg").setValue("Emergency Message");
                Intent intent = new Intent(EmergencyContactSelection.this,main_screen.class);
                startActivity(intent);
            }
        });



        mCard1 = (CardView)findViewById(R.id.card1);
        mCard2 = (CardView)findViewById(R.id.card2);
        mCard3 = (CardView)findViewById(R.id.card3);

        imag1 = (ImageView)findViewById(R.id.imageView1);
        imag2 = (ImageView)findViewById(R.id.imageView2);
        imag3 = (ImageView)findViewById(R.id.imageView3);

        textName1 = (TextView)findViewById(R.id.em_con_1_name);
        textName2 = (TextView)findViewById(R.id.em_con_2_name);
        textName3 = (TextView)findViewById(R.id.em_con_3_name);

        textNum1 = (TextView)findViewById(R.id.em_con_1_no);
        textNum2 = (TextView)findViewById(R.id.em_con_2_no);
        textNum3 = (TextView)findViewById(R.id.em_con_3_no);

        mCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card_1 = "1";
                String no ="1";
                Intent i = new Intent(EmergencyContactSelection.this,ContactList.class);
                i.putExtra("num1",card_1);
                i.putExtra("no",no);
                startActivity(i);

            }
        });
        mCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card_2 = "2";
                String no ="1";
                Intent i = new Intent(EmergencyContactSelection.this,ContactList.class);
                i.putExtra("num1",card_2);
                i.putExtra("no",no);
                startActivity(i);
            }
        });
        mCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card_3 = "3";
                String no ="1";
                Intent i = new Intent(EmergencyContactSelection.this,ContactList.class);
                i.putExtra("num1",card_3);
                i.putExtra("no",no);
                startActivity(i);
            }
        });


    }







    public void retrieveTustedContactsOnCard()
    {

        mDatabaseTrustedContacts.child(uidd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Trustedcontacts.clear();
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                    Users user = postsnapshot.getValue(Users.class);
                    Trustedcontacts.add(user);
                }
                textName1.setText(Trustedcontacts.get(0).getName());
                textName2.setText(Trustedcontacts.get(1).getName());
                textName3.setText(Trustedcontacts.get(2).getName());

                textNum1.setText(Trustedcontacts.get(0).getMob());
                textNum2.setText(Trustedcontacts.get(1).getMob());
                textNum3.setText(Trustedcontacts.get(2).getMob());

                Picasso.with(EmergencyContactSelection.this).load(Trustedcontacts.get(0).getPhotourl()).into(imag1);
                Picasso.with(EmergencyContactSelection.this).load(Trustedcontacts.get(1).getPhotourl()).into(imag2);
                Picasso.with(EmergencyContactSelection.this).load(Trustedcontacts.get(2).getPhotourl()).into(imag3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }
}
