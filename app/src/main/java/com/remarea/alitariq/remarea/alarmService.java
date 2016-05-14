package com.remarea.alitariq.remarea;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mongodb.DBObject;

public class alarmService extends IntentService {
    DBObject[] data_list;

    public alarmService() {
        super("alarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Log.d("Azlan in Service", extras.getString("foo"));
        }

    }
}
