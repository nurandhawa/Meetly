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
        new Thread(new Runnable() {

            @Override
            public void run() {
                loadMeetlyEvents();
            }
        }).start();

        Collections.sort(myEvents, new EventListComparator());
        populateListView();
        makeItemsClickable();
    }

    private void loadMeetlyEvents() {

        MeetlyServer server = AppState.getServer();

        try {
            myEvents = server.fetchEventsAfter(1);
            for (MeetlyServer.MeetlyEvent e : server.fetchEventsAfter(1)) {
                Log.i("DBTester", "Event " + e.title);
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
            if (event1.startTime.get(Calendar.MONTH) > event2.startTime.get(Calendar.MONTH)) {
                return 1;
            } else if (event1.startTime.get(Calendar.MONTH) < event2.startTime.get(Calendar.MONTH)) {
                return -1;
            } else {
                if (event1.startTime.get(Calendar.DAY_OF_MONTH) > event2.startTime.get(Calendar.DAY_OF_MONTH)) {
                    return 1;
                } else if (event1.startTime.get(Calendar.DAY_OF_MONTH) < event2.startTime.get(Calendar.DAY_OF_MONTH)) {
                    return -1;
                } else {
                    if (event1.startTime.getTimeInMillis() > event2.startTime.getTimeInMillis()) {
                        return 1;
                    } else if (event1.startTime.getTimeInMillis() < event2.startTime.getTimeInMillis()){
                        return  -1;
                    } else {
                        return 0;
                    }
                }
            }
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

                String name = (eventClicked.title);

//                TextView start = (TextView) findViewById(R.id.startTime);
                String start = (eventClicked.startTime.toString());
                int year = eventClicked.startTime.get(Calendar.YEAR);
                int month = eventClicked.startTime.get(Calendar.MONTH);
                int day = eventClicked.startTime.get(Calendar.DAY_OF_MONTH);
                int startHour = eventClicked.startTime.get(Calendar.HOUR_OF_DAY);
                int startMin = eventClicked.startTime.get(Calendar.MINUTE);
                int startSec = eventClicked.startTime.get(Calendar.SECOND);

//                TextView end = (TextView) findViewById(R.id.endTime);
                String end = (eventClicked.endTime.toString());
                int endHour = eventClicked.endTime.get(Calendar.HOUR_OF_DAY);
                int endMin = eventClicked.endTime.get(Calendar.MINUTE);
                int endSec = eventClicked.endTime.get(Calendar.SECOND);
//                TextView timeRemaining = (TextView) findViewById(R.id.timeLeft);
                Date now = new Date();

                String difference = findTimeRemaining(eventClicked, now);
                //EventViewer.textViewTime.setText(difference);

                double lat = (eventClicked.latitude);
                double lng = (eventClicked.longitude);

                Bundle b = new Bundle();
               // b.putInt("POS" , position);
                b.putString("NAME", name);
                b.putString("START" , start);
                b.putString("END" , end);
                b.putString("TIME", difference);
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

                String message = eventClicked.title;
                Toast.makeText(EventList.this, message, Toast.LENGTH_LONG).show();
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
            startText.setText("THE TEXT START: " + e.startTime);

            // end:
            TextView duration = (TextView) itemView.findViewById(R.id.item_txtEnd);

            long difference = calculateDuration(e);


            long daysPassed = difference / daysInMilli;
            difference = difference % daysInMilli;

            long hoursPassed = difference / hoursInMilli;
            difference = difference % hoursInMilli;

            long minutesPassed = difference / minutesInMilli;
            difference = difference % minutesInMilli;

            long secondsPassed = difference / secondsInMilli;

            duration.setText( daysPassed + " days, " +
                    hoursPassed + " hours, " +
                    minutesPassed + " minutes, " +
                    secondsPassed + " seconds");


            return itemView;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}