package com.example.team3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.sql.Time;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class game_six extends AppCompatActivity {
    private ImageManager imageManager;
    private int clickedImageIndex;// 클릭한 이미지의 인덱스를 저장하는 변수

    ImageView hand_imageview;
    ImageView set_hand_imageview;
    AnimationDrawable animationDrawable;
    TextToSpeech textToSpeech;
    Button start_btn;
    private long startTimeMillis;
    private long endTimeMillis;
    TextView Win;
    TextView time;
    Runnable timerRunnable;

    private int winCount = 0;
    private boolean gameStarted = false;
    private int elapsedSeconds = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_six);

        imageManager = ImageManager.getInstance();
        // 전달된 인덱스 받아오기
        Intent intent = getIntent();
        clickedImageIndex = intent.getIntExtra("clickedImageIndex", -1);

        hand_imageview = findViewById(R.id.hand_imageview);
        set_hand_imageview = findViewById(R.id.set_hand_imageview);
        time = (TextView) findViewById(R.id.time);
        Win = findViewById(R.id.Win);
        start_btn = findViewById(R.id.start_btn);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();// 제목 없애기
        }

        animationDrawable = (AnimationDrawable) hand_imageview.getDrawable();

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.KOREA);
                    textToSpeech.setPitch(0.6f);  // 목소리 톤
                    textToSpeech.setSpeechRate(1.2f);  // 목소리 속도
                }
            }
        });
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameStarted) {
                    start_btn.setVisibility(View.GONE);
                    set_hand_imageview.setVisibility(View.GONE);
                    hand_imageview.setVisibility(View.VISIBLE);
                    animationDrawable.start();
                    voicePlay("참참참");
                    gameStarted = true;
                    new Thread(new game_six.timeCheck()).start();
                }
            }
        });
    }


    public void btn_click(View view) {
        int getComHand;
        switch (view.getId()) {

            case R.id.LF_btn:
            case R.id.RF_btn:
                if (gameStarted && winCount < 3)
                    hand_imageview.setVisibility(View.GONE);
                set_hand_imageview.setVisibility(View.VISIBLE);


                getComHand = new Random().nextInt(3) + 1;
                switch (getComHand) {
                    case 1:
                        set_hand_imageview.setImageResource(R.drawable.lefthand);
                        if (view.getId() == R.id.LF_btn) {
                            voicePlay("패");
                            showToast("패");
                            animationDrawable.start();
                            hand_imageview.setVisibility(View.VISIBLE);
                            set_hand_imageview.setVisibility(View.GONE);
                            updateWin();
                        } else {
                            voicePlay("승");
                            showToast("승");
                            animationDrawable.start();
                            hand_imageview.setVisibility(View.VISIBLE);
                            set_hand_imageview.setVisibility(View.GONE);
                            winCount++;
                            updateWin();

                        }
                        break;
                    case 2:
                        set_hand_imageview.setImageResource(R.drawable.righthand);
                        if (view.getId() == R.id.LF_btn) {
                            voicePlay("승");
                            showToast("승");
                            animationDrawable.start();
                            hand_imageview.setVisibility(View.VISIBLE);
                            set_hand_imageview.setVisibility(View.GONE);
                            winCount++;
                            updateWin();
                        } else {
                            voicePlay("패");
                            showToast("패");
                            animationDrawable.start();
                            hand_imageview.setVisibility(View.VISIBLE);
                            set_hand_imageview.setVisibility(View.GONE);
                            updateWin();
                        }
                        break;

                }
                break;

            default:

                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        textToSpeech.shutdown();
    }

    public void voicePlay(String voiceText) {
        textToSpeech.speak(voiceText, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    private void updateWin() {
        Win.setText("Win: " + winCount);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            time.setText(msg.arg1 + "초");
        }
    };

    public class timeCheck implements Runnable {
        final int MAXTIME = 3000;

        @Override
        public void run() {
            for (int i = 0; i <= MAXTIME; i++) {
                final int currentCount = i;  // final 변수로 선언

                Message msg = new Message();
                msg.arg1 = currentCount;
                handler.sendMessage(msg);

                // score가 3이 되면 현재까지의 경과 시간을 elapsedSeconds에 저장하고 다음 액티비티로 이동
                if (winCount >= 3 && gameStarted) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            animationDrawable.stop();
                            gameStarted = false;
                            findViewById(R.id.LF_btn).setEnabled(false);
                            findViewById(R.id.RF_btn).setEnabled(false);
                            elapsedSeconds = currentCount;

                            Intent intent = new Intent(game_six.this, game_six_result.class);
                            intent.putExtra("second", elapsedSeconds);
                            startActivityForResult(intent, clickedImageIndex-1);
                            finish();
                        }
                    });
                    return; // 스레드 종료
                }

                try {
                    if (currentCount != MAXTIME) {
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}