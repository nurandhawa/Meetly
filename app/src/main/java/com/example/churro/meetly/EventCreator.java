package com.example.churro.meetly;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class EventCreator extends ActionBarActivity implements GoogleMap.OnMapClickListener {
    private AlertDialog.Builder dialogBuilder;

    private GoogleMap mMap;

    //Temp strings for checking
    EditText textDate, textTimeStart, textTimeEnd;
    private String eventName = "Current Event", eventDetail = "Fun stuff for everyone right?";
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private final LatLng DEFAULT_LOCATION = new LatLng(49.187500,-122.849000);
    private double LATITUDE;
    private double LONGITUDE;


    //Variable for saving file
    String Message;
    int data_block = 100;
    String final_data="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.event_creator_screen);
        LATITUDE = 49.187500;
        LONGITUDE = -122.849000;
        setButtons();
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setOnMapClickListener(this);
        setDefaultMap();
        textDate = (EditText) findViewById(R.id.txtDate);
        textTimeStart = (EditText) findViewById(R.id.txtStart);
        textTimeEnd = (EditText) findViewById(R.id.txtEnd);
	}

    private void setDefaultMap() {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 12);
        mMap.animateCamera(update);
    }

    private void setButtons() {
        Button name = (Button) findViewById(R.id.btnName);
       // Button detail = (Button) findViewById(R.id.btnDetail);
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

//        detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setEventDetail();
//            }
//        });

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
        startActivity(new Intent(EventCreator.this, EventList.class));
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

//    private void setEventDetail() {
//        dialogBuilder = new AlertDialog.Builder(this);
//        final EditText textInput = new EditText(this);
//
//        eventDetail = "EVENT_DETAIL: ";
//
//        dialogBuilder.setTitle("Details");
//        dialogBuilder.setMessage("Any additional information?");
//        dialogBuilder.setView(textInput);
//
//        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                eventDetail += textInput.getText().toString();
//                updateDisplay(eventDetail, R.id.txtDetail);
//            }
//        });
//
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });

//        AlertDialog dialogEventName = dialogBuilder.create();
//        dialogEventName.show();
//    }

    private void updateDisplay(final String message, final int viewId) {
        TextView textView = (TextView) findViewById(viewId);
        textView.setText(message);
    }

    @Override
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
}
