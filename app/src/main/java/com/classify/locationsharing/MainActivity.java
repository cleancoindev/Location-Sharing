package com.classify.locationsharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private SignInButton Gbutton;
    private static final int RC_SIGN_IN = 1;
    int flags=0;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Main_Activity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    String name;
    String email;
    String photourl;
    String firebase_name;
    String total;
    int total1;

    DatabaseReference mDatabaseUsers;
    DatabaseReference mDatabaseLocationUsers;
    DatabaseReference mDatabaseContacts;
    DatabaseReference mDatabaseGroupChat;
    DatabaseReference mDatabaseTrustedContacts;

    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gbutton = (SignInButton) findViewById(R.id.SignIn);

        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    mProgress.dismiss();
                    name = firebaseAuth.getCurrentUser().getDisplayName();
                    email = firebaseAuth.getCurrentUser().getEmail();
                    photourl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();


                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this,"ERROR In Connection",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        Gbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    public void WriteData()
    {

        mDatabaseSingleChat.child(total1+"").child("email").setValue(email);
        mDatabaseSingleChat.child(total1+"").child("no").setValue(total1+"");

        mDatabaseUsers.child(total1+"").child("email").setValue(email);
        mDatabaseUsers.child(total1+"").child("name").setValue(name);
        mDatabaseUsers.child(total1+"").child("photourl").setValue(photourl);
        mDatabaseUsers.child(total1+"").child("no").setValue(total1+"");
        mDatabaseUsers.child(total1+"").child("latitude").setValue("50");
        mDatabaseUsers.child(total1+"").child("longitude").setValue("20");

        mDatabaseLocationUsers.child(total1+"").child("email").setValue(email);
        mDatabaseLocationUsers.child(total1+"").child("name").setValue(name);
        mDatabaseLocationUsers.child(total1+"").child("photourl").setValue(photourl);
        mDatabaseLocationUsers.child(total1+"").child("no").setValue(total1+"");
        mDatabaseLocationUsers.child(total1+"").child("latitude").setValue("50");
        mDatabaseLocationUsers.child(total1+"").child("longitude").setValue("20");

        mDatabaseSingleChat.child(total1+"").child("total").child("no").setValue(0+"");
        mDatabaseSingleChat.child("total").child("no").setValue(total1+"");

        mDatabaseContacts.child(total1+"").child("total").setValue("0");

        mDatabaseGroupChat.child(total1+"").child("email").setValue(email);
        mDatabaseGroupChat.child(total1+"").child("name").setValue(name);
        mDatabaseGroupChat.child(total1+"").child("totalgroups").setValue(0+"");
        mDatabaseGroupChat.child(total1+"").child("user_no").setValue(total1+"");

        mDatabaseTrustedContacts.child(total1+"").child("1").child("email").setValue("Contact_1@gmail.com");
        mDatabaseTrustedContacts.child(total1+"").child("1").child("mob").setValue("9999999999");
        mDatabaseTrustedContacts.child(total1+"").child("1").child("name").setValue("Contact 1");
        mDatabaseTrustedContacts.child(total1+"").child("1").child("photourl").setValue("https://firebasestorage.googleapis.com/v0/b/talkntrace-37543.appspot.com/o/gallery%2Fcontact_icon.png?alt=media&token=42f686f3-3597-4870-a46c-889af540aa99");
        mDatabaseTrustedContacts.child(total1+"").child("1").child("no").setValue("1");


        mDatabaseTrustedContacts.child(total1+"").child("2").child("email").setValue("Contact_2@gmail.com");
        mDatabaseTrustedContacts.child(total1+"").child("2").child("mob").setValue("9999999999");
        mDatabaseTrustedContacts.child(total1+"").child("2").child("name").setValue("Contact 2");
        mDatabaseTrustedContacts.child(total1+"").child("2").child("photourl").setValue("https://firebasestorage.googleapis.com/v0/b/talkntrace-37543.appspot.com/o/gallery%2Fcontact_icon.png?alt=media&token=42f686f3-3597-4870-a46c-889af540aa99");
        mDatabaseTrustedContacts.child(total1+"").child("2").child("no").setValue("2");

        mDatabaseTrustedContacts.child(total1+"").child("3").child("email").setValue("Contact_3@gmail.com");
        mDatabaseTrustedContacts.child(total1+"").child("3").child("mob").setValue("9999999999");
        mDatabaseTrustedContacts.child(total1+"").child("3").child("name").setValue("Contact 3");
        mDatabaseTrustedContacts.child(total1+"").child("3").child("photourl").setValue("https://firebasestorage.googleapis.com/v0/b/talkntrace-37543.appspot.com/o/gallery%2Fcontact_icon.png?alt=media&token=42f686f3-3597-4870-a46c-889af540aa99");
        mDatabaseTrustedContacts.child(total1+"").child("3").child("no").setValue("3");
/*
        Intent intent = new Intent(Google_Login.this,PhoneNumber.class);
        intent.putExtra("total",total1);
        startActivity(intent);
        Google_Login.this.finish();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn() {
        /*mProgress.setMessage("Logging In");
        mProgress.show();*/

        mProgress = ProgressDialog.show(this, "Please wait",
                "Logging In", true);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else {
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}