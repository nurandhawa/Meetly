package com.example.churro.meetly;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        EditText un = (EditText) findViewById(R.id.tf_username);
        final String username = un.toString();

        EditText pd = (EditText) findViewById(R.id.tf_pwd);
        final String pwd = pd.toString();

        Button login = (Button) findViewById(R.id.btn_login1);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetlyServerImpl server = new MeetlyServerImpl();

                try {
                    int token = server.login(username, pwd);
                } catch (MeetlyServer.FailedLoginException e) {

                    dialogPopUp();
                }
                LoginScreen.this.finish();

            }
        });
    }

    private void dialogPopUp() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Invalid User Information");
        alertDialog.setMessage("Invalid password or username. Please try again!");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }


}
