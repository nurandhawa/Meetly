package com.example.churro.meetly;

import java.util.Calendar;
import java.util.Date;

public class Event {
    String title;
    Date start;
    Date end;

    public Event(String title, Date start, Date end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }


}
