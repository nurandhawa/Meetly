package com.example.churro.meetly;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class EventList extends ActionBarActivity {

    final long secondsInMilli = 1000;
    final long minutesInMilli = secondsInMilli * 60;
    final long hoursInMilli = minutesInMilli * 60;
    final long daysInMilli = hoursInMilli * 24;

    public static List<MeetlyServer.MeetlyEvent> myEvents = new ArrayList<MeetlyServer.MeetlyEvent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_screen);
        setInfo();
        createEventButton();
        createLoginButton();
        createRefreshButton();
        loadEvents();

        Log.i("App Loaded", "Proceed");
    }

    private void loadEvents() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                loadMeetlyEvents();
            }
        }).start();

        Collections.sort(myEvents, new EventListComparator());
        populateListView();
        makeItemsClickable();
        deleteDuplicates();
    }

    private void deleteDuplicates() {
        for (int i = 0; i < myEvents.size()-1; i++) {
            for (int j = 1; j < myEvents.size(); j++) {
                if (myEvents.get(i).equals(myEvents.get(j))) {
                    myEvents.remove(j);
                }
            }
        }
    }

    private void loadMeetlyEvents() {

        MeetlyServer server = AppState.getServer();
        Date now = new Date();
        long nowTime = now.getTime();

        try {
            for (MeetlyServer.MeetlyEvent e : server.fetchEventsAfter(1)) {
                if(e.startTime.getTimeInMillis() - nowTime > 0) {
                    myEvents.add(e);
                }
            }

        } catch (MeetlyServer.FailedFetchException e){
            e.printStackTrace();
        }
    }

    private void setInfo() {
        TextView setName = (TextView)findViewById(R.id.userName);
        String curUser = AppState.getName();
        setName.setText(curUser);

    }

    private void createRefreshButton() {
        Button refreshBtn = (Button) findViewById(R.id.btn_refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEvents();
            }

        });
    }

    private void createLoginButton() {

        Button BLogin = (Button) findViewById(R.id.btn_login);
        BLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventList.this, LoginScreen.class);
                startActivity(intent);
            }
        });

    }

    private void createEventButton() {
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventList.this, EventCreator.class));
            }
        });
    }

    public class EventListComparator implements Comparator<MeetlyServer.MeetlyEvent> {
        public int compare(MeetlyServer.MeetlyEvent event1 , MeetlyServer.MeetlyEvent event2) {
           return event1.startTime.compareTo(event2.startTime);
        }
    }

    private long calculateDuration(MeetlyServer.MeetlyEvent e) {
        long difference = 0;
        difference = e.endTime.getTimeInMillis() - e.startTime.getTimeInMillis();
        return difference;
    }

    private void populateListView() {

        ArrayAdapter<MeetlyServer.MeetlyEvent> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);

    }

    private void makeItemsClickable() {
        ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                MeetlyServer.MeetlyEvent eventClicked = myEvents.get(position);

                String message = eventClicked.title;
                int eventNumber = eventClicked.eventID;

                int year = eventClicked.startTime.get(Calendar.YEAR);
                int month = eventClicked.startTime.get(Calendar.MONTH);
                int day = eventClicked.startTime.get(Calendar.DAY_OF_MONTH);

                int startHour = eventClicked.startTime.get(Calendar.HOUR_OF_DAY);
                int startMin = eventClicked.startTime.get(Calendar.MINUTE);
                int startSec = eventClicked.startTime.get(Calendar.SECOND);

                int endHour = eventClicked.endTime.get(Calendar.HOUR_OF_DAY);
                int endMin = eventClicked.endTime.get(Calendar.MINUTE);
                int endSec = eventClicked.endTime.get(Calendar.SECOND);

                double lat = (eventClicked.latitude);
                double lng = (eventClicked.longitude);

                long difference = calculateDuration(eventClicked);

                long hoursPassed = difference / hoursInMilli;
                difference = difference % hoursInMilli;

                long minutesPassed = difference / minutesInMilli;
                difference = difference % minutesInMilli;

                long secondsPassed = difference / secondsInMilli;

                String time = ( "Event Duration: " + hoursPassed + " hours, " + minutesPassed + " minutes, " +
                        secondsPassed + " seconds");

                Log.i("Event clicked", message);
                Log.i("Event ID: ", String.valueOf(eventNumber));

                Bundle b = new Bundle();

                b.putString("TIME", time);

                b.putString("NAME", message);
                b.putInt("EVENTID", eventNumber);

                b.putDouble("LAT", lat);
                b.putDouble("LNG", lng);

                b.putInt("DAY" , day);
                b.putInt("MON" , month);
                b.putInt("YEAR" , year);

                b.putInt("SH" , startHour);
                b.putInt("SM" , startMin);
                b.putInt("SS" , startSec);

                b.putInt("EH" , endHour);
                b.putInt("EM" , endMin);
                b.putInt("ES" , endSec);

                Intent intent = new Intent (EventList.this, EventViewer.class);
                intent.putExtras(b);

                startActivity(intent);

            }
        });

    }

    private String findTimeRemaining(MeetlyServer.MeetlyEvent eventClicked, Date now) {
        long start = eventClicked.startTime.getTimeInMillis();
        long nowTime = now.getTime();
        long difference = start - nowTime;

        if (difference >= 0) {

            long daysPassed = difference / daysInMilli;
            difference = difference % daysInMilli;

            long hoursPassed = difference / hoursInMilli;
            difference = difference % hoursInMilli;

            long minutesPassed = difference / minutesInMilli;
            difference = difference % minutesInMilli;

            long secondsPassed = difference / secondsInMilli;

            String timeRemaining = daysPassed + " days, " +
                    hoursPassed + " hours, " +
                    minutesPassed + " minutes, " +
                    secondsPassed + " seconds";

            return timeRemaining;
        } else {
            return "Happening now!";
        }
    }

    private class MyListAdapter extends ArrayAdapter<MeetlyServer.MeetlyEvent> {
        public MyListAdapter() {
            super(EventList.this, R.layout.item_list, myEvents);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_list, parent, false);
            }

            // Find the event to work with.
            MeetlyServer.MeetlyEvent e = myEvents.get(position);

            // name:
            TextView titleText = (TextView) itemView.findViewById(R.id.item_txtName);
            titleText.setText("Event Title: " + e.title);

            // start:
            TextView startText = (TextView) itemView.findViewById(R.id.item_txtStart);
            startText.setText("Event Starts:  " + e.startTime.get(Calendar.YEAR) + "/" + e.startTime.get(Calendar.MONTH) +
                    "/" + e.startTime.get(Calendar.DAY_OF_MONTH) + " at: " + e.startTime.get(Calendar.HOUR_OF_DAY) + ":" + e.startTime.get(Calendar.MINUTE));

            // end:

            long difference = calculateDuration(e);


            long daysPassed = difference / daysInMilli;
            difference = difference % daysInMilli;

            long hoursPassed = difference / hoursInMilli;
            difference = difference % hoursInMilli;

            long minutesPassed = difference / minutesInMilli;
            difference = difference % minutesInMilli;

            long secondsPassed = difference / secondsInMilli;

            TextView duration = (TextView) itemView.findViewById(R.id.item_txtEnd);
            duration.setText( "Event Duration: " + hoursPassed + " hours, " + minutesPassed + " minutes, " +
                    secondsPassed + " seconds");


            return itemView;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
