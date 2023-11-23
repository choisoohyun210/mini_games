package com.example.team3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class OptionActivity extends AppCompatActivity {

    private BgmService bgmService;
    private boolean bgmServiceBound = false;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_BGM_STATUS = "bgmStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); // 제목 없애기
        }

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // 앱을 처음 실행했을 때 BGM 상태가 저장되지 않았다면, 기본값인 true로 설정
        boolean defaultBgmStatus = true;
        if (!sharedPreferences.contains(KEY_BGM_STATUS)) {
            updateBgmStatus(defaultBgmStatus);
        }

        // BGM 상태에 따라 초기 설정
        if (isBgmOn()) {
            playBgm();
        } else {
            stopBgm();
        }

        // BGM 토글 스위치
        Switch toggleSwitch = findViewById(R.id.toggleButton);

        // 여기서 setOnCheckedChangeListener를 설정하기 전에 toggleSwitch.setChecked 호출
        toggleSwitch.setOnCheckedChangeListener(null);
        toggleSwitch.setChecked(isBgmOn());
        toggleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> toggleBgm(isChecked));

        Button onmainbtn = findViewById(R.id.onmainbtn);
        onmainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onmain();
            }
        });
    }



    private void playBgm() {
        if (bgmService != null) {
            bgmService.startBgm();
        }
    }

    private void stopBgm() {
        if (bgmService != null) {
            bgmService.stopBgm();
        }
    }

    private void toggleBgm(boolean isBgmOn) {
        if (isBgmOn) {
            playBgm();
        } else {
            stopBgm();
        }
        updateBgmStatus(isBgmOn);
    }

    private void updateBgmStatus(boolean isBgmOn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_BGM_STATUS, isBgmOn);
        editor.apply();
    }

    private boolean isBgmOn() {
        return sharedPreferences.getBoolean(KEY_BGM_STATUS, true);
    }

    private void onmain() {
        Intent intent = new Intent(getApplicationContext(), OpenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BgmService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bgmServiceBound) {
            unbindService(connection);
            bgmServiceBound = false;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            BgmService.LocalBinder binder = (BgmService.LocalBinder) service;
            bgmService = binder.getService();
            bgmServiceBound = true;
            // BGM 스위치의 상태에 따라 BGM 시작 여부 설정
            toggleBgm(isBgmOn());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bgmServiceBound = false;
        }
    };
}
