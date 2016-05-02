package com.remarea.alitariq.remarea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class userHome extends AppCompatActivity {
    private String username;

    TextView welcomeUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        welcomeUsername = (TextView) findViewById(R.id.welcomeUsername);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        welcomeUsername.setText("Welcome: " + username);
    }
}
