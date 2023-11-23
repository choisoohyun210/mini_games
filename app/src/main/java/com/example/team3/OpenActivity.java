package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class OpenActivity extends AppCompatActivity {
    AnimationDrawable myAni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_open);

        LinearLayout img = (LinearLayout)findViewById(R.id.open_layout);
        myAni = (AnimationDrawable) img.getBackground();

        img.post(new Runnable() {
            public void run() {
                myAni.start();
            }
        });
    }
    // 게임시작
    public void onStart(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    // 랭킹메뉴로 이동
    public void onRanking(View v){
        Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
        startActivity(intent);
    }
    // 옵션으로 이동
    public void onOption(View v){
        Intent intent = new Intent(getApplicationContext(), OptionActivity.class);
        startActivity(intent);
        finish();
    }
}