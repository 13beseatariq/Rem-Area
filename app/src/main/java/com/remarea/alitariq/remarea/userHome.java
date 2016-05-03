package com.remarea.alitariq.remarea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class userHome extends AppCompatActivity implements View.OnClickListener {
    private String username;
    Button addReminder, checkReminders;

    TextView welcomeUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        welcomeUsername = (TextView) findViewById(R.id.welcomeUsername);
        addReminder = (Button) findViewById(R.id.addReminder);
        checkReminders = (Button) findViewById(R.id.checkReminders);

        addReminder.setOnClickListener(this);
        checkReminders.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        welcomeUsername.setText("Welcome: " + username);
    }

    @Override
    public void onClick(View v) {
        if (v == addReminder){
            this.startActivity(new Intent(this, addReminderMap.class));
        }
        else if(v == checkReminders){

        }
    }
}
