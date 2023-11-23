package com.example.team3;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.Random;
//고양이 술래잡기 게임
public class game_two extends AppCompatActivity {
    private ImageManager imageManager;
    private int clickedImageIndex;
    TextView time;
    TextView count;
    Button start;

    ImageView[] img_array = new ImageView[9];
    int[] imageID = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8, R.id.imageView9};

    final String TAG_ON = "on"; //태그용
    final String TAG_OFF = "off";
    private int score = 0;
    private int elapsedSeconds = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_two);

        imageManager = ImageManager.getInstance();
        Intent intent = getIntent();
        clickedImageIndex = intent.getIntExtra("clickedImageIndex", -1);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        time = (TextView)findViewById(R.id.time);
        count = (TextView)findViewById(R.id.count);
        start = (Button)findViewById(R.id.start);

        for(int i = 0; i<img_array.length; i++){
            /*int img_id = getResources().getIdentifier("imageView"+i+1, "id", "com.example.pc_20.molegame");*/
            img_array[i] = (ImageView)findViewById(imageID[i]);
            img_array[i].setImageResource(R.drawable.cathome);
            img_array[i].setTag(TAG_OFF);

            img_array[i].setOnClickListener(new View.OnClickListener() { //고양이 이미지에 온클릭리스너
                @Override
                public void onClick(View v) {
                    if(((ImageView)v).getTag().toString().equals(TAG_ON)){
                        Toast.makeText(getApplicationContext(), "good", Toast.LENGTH_LONG).show();
                        count.setText(String.valueOf(score++));
                        ((ImageView) v).setImageResource(R.drawable.cathome);
                        v.setTag(TAG_OFF);
                    }else{
                        Toast.makeText(getApplicationContext(), "bad", Toast.LENGTH_LONG).show();
                        if(score<=0){
                            score=0;
                            count.setText(String.valueOf(score));
                        }
                        ((ImageView) v).setImageResource(R.drawable.catcute);
                        v.setTag(TAG_ON);
                    }
                }
            });
        }

        time.setText("시간");
        count.setText("0마리");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start.setVisibility(View.GONE);
                count.setVisibility(View.VISIBLE);

                new Thread(new timeCheck()).start();

                for(int i = 0; i<img_array.length; i++){
                    new Thread(new DThread(i)).start();
                }
            }
        });
    }

    Handler onHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            img_array[msg.arg1].setImageResource(R.drawable.catcute);
            img_array[msg.arg1].setTag(TAG_ON); //올라오면 ON태그 달아줌
        }
    };

    Handler offHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            img_array[msg.arg1].setImageResource(R.drawable.cathome);
            img_array[msg.arg1].setTag(TAG_OFF); //내려오면 OFF태그 달아줌

        }
    };

    public class DThread implements Runnable{ //두더지를 올라갔다 내려갔다 해줌
        int index = 0; //두더지 번호

        DThread(int index){
            this.index=index;
        }

        @Override
        public void run() {
            while(true){
                try {
                    Message msg1 = new Message();
                    int offtime = new Random().nextInt(5000) + 500 ;
                    Thread.sleep(offtime); //두더지가 내려가있는 시간

                    msg1.arg1 = index;
                    onHandler.sendMessage(msg1);

                    int ontime = new Random().nextInt(1000)+500;
                    Thread.sleep(ontime); //두더지가 올라가있는 시간
                    Message msg2 = new Message();
                    msg2.arg1= index;
                    offHandler.sendMessage(msg2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
                if (score >= 10) {
                    elapsedSeconds = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(game_two.this, game_two_result.class);
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
}