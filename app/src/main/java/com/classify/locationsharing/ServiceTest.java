package com.classify.locationsharing;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ADMIN on 04-04-2018.
 */

public class ServiceTest extends Service implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    LocationRequest mLocationrequest;
    GoogleApiClient mgoogleapiclient;
    DatabaseHandler db;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    String uid;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mgoogleapiclient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mgoogleapiclient.connect();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new DatabaseHandler(this);
        /*if (intent != null && intent.getExtras() != null){
            //uid = intent.getStringExtra("uid");
            uid = db.getUids();
        }*/
        if(intent==null)
        {
            db.updateGrant("true");
        }
        uid = db.getUids();
        Log.d("Uid_msg",uid+"");
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationrequest = LocationRequest.create();
        mLocationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationrequest.setInterval(500);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleapiclient, mLocationrequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
        {
            Double Lat = location.getLatitude();
            Double Log = location.getLongitude();
            mRef.child("LocationUser").child(uid).child("latitude").setValue(Lat+"");
            mRef.child("LocationUser").child(uid).child("longitude").setValue(Log+"");
            android.util.Log.d("latndlon1", Lat+" "+Log);
        }
    }

    public void onDestroy() {/*
        Intent intent = new Intent("com.classify.locationsharing");
        intent.putExtra("uid", Globalshare.uid);
        sendBroadcast(intent);*/
        super.onDestroy();
    }
}

