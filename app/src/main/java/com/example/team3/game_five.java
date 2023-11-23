package com.example.team3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Time;

// 고양이 사냥시켜주기 게임
public class game_five extends AppCompatActivity {
    private ImageManager imageManager;
    private int clickedImageIndex;// 클릭한 이미지의 인덱스를 저장하는 변수

    ImageButton punch1;
    ImageButton punch2;
    Button start_btn;
    TextView scoreTextView;
    TextView time;
    int score = 0;
    boolean isGameStarted = false;
    Handler timerHandler;
    Runnable timerRunnable;
    private int elapsedSeconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_five);

        imageManager = ImageManager.getInstance();
        // 전달된 인덱스 받아오기
        Intent intent = getIntent();
        clickedImageIndex = intent.getIntExtra("clickedImageIndex", -1);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();// 제목 없애기
        }

        punch1 = findViewById(R.id.punch1);
        punch2 = findViewById(R.id.punch2);
        start_btn = findViewById(R.id.start_btn);
        time = (TextView) findViewById(R.id.time);
        scoreTextView = findViewById(R.id.scoreTextView);

        punch1.setVisibility(View.VISIBLE);
        punch2.setVisibility(View.GONE);

        timerHandler=new Handler(Looper.getMainLooper());

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameStarted){
                    isGameStarted=true;
                    startGame();
                    start_btn.setVisibility(View.GONE);
                }


            }
        });
        punch1.setOnClickListener(punchClickListener);
        punch2.setOnClickListener(punchClickListener);
    }

    private void startGame() {
        timerRunnable = new Runnable() {
            private  int seconds=0;
            @Override
            public void run() {
                if (isGameStarted) {
                    time.setText("Time: " + (seconds++) + "s");
                    elapsedSeconds = seconds;
                    timerHandler.postDelayed(this, 1000);
                }
            }
        };
        timerHandler.postDelayed(timerRunnable,0);

        punch1.setOnClickListener(punchClickListener);
        punch2.setOnClickListener(punchClickListener);
    }


    private View.OnClickListener punchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isGameStarted && score < 25) {
                if (view == punch1) {
                    punch1.setVisibility(View.GONE);
                    punch2.setVisibility(View.VISIBLE);
                } else if (view == punch2) {
                    punch1.setVisibility(View.VISIBLE);
                    punch2.setVisibility(View.GONE);
                }
                increaseScore();
            }
        }
    };

    private void increaseScore() {
        score++;
        scoreTextView.setText("Score: " + score);

        if(score>=25) {
            isGameStarted=false;
            moveToNextScreen();
        }
    }

    private void moveToNextScreen() {
        // 다음 화면으로 이동하는 코드
        Intent intent = new Intent(game_five.this, game_five_result.class);
        intent.putExtra("time", elapsedSeconds);
        startActivityForResult(intent, clickedImageIndex-1);
        finish();
    }

    private void stopTimer(){
        timerHandler.removeCallbacks(timerRunnable);
    }

    protected void onDestroy(){
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
