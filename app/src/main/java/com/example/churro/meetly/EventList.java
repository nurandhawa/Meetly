package com.example.churro.meetly;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class EventList extends ActionBarActivity {

    final long secondsInMilli = 1000;
    final long minutesInMilli = secondsInMilli * 60;
    final long hoursInMilli = minutesInMilli * 60;
    final long daysInMilli = hoursInMilli * 24;
    //Variable for saving/loading event information
    int data_block = 100;
    String final_data="";

    public static List<Event> myEvents = new ArrayList<Event>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_screen);
        loadEvent();
        populateList();
        Collections.sort(myEvents, new EventListComparator());

        populateListView();
        makeItemsClickable();

        createEventButton();

    }

    private void loadEvent() {
        final_data = "";
        try {
            FileInputStream fis = openFileInput("events.txt");

            InputStreamReader isr = new InputStreamReader(fis);
            char[] data = new char[data_block];
            int size;
            try {
                while((size = isr.read(data))>0) {
                    String read_data = String.copyValueOf(data, 0, size);
                    final_data+= read_data;
                    data = new char[data_block];
                }
                Log.i("Current Events", final_data);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    //TODO:: get list from other activity
    private void populateList() {
//        Date startDate1 = new Date(115,5,12,6,06); //year, month indexed0, day, hour, minute
//        Date endDate1 = new Date (115,6,12,6,30);
//        myEvents.add(new Event("Basketball Game" , startDate1, endDate1 ));
//        Date startDate2 = new Date(115,2, 10, 5, 30);
//        Date endDate2 = new Date(115,2, 12, 5, 30);
//        myEvents.add(new Event("Hockey game", startDate2, endDate2));

        //Parse and create events

        int tempYear = -1;
        int tempMonth = -1;
        int tempDay = -1;
        int tempHourStart = -1;
        int tempHourEnd = -1;
        int tempMinStart = -1;
        int tempMinEnd = -1;
        int state = -1;
        String tempName = "";

        String delims = "[ ]+";
        String[] tokens = final_data.split(delims);
        Log.i("Starting", "parser");

        for (int i = 0; i < tokens.length; i++) {
            Log.i("current token: ", tokens[i]);
            Log.i("Current State: ", String.valueOf(state));
            if (tokens[i].equals("EVENT_TITLE:")) {
                if (tempYear != -1 && tempMonth != -1 && tempDay != -1 && tempHourStart != -1 &&
                        tempHourEnd != -1 && tempMinStart != -1 && tempMinEnd != -1) {
                    //Create event, reset all temps back to -1;
                    Date startDate = new Date(tempYear, tempMonth, tempDay, tempHourStart, tempMinStart);
                    Date endDate = new Date(tempYear, tempMonth, tempDay, tempHourEnd, tempMinEnd);
                    myEvents.add(new Event(tempName, startDate, endDate));
                    Log.i("Event Created: ", tempName);
                    tempYear = -1;
                    tempMonth = -1;
                    tempDay = -1;
                    tempHourStart = -1;
                    tempHourEnd = -1;
                    tempMinStart = -1;
                    tempMinEnd = -1;
                    state = -1;
                    tempName = "";
                }
                state = 1;
            }
            if (tokens[i].equals("EVENT_DATE:")) {
                state = 0;
            }

            if (!tokens[i].equals("EVENT_TITLE:") && state == 1) {
                Log.i("checking event title", "new EVENT_TITLE found");
                tempName += " " + tokens[i];
            }
            if (tokens[i].equals("EVENT_DATE:")) {
                tempDay = Integer.parseInt(tokens[i+1]);
                tempMonth = Integer.parseInt(tokens[i+2]);
                tempYear = Integer.parseInt(tokens[i+3]) - 1900;
            }
            if (tokens[i].equals("EVENT_START:")) {
                tempHourStart = Integer.parseInt(tokens[i+1]);
                tempMinStart = Integer.parseInt(tokens[i+2]);
            }
            if (tokens[i].equals("EVENT_END:")) {
                tempHourEnd = Integer.parseInt(tokens[i+1]);
                tempMinEnd = Integer.parseInt(tokens[i+2]);
            }
            //Catch last event
            if (i+1 == tokens.length && tempYear != -1 && tempMonth != -1 && tempDay != -1 && tempHourStart != -1 &&
                    tempHourEnd != -1 && tempMinStart != -1 && tempMinEnd != -1) {
                //Create event, reset all temps back to -1;
                Date startDate = new Date(tempYear, tempMonth, tempDay, tempHourStart, tempMinStart);
                Date endDate = new Date(tempYear, tempMonth, tempDay, tempHourEnd, tempMinEnd);
                myEvents.add(new Event(tempName, startDate, endDate));
                Log.i("Event Created: ", tempName);
                tempYear = -1;
                tempMonth = -1;
                tempDay = -1;
                tempHourStart = -1;
                tempHourEnd = -1;
                tempMinStart = -1;
                tempMinEnd = -1;
                state = -1;
                tempName = "";
            }
        }
    }

    public class EventListComparator implements Comparator<Event> {
        public int compare(Event event1 , Event event2) {
            if (event1.getStart().getMonth() > event2.getStart().getMonth()) {
                return 1;
            } else if (event1.getStart().getMonth() < event2.getStart().getMonth()) {
                return -1;
            } else {
                if (event1.getStart().getDay() > event2.getStart().getDay()) {
                    return 1;
                } else if (event1.getStart().getDay() < event2.getStart().getDay()) {
                    return -1;
                } else {
                    if (event1.getStart().getTime() > event2.getStart().getTime()) {
                        return 1;
                    } else if (event1.getStart().getTime() < event2.getStart().getTime()){
                        return  -1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }



    private long calculateDuration(Event e) {
        long difference = 0;
        difference = e.getEnd().getTime() - e.getStart().getTime();
        return difference;
    }

    private void populateListView() {
//       String[] items =  {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,   //context
//                R.layout.item_list,     //layout to use
//                items); //items to display

        ArrayAdapter<Event> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);

    }

    private void makeItemsClickable() {
        ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Event eventClicked = myEvents.get(position);

                //TextView name = (TextView) findViewById(R.id.eventName);
                //name.setText(eventClicked.getTitle());
                String name = (eventClicked.getTitle());

//                TextView start = (TextView) findViewById(R.id.startTime);
                String start = (eventClicked.getStart().toString());

//                TextView end = (TextView) findViewById(R.id.endTime);
                String end = (eventClicked.getEnd().toString());

//                TextView timeRemaining = (TextView) findViewById(R.id.timeLeft);
                Date now = new Date();

                String difference = findTimeRemaining(eventClicked , now);
                //EventViewer.textViewTime.setText(difference);

                Bundle b = new Bundle();
                b.putString("NAME", name);
                b.putString("START" , start);
                b.putString("END" , end);
                b.putString("TIME", difference);

                Intent intent = new Intent (EventList.this, EventViewer.class);
                intent.putExtras(b);

                startActivity(intent);

                String message = eventClicked.getTitle();
                Toast.makeText(EventList.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    private String findTimeRemaining(Event eventClicked, Date now) {
        long start = eventClicked.getStart().getTime();
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

    private class MyListAdapter extends ArrayAdapter<Event> {
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
            Event e = myEvents.get(position);

            // name:
            TextView titleText = (TextView) itemView.findViewById(R.id.item_txtName);
            titleText.setText(e.getTitle());

            // start:
            TextView startText = (TextView) itemView.findViewById(R.id.item_txtStart);
            startText.setText("" + e.getStart());

            // end:
            TextView duration = (TextView) itemView.findViewById(R.id.item_txtEnd);
//            endText.setText("" + e.getEnd());

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