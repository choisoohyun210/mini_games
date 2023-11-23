package com.example.team3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RankingActivity extends AppCompatActivity {

    private DatabaseHelper dbAdapter;
    private SQLiteDatabase database;

    private int total_time = 0;
    TextView rank[] = new TextView[10];
    TextView name[] = new TextView[10];
    TextView time[] = new TextView[10];

    Cursor rank_sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ranking);
        Button retry = findViewById(R.id.sub_retry);

        // dbHelper를 사용하여 database를 가져옴
        DatabaseManager dbManager = new DatabaseManager(this);
        dbManager.open();
        database = dbManager.getDatabase();


        showTotalTime();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences spf = getSharedPreferences("spfScore", MODE_PRIVATE);
                // 이전 total_time을 가져옴
                int previousTotalTime = spf.getInt("totalTime", 0);

                // total_time을 0으로 초기화
                SharedPreferences.Editor editor = spf.edit();
                editor.putInt("totalTime", 0);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), OpenActivity.class);
                startActivity(intent);
                finish();

            }
        });

        dbAdapter = DatabaseHelper.getInstance(this);
        dbAdapter.open();

        // 데이터베이스에서 데이터 가져오기
        rank_sort = dbAdapter.getDatabase().query(
                DatabaseHelper.DATABASE_TABLE,
                new String[]{"_id", DatabaseHelper.USER_NAME, "time"},
                null,
                null,
                null,
                null,
                "time ASC",
                "10"
        );
        // 11위 사용자 삭제
        delete11thRankUser();

        layout();
        sort(rank_sort);
    }
    private void delete11thRankUser() {
        Cursor cursor = dbAdapter.getDatabase().query(
                DatabaseHelper.DATABASE_TABLE,
                new String[]{"_id", DatabaseHelper.USER_NAME, "time"},
                null,
                null,
                null,
                null,
                "time ASC",
                "10,1"  // 11th rank
        );

        if (cursor != null && cursor.moveToFirst()) {
            int userIdColumnIndex = cursor.getColumnIndex("_id");

            //_id 컬럼이 존재하는지 확인
            if (userIdColumnIndex != -1) {
                int userId = cursor.getInt(userIdColumnIndex);
                dbAdapter.deleteRank(userId);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void showTotalTime() {
        SharedPreferences spf = getSharedPreferences("spfScore", MODE_PRIVATE);
        int total_time = spf.getInt("totalTime", 0);
        Log.d("RankingActivity", "SharedPreferences에서 검색된 Total Time: " + total_time);
        // 인텐트나 저장된 위치에서 사용자 이름 가져오기
        String userName = getIntent().getStringExtra("USER_NAME"); // 실제 사용자 이름으로 대체

        // 데이터베이스에서 해당 사용자의 시간 업데이트
        updateTotalTime(userName, total_time);
        Log.d("RankingActivity", "데이터베이스에서 Total Time이 업데이트됨.");

    }

    private void updateTotalTime(String userName, int totalTime) {
        ContentValues values = new ContentValues();
        values.put("time", totalTime);  // "time" 열에 시간 저장

        // 해당 사용자의 시간 업데이트
        database.update(DatabaseHelper.DATABASE_TABLE, values, DatabaseHelper.USER_NAME + "=?", new String[]{userName});
        Log.d("RankingActivity", "Updating total time for user: " + userName + ", Total Time: " + totalTime);
    }

    @SuppressLint("Range")
    private void sort(Cursor sort_cursor) {
        if (sort_cursor != null && sort_cursor.moveToFirst()) {
            ArrayList<String> nameList = new ArrayList<>();
            ArrayList<String> timeList = new ArrayList<>();

            int index = 0;
            do {
                nameList.add(sort_cursor.getString(sort_cursor.getColumnIndex(DatabaseHelper.USER_NAME)));
                timeList.add(sort_cursor.getString(sort_cursor.getColumnIndex("time")));

                if (index < 10) {
                    name[index].setText(nameList.get(index));
                    time[index].setText(timeList.get(index));
                }
                index++;
            } while (sort_cursor.moveToNext());
        } else {
            // sort_cursor가 null이거나 데이터가 없는 경우에 대한 처리
            // 데이터가 없다는 메시지를 보여주거나 빈 화면으로 처리
        }

        if (sort_cursor != null) {
            sort_cursor.close();
        }
    }

    private void layout() {
        // name TextView
        name[0] = (TextView) findViewById(R.id.name1);
        name[1] = (TextView) findViewById(R.id.name2);
        name[2] = (TextView) findViewById(R.id.name3);
        name[3] = (TextView) findViewById(R.id.name4);
        name[4] = (TextView) findViewById(R.id.name5);
        name[5] = (TextView) findViewById(R.id.name6);
        name[6] = (TextView) findViewById(R.id.name7);
        name[7] = (TextView) findViewById(R.id.name8);
        name[8] = (TextView) findViewById(R.id.name9);
        name[9] = (TextView) findViewById(R.id.name10);
        // ranking TextView
        rank[0] = (TextView) findViewById(R.id.ranking1);
        rank[1] = (TextView) findViewById(R.id.ranking2);
        rank[2] = (TextView) findViewById(R.id.ranking3);
        rank[3] = (TextView) findViewById(R.id.ranking4);
        rank[4] = (TextView) findViewById(R.id.ranking5);
        rank[5] = (TextView) findViewById(R.id.ranking6);
        rank[6] = (TextView) findViewById(R.id.ranking7);
        rank[7] = (TextView) findViewById(R.id.ranking8);
        rank[8] = (TextView) findViewById(R.id.ranking9);
        rank[9] = (TextView) findViewById(R.id.ranking10);
        // time TextView
        time[0] = (TextView) findViewById(R.id.time1);
        time[1] = (TextView) findViewById(R.id.time2);
        time[2] = (TextView) findViewById(R.id.time3);
        time[3] = (TextView) findViewById(R.id.time4);
        time[4] = (TextView) findViewById(R.id.time5);
        time[5] = (TextView) findViewById(R.id.time6);
        time[6] = (TextView) findViewById(R.id.time7);
        time[7] = (TextView) findViewById(R.id.time8);
        time[8] = (TextView) findViewById(R.id.time9);
        time[9] = (TextView) findViewById(R.id.time10);

        for (int index = 0; index < 10; index++) {
            name[index].setText("");
            time[index].setText("");
        }// 순위 1~10 setting
        for (int index = 0; index < 10; index++) {
            rank[index].setText(String.valueOf(index + 1));
        }
    }
}
