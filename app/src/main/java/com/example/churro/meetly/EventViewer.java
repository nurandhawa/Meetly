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

        String start = getIntent().getExtras().getString("START");
        textViewStart = (TextView) findViewById(R.id.startTime);
        textViewStart.setText(start);

        String end = getIntent().getExtras().getString("END");
        textViewEnd = (TextView) findViewById(R.id.endTime);
        textViewEnd.setText(end);

        String duration = getIntent().getExtras().getString("TIME");
        textViewTime = (TextView) findViewById(R.id.timeLeft);
        textViewTime.setText("Starts in: " + duration);

        double lat = getIntent().getExtras().getDouble("LAT");
        double lng = getIntent().getExtras().getDouble("LNG");
        final LatLng location = new LatLng(lat,lng);


        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.addMarker(new MarkerOptions().position(location).title("Event Here"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 12);
        mMap.animateCamera(update);


        int position = getIntent().getExtras().getInt("POS");

        startEditActivity(location , position);
    }

    private void startEditActivity(LatLng location, int position) {
        Button startEdit = (Button) findViewById(R.id.editButton);
        final double lat = location.latitude;
        final double lng = location.longitude;
        final int pos = position;
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