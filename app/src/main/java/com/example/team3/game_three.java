package com.example.team3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
//카드 짝맞추기
public class game_three extends AppCompatActivity {
    private ImageManager imageManager;
    private int clickedImageIndex;

    private ImageButton[] buttons = new ImageButton[12];
    private ArrayList<Integer> imageList;
    private ArrayList<MemoryCard> cards;
    private TextView resultText;
    private Button startBtn;
    int preCardPosition = -1;

    private long startTimeMillis;
    private long endTimeMillis;
    private Handler handler = new Handler();
    private boolean gameRunning = false;
    private int elapsedSeconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_three);

        imageManager = ImageManager.getInstance();
        Intent intent = getIntent();
        clickedImageIndex = intent.getIntExtra("clickedImageIndex", -1);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        resultText = findViewById(R.id.result_text);
        init();
        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
                startBtn.setVisibility(View.GONE);
            }
        });
    }

    private void startGame() {
        if (!gameRunning) {
            startTimeMillis = System.currentTimeMillis();
            gameRunning = true;

            // 이미지 버튼 활성화
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setEnabled(true);
            }

            // 게임 진행 시간 업데이트
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (gameRunning) {
                        long currentTimeMillis = System.currentTimeMillis();
                        long gameTimeMillis = currentTimeMillis - startTimeMillis;
                        long gameTimeSeconds = gameTimeMillis / 1000;

                        resultText.setText("게임 진행 시간: " + gameTimeSeconds + "초");
                        // 1초마다 업데이트
                        handler.postDelayed(this, 1000);
                    }
                }
            }, 1000);
        }
    }

    public void init() { //리스트에 이미지 추가
        imageList = new ArrayList<>();
        imageList.add(R.drawable.catcute);
        imageList.add(R.drawable.card_two);
        imageList.add(R.drawable.card_three);
        imageList.add(R.drawable.card_four);
        imageList.add(R.drawable.cathome);
        imageList.add(R.drawable.card_six);
        imageList.add(R.drawable.catcute);
        imageList.add(R.drawable.card_two);
        imageList.add(R.drawable.card_three);
        imageList.add(R.drawable.card_four);
        imageList.add(R.drawable.cathome);
        imageList.add(R.drawable.card_six);

        Collections.shuffle(imageList); //이미지 순서 섞기
        cards = new ArrayList<>(); // 카드 리스트 초기화
        for (int i = 0; i < buttons.length; i++) {
            String buttonID = "imageBtn" + i;
            int resourseID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resourseID);

            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int position = getPositionFromViewId(view.getId());
                    if (position != -1) {
                        updateModel(position);
                        updateView(position);
                    }
                }
            });
            cards.add(new MemoryCard(imageList.get(i), false, false));
            buttons[i].setImageResource(R.drawable.card_q);
        }
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
        }
        resultText.setText("게임 진행 시간: 0초");
        gameRunning = false;
    }

    public int getPositionFromViewId(int viewId) {
        switch (viewId) {
            case R.id.imageBtn0:
                return 0;
            case R.id.imageBtn1:
                return 1;
            case R.id.imageBtn2:
                return 2;
            case R.id.imageBtn3:
                return 3;
            case R.id.imageBtn4:
                return 4;
            case R.id.imageBtn5:
                return 5;
            case R.id.imageBtn6:
                return 6;
            case R.id.imageBtn7:
                return 7;
            case R.id.imageBtn8:
                return 8;
            case R.id.imageBtn9:
                return 9;
            case R.id.imageBtn10:
                return 10;
            case R.id.imageBtn11:
                return 11;
            default:
                return -1;
        }
    }

    private void updateModel(int position) {
        MemoryCard card = cards.get(position);
        if (card.isFaceUp()) {
            Toast.makeText(this, "이미 뒤집힘", Toast.LENGTH_SHORT).show();
            return;
        }

        if (preCardPosition == -1) {
            restoreCard();
            preCardPosition = position;
        } else {
            checkForMatch(preCardPosition, position);
            preCardPosition = -1;
        }
        cards.get(position).setFaceUp(!card.isFaceUp());
    }

    private void updateView(int position) {
        MemoryCard card = cards.get(position);
        if (card.isFaceUp()) {
            buttons[position].setImageResource(card.getImageId());
        } else {
            buttons[position].setImageResource(R.drawable.card_q);
        }
    }

    private void restoreCard() {
        for (int i = 0; i < cards.size(); i++) {
            if (!cards.get(i).isMatched()) {
                buttons[i].setImageResource(R.drawable.card_q);
                cards.get(i).setFaceUp(false);
            }
        }
    }

    private void checkForMatch(int prePosition, int position) {
        if (cards.get(prePosition).getImageId() == cards.get(position).getImageId()) {
            //resultText.setText("매치성공");
            cards.get(prePosition).setMatched(true);
            cards.get(position).setMatched(true);

            checkCompletion();
        }
    }

    private void checkCompletion() {
        int count = 0;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).isMatched()) {
                count++;
            }
        }
        if (count == cards.size()) {
            endTimeMillis = System.currentTimeMillis();
            long gameTimeMillis = endTimeMillis - startTimeMillis;
            long gameTimeSeconds = gameTimeMillis / 1000;//밀리초를 초로 변경
            elapsedSeconds = (int)gameTimeSeconds;
            resultText.setText("게임 진행시간: " + gameTimeSeconds + "초");
            gameRunning = false; // 게임 종료
            Intent intent = new Intent(game_three.this, game_three_result.class);
            intent.putExtra("second", elapsedSeconds);
            Log.d("game_three", "Sending Second Value: " + gameTimeSeconds);
            startActivityForResult(intent, clickedImageIndex-1);
            finish();
        }
    }
}

