package com.example.team3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


public class game_one_result extends AppCompatActivity {
    private ImageManager imageManager;

    TextView sub_result;
    Button sub_retry;
    SharedPreferences spf = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one_result);

        imageManager = ImageManager.getInstance();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        spf = getSharedPreferences("spfScore", MODE_PRIVATE);

        sub_result = findViewById(R.id.sub_result);
        sub_retry = findViewById(R.id.sub_retry);

        int second = getIntent().getIntExtra("second", 0);
        Log.d("game_one", "Second Value: " + second);
        sub_result.setText(second + "초가 걸렸다냥!");
        // 이전 게임들의 총 시간을 가져옴
        int totalPreviousTime = spf.getInt("totalTime", 0);

        // 현재 게임의 결과를 이전 게임들의 총 시간에 더함
        int total_time = totalPreviousTime + second;
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("resultSecond", second);
        editor.putInt("totalTime", total_time);
        editor.apply();

        sub_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(RESULT_OK);
                // 액티비티를 종료
                finish();
            }
        });
    }
}