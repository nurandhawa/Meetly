package com.example.churro.meetly;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EventCreator extends ActionBarActivity {

    public List<Event> myEvents = new ArrayList<Event>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_creator_screen);

        createEvent();
	}

    private void createEvent() {
        EditText name = (EditText) findViewById(R.id.titleInput);
        String eventName = name.getText().toString();

        DatePicker startPicker = (DatePicker) findViewById(R.id.startDatePicker);
        int startMonth = startPicker.getMonth();
        int startDay = startPicker.getDayOfMonth();
        int startYear = startPicker.getYear();

        TimePicker startTP = (TimePicker) findViewById(R.id.startTimePicker);
        int startHour = startTP.getCurrentHour();
        int startMin = startTP.getCurrentMinute();

        Date startDate = new Date(startYear, startMonth, startDay, startHour, startMin);

        DatePicker endPicker = (DatePicker) findViewById(R.id.endDatePicker);
        int endMonth = endPicker.getMonth();
        int endDay = endPicker.getDayOfMonth();
        int endYear = endPicker.getYear();

        TimePicker endTP = (TimePicker) findViewById(R.id.endTimePicker);
        int endHour = endTP.getCurrentHour();
        int endMin = endTP.getCurrentMinute();

        Date endDate = new Date(endYear, endMonth, endDay, endHour, endMin);

        Event newEvent = new Event(eventName, startDate, endDate);
        myEvents.add(newEvent);
    }


}
