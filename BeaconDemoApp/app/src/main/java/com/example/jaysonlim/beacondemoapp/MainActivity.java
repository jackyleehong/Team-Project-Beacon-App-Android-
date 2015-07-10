package com.example.jaysonlim.beacondemoapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.estimote.sdk.EstimoteSDK;
import com.example.jaysonlim.beacondemoapp.notification.EstimoteManager;
import com.example.jaysonlim.beacondemoapp.notification.EstimoteReceiver;
import com.example.jaysonlim.beacondemoapp.notification.EstimoteService;

import android.content.Intent;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.*;

public class MainActivity extends Activity{
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EstimoteSDK.initialize(this, "beacondemoapp", "ccb71d02516ab7703ac526c127430d36");

        // Configure verbose debug logging.
        EstimoteSDK.enableDebugLogging(true);



//TestActivity

        startService(new Intent(this, EstimoteService.class));

        EstimoteManager.Create((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE), this, new Intent(MainActivity.this, MainActivity.class));


        findViewById(R.id.btnScanBeacon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListBeaconsActivity.class);
                intent.putExtra(ListBeaconsActivity.EXTRAS_TARGET_ACTIVITY, DistanceBeaconActivity.class.getName());
                startActivity(intent);
            }
        });

        findViewById(R.id.btnNear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListNearablesActivity.class);
                intent.putExtra(ListNearablesActivity.EXTRAS_TARGET_ACTIVITY, NearablesDemoActivity.class.getName());
                startActivity(intent);
            }
        });

        findViewById(R.id.btnNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListNearablesActivity.class);
                startActivity(intent);
            }
        });

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */


    @Override
    protected void onDestroy() {
       // Log.w(TAG, "onDestroy !!!");

        super.onDestroy();
    }
    @Override
    protected void onStop() {
      //  Log.w(TAG, "onStop !!!");
       // startService(new Intent(this, EstimoteService.class));

      //  EstimoteManager.stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED");
        this.registerReceiver(new EstimoteReceiver(), filter);
    }
}
