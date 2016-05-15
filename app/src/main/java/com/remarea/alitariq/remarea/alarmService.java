package com.remarea.alitariq.remarea;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class alarmService extends IntentService {
    DBObject[] list_data;

    public alarmService() {
        super("alarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.list_data = userHome.list_data;

        LatLng curLocation = getCurrentLocation();

        for(int i=0; i<list_data.length; i++){
            if(checkLocationDistance(curLocation, new LatLng((Double)list_data[i].get("lat"), (Double)list_data[i].get("lon")),
                    (Integer)list_data[i].get("radius"))){
                Log.d("Azlan", (String) list_data[i].get("_id").toString());
                showNotification((String) list_data[i].get("_id").toString(), "RemArea - Alarm", (String) list_data[i].get("text"));
            }
        }
    }

    LatLng getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            if (locationManager != null) {
                String provider = locationManager.getBestProvider(criteria, true);
                Location myLocation = locationManager.getLastKnownLocation(provider);

                double latitude, longitude;
                if (myLocation != null) {
                    latitude = myLocation.getLatitude();
                    longitude = myLocation.getLongitude();

                    try {
                        return new LatLng((double)Math.round(latitude * 10000000d) / 10000000d,
                                (double)Math.round(longitude * 10000000d) / 10000000d);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return null;
    }

    boolean checkLocationDistance(LatLng A, LatLng B, double radius){
        double newDistance = haversine(A.latitude, A.longitude, B.latitude, B.longitude);

        Log.d("Location Distance", newDistance+"");

        if(newDistance <= radius)
            return true;
        else
            return false;
    }

    double haversine(double lat1, double lng1, double lat2, double lng2) {
        int r = 6371000; // average radius of the earth in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        return d;
    }

    public int showNotification(String alarm_id, String header, String body){
        int id = (int) Math.random()*1000;

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, dialogueBox.class)
                .putExtra("notif_id", id).putExtra("alarm_id", alarm_id).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.logo_transparent);
        mBuilder.setContentTitle(header);
        mBuilder.setContentText(body);

        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
        return id;
    }
}
