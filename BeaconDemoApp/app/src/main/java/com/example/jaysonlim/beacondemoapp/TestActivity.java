package com.example.jaysonlim.beacondemoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.estimote.sdk.*;
import com.estimote.sdk.BeaconManager.MonitoringListener;
import com.estimote.sdk.Nearable;
import com.example.jaysonlim.beacondemoapp.ListNearablesActivity;
import com.example.jaysonlim.beacondemoapp.R;
import java.util.List;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.*;
/**
 * Created by jaysonlim on 7/4/15.
 */
public class TestActivity extends Activity {

    private BeaconManager beaconManager ;//= new BeaconManager(context);
    private String scanId;
    private static final String TAG = TestActivity.class.getSimpleName();

    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);

    //public static interface BeaconManager.MonitoringListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        beaconManager = new BeaconManager(this);

        // Should be invoked in #onCreate.
        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List<Nearable> nearables) {
                Log.w(TAG, "Discovered nearables: " + nearables);
            }
        });


        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {


                Log.w(TAG, "Ranged beacons: " + beacons);
            }
        });


        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region,
                                 List<Beacon> beacons) {
                Log.w(TAG, "onEnteredRegion beacons: " + beacons);

            }

            @Override
            public void onExitedRegion(Region region) {

                Log.w(TAG, "onExitedRegion : " + region);

            }


        });

        Button orderButton = (Button) findViewById(R.id.button2);
        orderButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                    @Override public void onServiceReady() {
                        try {


                            beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);

                            scanId = beaconManager.startNearableDiscovery();

                            TextView infoText = (TextView) findViewById(R.id.textView3);
                            infoText.setText(scanId);
                            Log.w(TAG, "Discovered nearables scanId : " + scanId);

                            Log.w(TAG,"hahhaa");

                        } catch (RemoteException e) {
                            Log.e(TAG, "Cannot start ranging", e);
                        }
                    }
                });
            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();


        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);

                    scanId = beaconManager.startNearableDiscovery();

                    TextView infoText = (TextView) findViewById(R.id.textView3);
                    infoText.setText(scanId);
                    Log.w(TAG, "Discovered nearables scanId : " + scanId);

                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });

        Log.i(TAG, "On Start .....");
    }


    @Override
    protected void onStop() {
        super.onStop();

        // Should be invoked in #onStop.
       // beaconManager.stopBeaconDiscovery(scanId);
        beaconManager.stopNearableDiscovery(scanId);
        Log.i(TAG, "On Stop .....");


        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When no longer needed. Should be invoked in #onDestroy.
        beaconManager.disconnect();
        Log.i(TAG, "On Destroy .....");
    }
}
