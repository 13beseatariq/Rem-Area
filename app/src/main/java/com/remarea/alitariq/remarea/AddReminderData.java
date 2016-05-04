package com.remarea.alitariq.remarea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class AddReminderData extends AppCompatActivity {
    LatLng marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder_data);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            marker = new LatLng(extras.getDouble("lat"), extras.getDouble("lon"));
        }
    }
}
