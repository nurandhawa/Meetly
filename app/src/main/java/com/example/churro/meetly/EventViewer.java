package com.example.churro.meetly;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class EventViewer extends ActionBarActivity {
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
	}



}
