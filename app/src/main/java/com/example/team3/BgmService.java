package com.example.team3;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.CompoundButton;

public class BgmService extends Service {
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new LocalBinder();
    private boolean isBgmEnabled = true;

    public class LocalBinder extends Binder {
        BgmService getService() {
            return BgmService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.catsound);
        mediaPlayer.setLooping(true);

        // BGM 스위치를 누를 때까지 대기
    }

    public void startBgm() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stopBgm() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public boolean isBgmPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void setBgmEnabled(boolean enabled) {
        isBgmEnabled = enabled;
        if (enabled) {
            startBgm();
        } else {
            stopBgm();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
