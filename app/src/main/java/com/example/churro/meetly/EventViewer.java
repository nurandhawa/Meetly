package com.example.churro.meetly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.nio.channels.Channel;
import java.util.Calendar;
import java.util.Date;


public class EventViewer extends ActionBarActivity {

    private GoogleMap mMap;

    public static TextView textViewName;
    public static TextView textViewStart;
    public static TextView textViewEnd;
    public static TextView textViewTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_viewer_screen);

        String name = getIntent().getExtras().getString("NAME");
        textViewName = (TextView) findViewById(R.id.eventName);
        textViewName.setText(name);

        int day = getIntent().getExtras().getInt("DAY");
        int month = getIntent().getExtras().getInt("MON");
        int year = getIntent().getExtras().getInt("YEAR");

        int startHour = getIntent().getExtras().getInt("SH");
        int startMin = getIntent().getExtras().getInt("SM");

        int endHour = getIntent().getExtras().getInt("EH");
        int endMin = getIntent().getExtras().getInt("EM");

        String date = Integer.toString(year) + "/" + Integer.toString(month) + "/" + Integer.toString(day);
        String sTime = Integer.toString(startHour) + ":" + Integer.toString(startMin);
        String eTime = Integer.toString(endHour) + ":" + Integer.toString(endMin);

        textViewStart = (TextView) findViewById(R.id.startTime);
        textViewStart.setText("Event Starts: " + date + " @" + sTime);

        textViewEnd = (TextView) findViewById(R.id.endTime);
        textViewEnd.setText("Event Ends: " + date + " @" + eTime);

        String duration = getIntent().getExtras().getString("TIME");
        textViewTime = (TextView) findViewById(R.id.timeLeft);
        textViewTime.setText(duration);

        double lat = getIntent().getExtras().getDouble("LAT");
        double lng = getIntent().getExtras().getDouble("LNG");
        final LatLng location = new LatLng(lat,lng);


        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.addMarker(new MarkerOptions().position(location).title("Event Here"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 12);
        mMap.animateCamera(update);

        startEditActivity();
    }

    private void startEditActivity() {
        Button startEdit = (Button) findViewById(R.id.editButton);
        startEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventViewer.this, EventEditor.class);
                Bundle b = getIntent().getExtras();
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        this.startActivity(new Intent(EventViewer.this, EventList.class));
    }


}