package com.example.jaysonlim.beacondemoapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.Activity;


public class HuntingStartPage extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunting_start_page);
        Button start = (Button)findViewById(R.id.btnStart);
        start.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
        Intent explorationPage = new Intent(HuntingStartPage.this,HuntingExplorationPage.class);
       // explorationPage.putExtra(ListBeaconsActivity.EXTRAS_TARGET_ACTIVITY,HuntingExplorationPage.class.getName());
        startActivity(explorationPage);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hunting_start_page, menu);
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
