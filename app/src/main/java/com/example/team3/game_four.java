package com.example.team3;
// MainActivity.java



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
// 고양이 가위바위보
public class game_four extends AppCompatActivity {
    private ImageManager imageManager;
    private int clickedImageIndex;// 클릭한 이미지의 인덱스를 저장하는 변수

    private ImageView yourRSP;
    private ImageView myRSP;
    private ImageButton buttonRock;
    private ImageButton buttonPaper;
    private ImageButton buttonScissors;
    private TextView resultview;
    private int score = 0;
    private int elapsedSeconds = 0;
    TextView time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_four);

        imageManager = ImageManager.getInstance();
        // 전달된 인덱스 받아오기
        Intent intent = getIntent();
        clickedImageIndex = intent.getIntExtra("clickedImageIndex", -1);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        yourRSP = findViewById(R.id.yourRSP);
        myRSP = findViewById(R.id.myRSP);
        buttonRock = findViewById(R.id.buttonRock);
        buttonPaper = findViewById(R.id.buttonPaper);
        buttonScissors = findViewById(R.id.buttonScissors);
        resultview = findViewById(R.id.resultview);

        time = findViewById(R.id.time);

        buttonRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame("Rock");
            }
        });

        buttonPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame("Paper");
            }
        });

        buttonScissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame("Scissors");
            }
        });

        new Thread(new timeCheck()).start();
    }

    private void playGame(String userChoice) {
        String[] choices = {"Rock", "Paper", "Scissors"};
        Random random = new Random();
        int computerIndex = random.nextInt(3);
        String computerChoice = choices[computerIndex];

        int yourRSPImage = getRSPImage(computerChoice);
        int myRSPImage = getRSPImage(userChoice);
        yourRSP.setImageResource(yourRSPImage);
        myRSP.setImageResource(myRSPImage);

        String result = determineWinner(userChoice, computerChoice);
        showToast(result);
    }

    private int getRSPImage(String choice) {
        switch (choice) {
            case "Rock":
                return R.drawable.catrock;
            case "Paper":
                return R.drawable.catpaper;
            case "Scissors":
                return R.drawable.catscissor;
            default:
                return 0;
        }
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
                Message msg = new Message();
                msg.arg1 = i;
                handler.sendMessage(msg);

                // score가 3이 되면 현재까지의 경과 시간을 elapsedSeconds에 저장하고 다음 액티비티로 이동
                if (score >= 3) {
                    elapsedSeconds = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(game_four.this, game_four_result.class);
                            intent.putExtra("second", elapsedSeconds);
                            startActivityForResult(intent, clickedImageIndex-1);
                            finish();
                        }
                    });
                    return; // 스레드 종료
                }

                try {
                    if (i != MAXTIME) {
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String determineWinner(String userChoice, String computerChoice) {
        if (userChoice.equals(computerChoice)) {
            return "It's a tie!";
        } else if ((userChoice.equals("Rock") && computerChoice.equals("Scissors")) ||
                (userChoice.equals("Paper") && computerChoice.equals("Rock")) ||
                (userChoice.equals("Scissors") && computerChoice.equals("Paper"))) {
            score++;
            resultview.setText(String.valueOf(score));
            return "You win!";
        } else {
            return "Cat wins!";
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}