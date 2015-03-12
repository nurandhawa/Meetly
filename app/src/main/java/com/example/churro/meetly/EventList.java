package com.example.churro.meetly;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class EventList extends ActionBarActivity {

    public List<Event> myEvents = new ArrayList<Event>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_screen);

        populateList();
        Collections.sort(myEvents, new EventListComparator());

        populateListView();
        makeItemsClickable();

        createEventButton();
        calculateDuration();

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
        Date startDate1 = new Date(115,3,10,5,30);
        Date endDate1 = new Date (115,3,12,6,30);
        myEvents.add(new Event("Basketball Game" , startDate1, endDate1 ));
        Date startDate2 = new Date(115,2, 10, 5, 30);
        Date endDate2 = new Date(115,2, 12, 5, 30);
        myEvents.add(new Event("Hockey game", startDate2, endDate2));
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



    private long calculateDuration() {
        long difference = 0;
        for (Event e : myEvents) {
            difference = e.getEnd().getTime() - e.getStart().getTime();
        }
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
                //TODO: open new activity once clicked
                String message = "You clicked # " + position +
                        " Event Name: " + eventClicked.getTitle();
                Toast.makeText(EventList.this, message, Toast.LENGTH_LONG).show();
            }
        });
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

            // Find the car to work with.
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
            long difference = calculateDuration();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

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
