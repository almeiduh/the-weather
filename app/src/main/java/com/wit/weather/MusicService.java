package com.wit.weather;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by aribeiro on 8/9/14.
 */
public class MusicService extends Service {

    private IBinder binder;
    private MediaPlayer player;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if ("com.wit.weather.action.MUSIC_PAUSE".equalsIgnoreCase(action)) {
                pausePlaying();

            } else if ("com.wit.weather.action.MUSIC_START".equalsIgnoreCase(action)) {
                startPlaying();

            } else if ("com.wit.weather.action.MUSIC_STOP".equalsIgnoreCase(action)) {
                stopPlaying();
            }
        }
    };


    @Override
    public void onCreate() {
        Log.d("MUSIC_SERVICE-"+Thread.currentThread().getId(), "CREATED SERVICE");
        super.onCreate();
        binder = new MusicBinder();

        IntentFilter f = new IntentFilter();
        f.addAction("com.wit.weather.action.MUSIC_PAUSE");
        f.addAction("com.wit.weather.action.MUSIC_START");
        f.addAction("com.wit.weather.action.MUSIC_STOP");

        registerReceiver(broadcastReceiver, f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MUSIC_SERVICE", "BOUND SERVICE");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void startPlaying() {
        Log.d("MUSIC_SERVICE-"+Thread.currentThread().getId(), "REQUEST TO PLAY");
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.music);

            Intent notificationIntent = new Intent(this, WeatherActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Intent pauseIntent = new Intent("com.wit.weather.action.MUSIC_PAUSE");
            Intent startIntent = new Intent("com.wit.weather.action.MUSIC_START");
            Intent stopIntent = new Intent("com.wit.weather.action.MUSIC_STOP");
            PendingIntent pauseBroadcast = PendingIntent.getBroadcast(this, 1, pauseIntent, 0);
            PendingIntent startBroadcast = PendingIntent.getBroadcast(this, 2, startIntent, 0);
            PendingIntent stopBroadcast = PendingIntent.getBroadcast(this, 3, stopIntent, 0);

            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentTitle("Music playing");
            builder.setContentText("Enjoy the sound, and relax!");
            builder.setTicker("Music started playing!");
            builder.setContentIntent(pendingIntent);
            builder.setWhen(System.currentTimeMillis());
            builder.setOngoing(true);
            builder.setSmallIcon(android.R.drawable.ic_media_play);

            builder.addAction(android.R.drawable.ic_media_play, "Play", startBroadcast);
            builder.addAction(android.R.drawable.ic_media_pause, "Pause", pauseBroadcast);
            builder.addAction(0, "Stop", stopBroadcast);

            Notification build = builder.build();

            startForeground(1, build);

        }

        if (!player.isPlaying()) {
            player.start();
        }
    }

    public void stopPlaying() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;

            stopForeground(true);
        }
    }

    public void pausePlaying() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    public class MusicBinder extends Binder {

        MusicService getService() {
            return MusicService.this;
        }

    }
}
