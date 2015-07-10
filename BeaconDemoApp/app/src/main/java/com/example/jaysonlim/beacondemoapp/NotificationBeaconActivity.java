package com.example.jaysonlim.beacondemoapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.example.jaysonlim.beacondemoapp.notification.*;

/**
 * Created by jaysonlim on 7/6/15.
 */
public class NotificationBeaconActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, EstimoteService.class));

        EstimoteManager.Create((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE), this, new Intent(NotificationBeaconActivity.this,NotificationBeaconActivity.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Intent startServiceIntent = new Intent(this, EstimoteReceiver.class);
        //this.startService(startServiceIntent);
        //Intent startServiceIntent = new Intent(this, EstimoteService.class);
        //this.startService(startServiceIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED");
        this.registerReceiver(new EstimoteReceiver(), filter);
    }
}
