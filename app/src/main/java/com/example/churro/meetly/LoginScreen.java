package com.example.churro.meetly;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = (Button) findViewById(R.id.btn_login1);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText un = (EditText) findViewById(R.id.tf_username);
                final String username = un.getText().toString();

                EditText pd = (EditText) findViewById(R.id.tf_pwd);
                final String pwd = pd.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connectToDB(username, pwd);
                    }

                }).start();

            }
        });
    }


    private void connectToDB(String username, String password) {

        MeetlyServer server = AppState.getServer();

        try {
            int token = server.login(username, password);
            AppState.setToken(token);
            AppState.setName(username);
            Log.i("Logged in as: ", username);
        } catch (MeetlyServer.FailedLoginException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(LoginScreen.this, EventList.class));
    }


}
