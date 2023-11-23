package com.example.team3;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Collections;

// 숫자 순서대로 누르기
public class game_one extends AppCompatActivity {
    private ImageManager imageManager;
    private int clickedImageIndex; // 클릭한 이미지의 인덱스를 저장하는 변수

    /* 난이도 추가/삭제 시 변경해야하는 부분
     @ 난이도관련 변수 추가/삭제
     @ max_index 추가/삭제
     @ numClickCheck() ; case 조건 추가/삭제
     @ OptionActivity.alert_level() : case 조건 추가/삭제
    */

    //DB 데이터를 갱신, 검색하기 위한 핸들러
    private TimeThread timer = new TimeThread(); //Thread를 이용한 타이머
    private SendMessageHandler sendMsg = new SendMessageHandler();  //타이머를 위한 handler

    // 숫자판관련 변수
    TableLayout numbers_layout; //숫자판 레이아웃
    TextView[] death_pad; //게임종료 카운트 판
    TextView[] num_pad; //숫자판 9개 생성 1~9
    TextView level_view; //현재 LEVEL 상태를 보여줌
    TextView timer_view; //시간출력 텍스트뷰
    TextView gameStart; //시작버튼
    boolean reset_num_pad = false; //숫자판을 재생성할때 사용하는 변수
    int max_index = 9; //숫자판 최대 갯수
    int end_count = 0; //다음에 터치할 숫자를 비교하는 카운트
    int death_count = 5; //5번 잘못누르면 게임종료

    // 타이머관련 변수 
    final int TIMER_STOP = 0;
    final int TIMER_SET = 1;
    final int TIMER_COUNTDOWN = 2;
    final int GAME_START = 3;
    int end_time = 0;  //DB에 시간을 입력하기 위한 변수

