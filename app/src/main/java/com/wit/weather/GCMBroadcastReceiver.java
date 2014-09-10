package com.wit.weather;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aribeiro on 25/8/14.
 */
public class GCMBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("data")) {

            String data = extras.getString("data");
            JSONObject object;
            try {
                object = new JSONObject(data);
                String message = object.getString("message");
                String temperature = object.getString("temperature");

                Toast.makeText(context, "Alert arrived!", Toast.LENGTH_SHORT).show();

                NotificationManager notyManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Notification.Builder notificationBuilder = new Notification.Builder(context);
                notificationBuilder.setContentTitle(temperature);
                notificationBuilder.setContentText(message);
                notificationBuilder.setTicker("Message received! " + temperature + "º");
                notificationBuilder.setSmallIcon(android.R.drawable.ic_menu_add);

                notyManager.notify(1, notificationBuilder.build());

                Intent i = new Intent(context, GCMIntentService.class);
                context.startService(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}
