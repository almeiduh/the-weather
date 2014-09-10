package com.wit.weather;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by aribeiro on 25/8/14.
 */
public class GCMIntentService extends IntentService {
    public GCMIntentService() {
        super(GCMIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v("INTENT-SERVICE-"+Thread.currentThread().getId(), "I'M DOING STUFF AND YOU DON'T SEE IT!");
        sleep(3000);
        Log.v("INTENT-SERVICE-"+Thread.currentThread().getId(), "HOW COOL IS THAT??");
        sleep(1000);
        Log.v("INTENT-SERVICE-"+Thread.currentThread().getId(), "I COULD BE DOING USEFUL STUFF, LIKE DOWNLOAD FILES");
        sleep(2000);
        Log.v("INTENT-SERVICE-"+Thread.currentThread().getId(), "OR UPDATE THE APP CONTENT");
        sleep(5000);
        Log.v("INTENT-SERVICE-"+Thread.currentThread().getId(), "BUT I'M JUST HERE PRINTING USELESS LOGS...");
        sleep(1000);
        Log.v("INTENT-SERVICE-"+Thread.currentThread().getId(), "I'M DONE! BYE!");

    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }
}
