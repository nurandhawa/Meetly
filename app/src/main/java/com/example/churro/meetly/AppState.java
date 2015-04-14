package com.example.churro.meetly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amansandhu on 13/04/15.
 */
public class AppState {

    static AppState instance = null;

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    private MeetlyServer server;
    private int userToken = -1;
    private String userName = "Not Logged In";
    private static List<MeetlyServer.MeetlyEvent> myEvents = new ArrayList<MeetlyServer.MeetlyEvent>();

    protected AppState() {
        server= new MeetlyServerImpl();
    }

    public static String getName() {
        return getInstance().userName;
    }

    public static void setName(String name) {
        getInstance().userName = name;
    }

    public static List<MeetlyServer.MeetlyEvent> getEvents() {
        return getInstance().myEvents;
    }
    public static void setEvents(List<MeetlyServer.MeetlyEvent> events) {
        getInstance().myEvents = events;
    }

    public static int getToken() {
        return getInstance().userToken;
    }

    public static void setToken(int token) {
        getInstance().userToken = token;
    }

    public static MeetlyServer getServer() {
        return getInstance().server;
    }

    public static void setServer(MeetlyServer server) {
        getInstance().server = server;
    }

}
