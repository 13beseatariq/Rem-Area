package com.remarea.alitariq.remarea;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class dialogueBox extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int notif_id = 0;
        String alarm_id=null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            alarm_id = extras.getString("alarm_id");
            notif_id = (int) extras.getInt("notif_id");
        }

        for(int i=0; i<userHome.list_data.length; i++){
            String id = userHome.list_data[i].get("_id").toString();
            if(id.equals(alarm_id)){
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(notif_id);

                Date date = new Date((Long)userHome.list_data[i].get("time"));
                showDialogue((String)userHome.list_data[i].get("text"), (String)userHome.list_data[i].get("address"), date.toLocaleString());
                break;
            }
        }
    }

    public void showDialogue(String text, String location, String time)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("RemArea - Alarm");
        builder.setMessage("Alarm text: " + text + "\n" + "Location: " + location + "\n" + "Added on: " + time
                            + "\n" + "Note: Clicking on Thanks will delete the alarm. Clicking on Later will postpone the alarm" +
                "for next entry.");

        builder.setPositiveButton("Thanks", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }

        });

        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
