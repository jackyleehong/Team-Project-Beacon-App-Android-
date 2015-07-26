package com.example.jaysonlim.beacondemoapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.estimote.sdk.Beacon;
import com.example.jaysonlim.beacondemoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BeaconClaimActivity extends Activity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beacon_claim);

      /*  Intent color = getIntent();
      final  int green = color.getIntExtra("greenVal", 0);
       final int blue = color.getIntExtra("blueVal",0);*/
        Random rnd = new Random();
       final int greenVals = rnd.nextInt(256);
       final int blueVals = rnd.nextInt(256);
        Intent getCount = getIntent();
      final  int count = getCount.getIntExtra("count", 0);


        ImageView paperBoat = (ImageView)findViewById(R.id.paperBoats);
        paperBoat.setColorFilter(Color.rgb(255, greenVals, blueVals));

        Button claimBtn = (Button)findViewById(R.id.claimBtn);

        claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent notifyHEP = new Intent(BeaconClaimActivity.this, HuntingExplorationPage.class);
                notifyHEP.putExtra("greenVal", greenVals);
                notifyHEP.putExtra("blueVal", blueVals);
                notifyHEP.putExtra("count",count);
                startActivity(notifyHEP);

               /* ImageView firstView = (ImageView) findViewById(R.id.paperBoats1);

                SharedPreferences pref = getSharedPreferences("claim", MODE_PRIVATE);*/


            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beacon_claim, menu);
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
}
