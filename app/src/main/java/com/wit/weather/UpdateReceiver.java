package com.wit.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by aribeiro on 9/9/14.
 */
public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.wit.weather.action.UPDATE_FINISHED".equalsIgnoreCase(intent.getAction())) {
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
