package com.example.churro.meetly;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EventCreator extends ActionBarActivity {
    private AlertDialog.Builder dialogBuilder;
    //Temp strings for checking
    EditText textDate, textTimeStart, textTimeEnd;
    private String eventName = "Current Event", eventDetail = "Fun stuff for everyone right?";
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_creator_screen);
        setButtons();
        textDate = (EditText) findViewById(R.id.txtDate);
        textTimeStart = (EditText) findViewById(R.id.txtStart);
        textTimeEnd = (EditText) findViewById(R.id.txtEnd);
	}

    private void setButtons() {
        Button name = (Button) findViewById(R.id.btnName);
        Button detail = (Button) findViewById(R.id.btnDetail);
        Button date = (Button) findViewById(R.id.btnDate);
        Button timeStart = (Button) findViewById(R.id.btnStart);
        Button timeEnd = (Button) findViewById(R.id.btnEnd);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEventName();
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEventDetail();
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
    }

    private void setEventStartTime() {
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textTimeStart.setText(hourOfDay + ":" + minute);
            }
        }, selectedHour, selectedMinute, true);
        tpd.show();
    }

    private void setEventEndTime() {
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textTimeEnd.setText(hourOfDay + ":" + minute);
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
                textDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            }
        }, selectedYear, selectedMonth, selectedDay);
        dpd.show();
    }

    private void setEventName() {
        dialogBuilder = new AlertDialog.Builder(this);
        final EditText textInput = new EditText(this);

        eventName = "Event Name: ";

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

    private void setEventDetail() {
        dialogBuilder = new AlertDialog.Builder(this);
        final EditText textInput = new EditText(this);

        eventDetail = "have fun";

        dialogBuilder.setTitle("Details");
        dialogBuilder.setMessage("Any additional information?");
        dialogBuilder.setView(textInput);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventDetail = textInput.getText().toString();
                updateDisplay(eventDetail, R.id.txtDetail);
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

}
