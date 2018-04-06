package com.classify.locationsharing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ADMIN on 04-04-2018.
 */
public class ReceiverCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String uid = intent.getExtras().getString("uid");
        Intent i =new Intent(context,ServiceTest.class);
        i.putExtra("uid","1122");
        context.startService(i);
        //context.startService(new Intent(context, ServiceTest.class));
    }

}

