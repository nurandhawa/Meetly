package com.example.churro.meetly;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class LoginScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        EditText un = (EditText) findViewById(R.id.tf_username);
        String username = un.toString();

        EditText pd = (EditText) findViewById(R.id.tf_pwd);
        String pwd = pd.toString();
    }

}
