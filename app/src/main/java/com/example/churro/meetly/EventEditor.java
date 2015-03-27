package com.example.churro.meetly;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EventEditor extends ActionBarActivity {

    EditText textDate, textTimeStart, textTimeEnd;
    private String eventName = "Current Event", eventDetail = "Fun stuff for everyone right?";
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    private final LatLng DEFAULT_LOCATION = new LatLng(49.187500,-122.849000);
    private double LATITUDE;
    private double LONGITUDE;

    private AlertDialog.Builder dialogBuilder;
    private GoogleMap mMap;


    int data_block = 100;
    String Message;
    String final_data="";
    public static List<Event> myEvents = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);

        //String name = getIntent().getExtras().getString("NAME");
        String name = "EVENT_TITLE:" + getIntent().getExtras().getString("NAME");
        String date = "EVENT_DATE:" + getIntent().getExtras().getInt("DAY") +
                getIntent().getExtras().getInt("MON") + getIntent().getExtras().getInt("YEAR");
        String timeStart = "EVENT_START:" + getIntent().getExtras().getInt("SH") +
                getIntent().getExtras().getInt("SM") + getIntent().getExtras().getInt("SS");
        String timeEnd = "EVENT_END:" + getIntent().getExtras().getInt("EH") +
                getIntent().getExtras().getInt("EM") + getIntent().getExtras().getInt("ES");
        String latlon = "EVENT_LOCATION:" + getIntent().getExtras().getDouble("LAT") +
                getIntent().getExtras().getDouble("LNG");

        String eventToDelete = name + date + timeStart + timeEnd + latlon;

        loadEvent();
        populateList();
        int position = getIntent().getExtras().getInt("POS");
        deleteOldEvent(position);
        setButtons();
///*