    String user_name = ""; //DB에 입력되는 유저의 이름

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game_one);

        imageManager = ImageManager.getInstance();
        // 전달된 인덱스 받아오기
        Intent intent = getIntent();
        clickedImageIndex = intent.getIntExtra("clickedImageIndex", -1);

        level_view = (TextView) findViewById(R.id.testview);
        gameStart = (TextView) findViewById(R.id.gameStart);
        timer_view = (TextView) findViewById(R.id.timer_view);
        numbers_layout = (TableLayout) findViewById(R.id.numbers);

        max_index = 9;
        death_count = 3;
        create_death_pad();
    }

    // START 버튼 - 클릭시 난수생성, INVISIBLE, THREAD 시작
    public void onGameStart(View v) {
        gameStart();
    }

    public void gameStart() {
        gameStart.setClickable(false);
        timer_view.setText("0:00");
        death_count = 3;

        for (int i = 0; i < death_pad.length; i++) {
            death_pad[i].setTextColor(Color.RED);
        }
        // 타이머 시작
        timer = new TimeThread();
        timer.start();
    }

    // 게임종료 이벤트 : | 타이머종료 | 랭킹체크 | end_count 초기화 |
    public void gameStop() {
        sendMsg.sendEmptyMessage(TIMER_STOP); // 타이머 멈추기

        gameStart.setText("READY");
        gameStart.setVisibility(View.VISIBLE);  // 시작버튼 보이기
        gameStart.setClickable(true);
        numbers_layout.setVisibility(View.INVISIBLE); // 숫자판 숨기기
    }

    // 자동종료카운트를 death_count의 수만큼 동적으로 생성
    public void create_death_pad() {
        LinearLayout layout_death = (LinearLayout) findViewById(R.id.layout_death);
        death_pad = new TextView[death_count];
        for (int i = 0; i < death_pad.length; i++) {
            death_pad[i] = new TextView(this);
            death_pad[i].setTextColor(Color.BLUE);
            death_pad[i].setText(" \uD83D\uDC3E ");
            death_pad[i].setTextSize(20);
            death_pad[i].setTypeface(death_pad[i].getTypeface(), Typeface.BOLD);
            layout_death.addView(death_pad[i]);
        }
    }

    // 숫자판 생성
    // 숫자판 Layout 생성
    public void num_pad_define(int row_max, int tv_max) {

        TableLayout tableLayout = (TableLayout) findViewById(R.id.numbers);
        TableRow[] tableRow = new TableRow[row_max];

        // Layout_width, Layout_height, Layout_weight 속성값 설정
        TableRow.LayoutParams tv_params = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1
        );
        TableLayout.LayoutParams row_params = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1
        );

        // TableLayout 생성된 TableRow, TextView들을 제거
        tableLayout.removeAllViews();
        num_pad = new TextView[tv_max];

        int old_index = 0; //TableRow에 TextView를 3개 또는 5개씩 생성하기 위한 변수들
        int new_index = row_max;    

        for (int row_index = 0; row_index < row_max; row_index++) {
            tableRow[row_index] = new TableRow(this);
            tableRow[row_index].setLayoutParams(row_params);

            for (int tv_index = old_index; tv_index < new_index; tv_index++) {
                // TextView 속성
                num_pad[tv_index] = new TextView(this);
                num_pad[tv_index].setText(String.valueOf(tv_index + 1));
                num_pad[tv_index].setGravity(Gravity.CENTER);
                num_pad[tv_index].setTextSize(40);
                num_pad[tv_index].setTextColor(Color.parseColor("#0B510E"));
                num_pad[tv_index].setId(tv_index);
                num_pad[tv_index].setLayoutParams(tv_params);
                num_pad[tv_index].setTypeface(num_pad[tv_index].getTypeface(), Typeface.BOLD);
                num_pad[tv_index].setOnClickListener(new View.OnClickListener() {
                    @Override
                    // 숫자를 클릭하였을 시 발생하는 이벤트
                    public void onClick(View v) {

                        final int num_pad_id = v.getId();

                        // 버튼클릭시 발생하는 애니메이션효과
                        AlphaAnimation click_ani = new AlphaAnimation(0, 1);
                        click_ani.setDuration(500);
                        num_pad[num_pad_id].startAnimation(click_ani);

                        // 숫자패드에 적힌 숫자와 현재 터치해야하는 숫자가 같은지 확인하는 조건문
                        if (num_pad[num_pad_id].getText().equals(String.valueOf(end_count + 1))) {
                            // 숫자를 정확히 터치하였을 경우
                            num_pad[num_pad_id].setClickable(false);
                            num_pad[num_pad_id].setTextColor(Color.argb(15, 000, 000, 000));
                            //num_pad[num_pad_id].setTextColor(Color.LTGRAY);
                            reset_num_pad = true;
                            end_count++;
                        } else {
                            // 숫자를 잘못 터치하였을 경우
                            death_count--;
                            if (death_count >= 0)
                                death_pad[death_count].setTextColor(Color.LTGRAY);
                            // 카운트가 0이 되면 게임이 종료된다
                            if (death_count == 0) {
                                gameStop();
                                Toast.makeText(getApplicationContext(), "도전 실패!!!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        numClickCheck(); //게임 종료를 위한 end_count 체크 및 새로운 숫자판 생성
                    }
                });
                tableRow[row_index].addView(num_pad[tv_index]); //TableRow에 TextView 생성
            }
            old_index = new_index;
            new_index = new_index + row_max;
            tableLayout.addView(tableRow[row_index]); //TableLayout에 TableRow 생성
        }
    }

    // 숫자판 생성 메소드
    public void create_num_pad(int min_num, int max_num) {
        ArrayList<Integer> list = number_shuffle(min_num, max_num);
        reset_num_pad = false;

        for (int index = 0; index < max_index; index++) {
            Typeface customFont = ResourcesCompat.getFont(this, R.font.cutefont);
            num_pad[index].setClickable(true);
            num_pad[index].setText(list.get(index).toString());//list에 있는 숫자를 TextView에 출력
            num_pad[index].setTypeface(customFont);
            num_pad[index].setTextColor(Color.BLUE);
        }
    }

    // 난수생성 메소드
    public ArrayList number_shuffle(int min_num, int max_num) {
        ArrayList shuffled_num = new ArrayList<Integer>(max_index);
        for (int cnt = min_num; cnt <= max_num; cnt++) {
            shuffled_num.add(cnt);
        }
        Collections.shuffle(shuffled_num);  // shuffled_num 에 있는 data를 섞음
        return shuffled_num;
    }

    // 게임 종료를 위한 end_count 체크 및 새로운 숫자판 생성
    public void numClickCheck() {
        if (end_count == 9) {
            gameStop();         //게임종료
        }
    }

    // 타이머 Thread / Handler 
    // Thread 에서 layout을 수정하기위해 사용하는 Handler
    class SendMessageHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMER_STOP: //타이머 종료
                    timer.stopTimer();
                    break;
                case TIMER_SET: //타이머 시작
                    timer_view.setText("" + msg.obj);
                    break;
                case TIMER_COUNTDOWN: //게임시작버튼 클릭 시 카운트다운 3..2..1..Go!!
                    gameStart.setText("" + msg.obj);
                    break;
                case GAME_START: //카운트다운 종료후 게임진행
                    num_pad_define(3, 9);
                    create_num_pad(1, 9); // 난수생성 메소드 호출
                    gameStart.setVisibility(View.INVISIBLE); //시작버튼 숨기기
                    numbers_layout.setVisibility(View.VISIBLE);
                    break; // 숫자판 보이기
                default:
                    break;
            }
        }
    }


    // 타이머 스레드
    class TimeThread extends Thread implements Runnable {
        boolean isStart = false;

        public TimeThread() {
            isStart = true;
        }

        public void stopTimer() { //스레드 정지
            isStart = !isStart;
        }

        public void run() {
            super.run();
            int m_second = 1;
            int second = 0;
            boolean flag = true;
            while (isStart) {
                try {
                    if (flag) {
                        for (int i = 3; i >= 0; i--) { // 게임시작시 카운트다운을 넣기 위한 반복문
                            Message loding_msg = sendMsg.obtainMessage();
                            if (i != 0) {
                                loding_msg.obj = i;
                            } else {
                                loding_msg.obj = "GO!!!";
                            }
                            loding_msg.what = TIMER_COUNTDOWN;
                            sendMsg.sendMessage(loding_msg);
                            Thread.sleep(500);
                        }
                        flag = false;
                        sendMsg.sendEmptyMessage(GAME_START);
                    }
                    // 게임진행시간을 handler로 보내주는 이벤트
                    Message timer_msg = sendMsg.obtainMessage();
                    end_time = (second * 100) + m_second;
                    String time_state = new String(String.valueOf(second + " : " + m_second));
                    timer_msg.what = TIMER_SET; // TIMER_SET == 1;
                    timer_msg.obj = time_state;
                    sendMsg.sendMessage(timer_msg);

                    Thread.sleep(10); // 1/100초의 속도로 진행

                    m_second++;
                    if (m_second == 100) { // 1/100s
                        second++;
                        m_second = 0;
                    }
                } catch (InterruptedException e) {
                }
            }

            Intent intent = new Intent(game_one.this, game_one_result.class);
            intent.putExtra("second", second);
            startActivityForResult(intent, clickedImageIndex-1);
            finish();

        }
    }
}