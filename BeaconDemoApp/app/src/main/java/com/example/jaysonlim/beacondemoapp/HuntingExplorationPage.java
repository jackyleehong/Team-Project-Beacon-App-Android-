package com.example.jaysonlim.beacondemoapp;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.jaysonlim.beacondemoapp.adapters.BeaconListAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HuntingExplorationPage extends Activity{

    private static final String TAG = HuntingExplorationPage.class.getSimpleName();
    private static final double RELATIVE_START_POS = 320.0 / 1110.0;
    private static final double RELATIVE_STOP_POS = 885.0 / 1110.0;
    public static final String EXTRAS_BEACON = "extrasBeacon";
    private BeaconListAdapter adapter;
    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final String ESTIMOTE_PROXIMITY_UUID2 = "B9407F30-F5F8-466E-AFF9-25556B57FE5F";
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    private BeaconManager beaconManager;
    private  Beacon beacon;
    private Region region;
    int count =1;
    int total = 0;

    private View dotView;
    private int startY = -1;
    private int segmentLength = -1;
    private static final int REQUEST_ENABLE_BT = 1234;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_hunting_exploration_page);

        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(500, 3000);
        beaconManager.setForegroundScanPeriod(700, 500);
    }

    protected void onStart() {
        super.onStart();
        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            connectToService();
        }
    }
    protected void onStop() {
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }

        super.onStop();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                connectToService();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
                getActionBar().setSubtitle("Bluetooth not enabled");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    String temp="";
    private void connectToService() {
        getActionBar().setSubtitle("Scanning...");
        // adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);

                    beaconManager.setRangingListener(new BeaconManager.RangingListener() {

                        @Override
                        public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {

                            getActionBar().setSubtitle("Found beacons: " + beacons.size());
                            for (Beacon beacon : beacons) {


                                findBeaconLocation(beacons, beacon);
                            }
                            for (Beacon beacon : beacons) {
                                String oldUUID = temp;
                                String currentUUID = beacon.getProximityUUID();
                                temp = currentUUID;
                                Toast.makeText(getApplicationContext(), currentUUID + ", " + oldUUID, Toast.LENGTH_LONG).show();
                                if (calculateDistance(beacon.getMeasuredPower(), beacon.getRssi()) < 0.5 && oldUUID != currentUUID && count < beacons.size()) {


                                               /* Random rnd = new Random();
                                    int greenVals = rnd.nextInt(256);
                                    int blueVals = rnd.nextInt(256);*/
                                            Intent claim = new Intent(HuntingExplorationPage.this, BeaconClaimActivity.class);
                                            claim.putExtra("count", count);

                                    /*claim.putExtra("greenVal", greenVals);
                                    claim.putExtra("blueVal", blueVals);*/
                                            startActivity(claim);




                                    Intent getNotified =getIntent();
                                    if( getNotified.getExtras() != null){
                                       //count = getNotified.getIntExtra("count",0);
                                        displayColorForPaperBoat(count,getNotified);
                                        //count++;
                                    }


                                }






                                // count++;
                                //reset(count);
                              /*  try {
                                    beaconManager.stopRanging(new Region("rid", beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor()));
                                } catch (RemoteException e) {
                                    e.printStackTrace();

                                }*/
                            }
                        }


                    });
                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }
    public void findBeaconLocation(List<Beacon> beacons, final Beacon beacon) {
        int repeat = beacons.size();
        String tempId = beacon.getProximityUUID();
        String Id = beacon.getProximityUUID();
        int Major = beacon.getMajor();
        String Mac = beacon.getMacAddress();
        int Minor = beacon.getMinor();


        // do {
        final View view = findViewById(R.id.sonar);
        view.getViewTreeObserver().
                addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        startY = (int) (RELATIVE_START_POS * view.getMeasuredHeight());
                        int stopY = (int) (RELATIVE_STOP_POS * view.getMeasuredHeight());
                        segmentLength = stopY - startY;
                        //ImageView dotView = new ImageView(HuntingExplorationPage.this);
                        // dotView.setImageResource(R.drawable.dot);
                        dotView = findViewById(R.id.dot);
                        dotView.setVisibility(View.VISIBLE);
                        dotView.setTranslationY(computeDotPosY(beacon));


                    }

                });
        // updateDistanceView(beacon);


        //  } while (count <= beacons.size());
    }
    public void displayColorForPaperBoat( int count ,Intent getNotified){

        final  int green = getNotified.getIntExtra("greenVal", 0);
        final int blue = getNotified.getIntExtra("blueVal",0);

        List<ImageView> paperBoats = new ArrayList<ImageView>();
        paperBoats.add((ImageView) findViewById(R.id.paperBoats1));
        paperBoats.add((ImageView) findViewById(R.id.paperBoats2));
        paperBoats.add((ImageView) findViewById(R.id.paperBoats3));
        paperBoats.add((ImageView) findViewById(R.id.paperBoats4));
        paperBoats.add((ImageView) findViewById(R.id.paperBoats5));
        paperBoats.add((ImageView) findViewById(R.id.paperBoats6));


        ImageView[] arrPB = new ImageView[paperBoats.size()];
        paperBoats.toArray(arrPB);



        if (count < 6 ) {
            EditText et = (EditText)findViewById(R.id.counter);
            int counter = count;
            et.setText("" + counter + "");
            arrPB[count-1].setColorFilter(Color.rgb(255, green, blue));

        } else {
            finishHunting();
        }
    }



  /*  public int reset(int count){
        return count = 0;
    }*/

    public void finishHunting(){
        Intent last = new Intent(HuntingExplorationPage.this,CongratulationDisplayActivity.class);
        startActivity(last);
    }




    private void updateDistanceView(Beacon foundBeacon) {
        if (segmentLength == -1) {
            return;
        }

        dotView.animate().translationY(computeDotPosY(foundBeacon)).start();
    }

    private int computeDotPosY(Beacon beacon) {
        // Let's put dot at the end of the scale when it's further than 6m.
        double distance = Math.min(Utils.computeAccuracy(beacon), 6.0);
        return startY + (int) (segmentLength * (distance / 6.0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hunting_exploration_page, menu);
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
    protected static double calculateDistance(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine distance, return -1.
        }

        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }
  /*  public int countOccurenceOfBeacon(String oldUUID, String currentUUID){
        if(oldUUID == currentUUID){
            count++;

        }else{
            count = 1;
        }
        return count;
    }*/



}