//*/
        textDate = (EditText) findViewById(R.id.txtDate);
        textTimeStart = (EditText) findViewById(R.id.txtStart);
        textTimeEnd = (EditText) findViewById(R.id.txtEnd);

    }


    private void deleteOldEvent(int position) {
        myEvents.remove(position);
    }

    private void setButtons() {
        Button name = (Button) findViewById(R.id.btnName);
        Button date = (Button) findViewById(R.id.btnDate);
        Button timeStart = (Button) findViewById(R.id.btnStart);
        Button timeEnd = (Button) findViewById(R.id.btnEnd);
        Button create = (Button) findViewById(R.id.btnCreate);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEventName();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEventDate();
            }
        });

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEventStartTime();
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEventEndTime();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });
    }
    private void loadEvent() {
        final_data = "";
        try {
            FileInputStream fis = openFileInput("event.txt");

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

    private void deleteEvent(String eventInfo) {
        String delims = "[ ]+";
        String[] tokens = eventInfo.split(delims);
        Log.i("Starting", "parser");




    }


    private void populateList() {

        int tempYear = -1;
        int tempMonth = -1;
        int tempDay = -1;
        int tempHourStart = -1;
        int tempHourEnd = -1;
        int tempMinStart = -1;
        int tempMinEnd = -1;
        double tempLat = -1;
        double tempLng = -1;
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
                        tempHourEnd != -1 && tempMinStart != -1 && tempMinEnd != -1 && tempLat != -1 && tempLng != -1) {
                    //Create event, reset all temps back to -1;
                    Date startDate = new Date(tempYear, tempMonth, tempDay, tempHourStart, tempMinStart);
                    Date endDate = new Date(tempYear, tempMonth, tempDay, tempHourEnd, tempMinEnd);
                    myEvents.add(new Event(tempName, startDate, endDate, tempLat, tempLng));
                    Log.i("Event Created: ", tempName);
                    tempYear = -1;
                    tempMonth = -1;
                    tempDay = -1;
                    tempHourStart = -1;
                    tempHourEnd = -1;
                    tempMinStart = -1;
                    tempMinEnd = -1;
                    tempLat = -1;
                    tempLng = -1;
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
                tempDay = Integer.parseInt(tokens[i + 1]);
                tempMonth = Integer.parseInt(tokens[i + 2]);
                tempYear = Integer.parseInt(tokens[i + 3]) - 1900;
            }
            if (tokens[i].equals("EVENT_START:")) {
                tempHourStart = Integer.parseInt(tokens[i + 1]);
                tempMinStart = Integer.parseInt(tokens[i + 2]);
            }
            if (tokens[i].equals("EVENT_END:")) {
                tempHourEnd = Integer.parseInt(tokens[i + 1]);
                tempMinEnd = Integer.parseInt(tokens[i + 2]);
            }
            if (tokens[i].equals("EVENT_LOCATION:")) {
                tempLat = Double.parseDouble(tokens[i + 1]);
                tempLng = Double.parseDouble(tokens[i + 2]);
            }
            //Catch last event
            if (i + 1 == tokens.length && tempYear != -1 && tempMonth != -1 && tempDay != -1 && tempHourStart != -1 &&
                    tempHourEnd != -1 && tempMinStart != -1 && tempMinEnd != -1) {
                //Create event, reset all temps back to -1;
                Date startDate = new Date(tempYear, tempMonth, tempDay, tempHourStart, tempMinStart);
                Date endDate = new Date(tempYear, tempMonth, tempDay, tempHourEnd, tempMinEnd);
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


    private void setEventStartTime() {
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textTimeStart.setText("EVENT_START: " + hourOfDay + " " + minute);
            }
        }, selectedHour, selectedMinute, true);
        tpd.show();
    }

    private void setEventEndTime() {
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textTimeEnd.setText("EVENT_END: " + hourOfDay + " " + minute);
            }
        }, selectedHour, selectedMinute, true);
        tpd.show();
    }


    private void setEventDate() {
        final Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textDate.setText("EVENT_DATE: " + dayOfMonth + " " + (monthOfYear) + " " + year);
            }
        }, selectedYear, selectedMonth, selectedDay);
        dpd.show();
    }

    private void setEventName() {
        dialogBuilder = new AlertDialog.Builder(this);
        final EditText textInput = new EditText(this);

        eventName = "EVENT_TITLE: ";

        dialogBuilder.setTitle("Name your Event!");
        dialogBuilder.setMessage("What is the name of your event?");
        dialogBuilder.setView(textInput);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventName += textInput.getText().toString();
                updateDisplay(eventName, R.id.txtName);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialogEventName = dialogBuilder.create();
        dialogEventName.show();
    }

    private void updateDisplay(final String message, final int viewId) {
        TextView textView = (TextView) findViewById(viewId);
        textView.setText(message);
    }

    //@Override
    public void onMapClick(LatLng pos) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(pos).title("Event Here"));
        Log.i("Location: ", pos.toString());
        String temp = pos.toString();
        String delims = "[ ,)(]+";
        String[] tokens = temp.split(delims);
        LATITUDE = Double.parseDouble(tokens[1]);
        LONGITUDE = Double.parseDouble(tokens[2]);
    }

    private void saveEvent() {
        final_data = "";
        try {
            FileInputStream fis = openFileInput("event.txt");

            InputStreamReader isr = new InputStreamReader(fis);
            char[] data = new char[data_block];
            int size;
            try {
                while((size = isr.read(data))>0) {
                    String read_data = String.copyValueOf(data, 0, size);
                    final_data+= read_data;
                    data = new char[data_block];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Message = final_data + " " + eventName + " " + textDate.getText().toString() + " " + textTimeStart.getText().toString() + " " +
                textTimeEnd.getText().toString() + " EVENT_LOCATION: " + Double.toString(LATITUDE) + " " + Double.toString(LONGITUDE);
        try {
            FileOutputStream fou = openFileOutput("event.txt", MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            try{
                osw.write(Message);
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("Current Events", Message);
//        Bundle b = new Bundle();
//        // b.putInt("POS" , position);
//        b.putString("NAME", eventName);
//        b.putString("START" , textTimeStart.getText().toString());
//        b.putString("END" , textTimeEnd.getText().toString());
//        b.putString("TIME", "test time left");
//        b.putDouble("LAT", 463.6666);
//        b.putDouble("LNG", 63.666);

        Intent intent = new Intent (EventEditor.this, EventViewer.class);
//        intent.putExtras(b);
        startActivity(intent);

    }


}


