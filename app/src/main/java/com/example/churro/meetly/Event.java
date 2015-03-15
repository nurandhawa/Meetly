package com.example.churro.meetly;

import java.util.Calendar;
import java.util.Date;

public class Event {
    String title;
    Date start;
    Date end;
    Double lat;
    Double lng;

    public Event(String title, Date start, Date end, Double lat, Double lng) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.lat = lat;
        this.lng = lng;
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

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }


}
