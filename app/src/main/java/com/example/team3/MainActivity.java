package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> usernames = new ArrayList<>();
    private ImageManager imageManager;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9, imageView10, imageView11, imageView12;

    private Class<?>[] activityClasses = {
            game_one.class,
            game_two.class,
            game_three.class,
            game_four.class,
            game_five.class,
            game_six.class,
          };
    private DatabaseHelper dbAdapter;
    DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageManager = ImageManager.getInstance();
        dbAdapter = DatabaseHelper.getInstance(this);

        dbManager = new DatabaseManager(this);  // 객체 생성
        dbManager.open();
        rank_input_name();

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);
        imageView10 = findViewById(R.id.imageView10);
        imageView11 = findViewById(R.id.imageView11);
        imageView12 = findViewById(R.id.imageView12);

        imageManager.setImageViews(imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9, imageView10, imageView11, imageView12);

        // 이미지뷰 배열 초기화
        ImageView[] imageViews = {imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9, imageView10, imageView11, imageView12};

        for (int i = 0; i < imageViews.length; i++) {
            final int currentIndex = i;
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 이미지 클릭 시, 랜덤한 액티비티로 이동
                    Intent randomIntent = new Intent(MainActivity.this, getRandomActivityClass());
                    imageManager.updateImages(view.getId());
                    randomIntent.putExtra("clickedImageIndex", currentIndex + 1);
                    startActivityForResult(randomIntent, currentIndex + 1); // 인덱스를 1부터 시작하도록 수정
                }
            });
        }

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지 업데이트 후 현재 액티비티를 리로드
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 1);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 2);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 3);
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 4);
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 5);

            }
        });

        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 6);

            }
        });
        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 7);

            }
        });
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 8);

            }
        });
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 9);

            }
        });
        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());
                Class<?> randomActivityClass = getRandomActivityClass();
                startActivityForResult(new Intent(MainActivity.this, randomActivityClass), 10);

            }
        });
        imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageManager.updateImages(view.getId());

                Intent intent = new Intent(MainActivity.this, RankingActivity.class);
                intent.putExtra("USER_NAME", usernames.get(usernames.size() - 1));
                startActivityForResult(intent, 11);
            }
        });
    }

    private Class<?> getRandomActivityClass() {
        // 랜덤으로 index를 선택
        int randomIndex = new Random().nextInt(activityClasses.length);

        // 선택된 index에 해당하는 액티비티 클래스 반환
        return activityClasses[randomIndex];
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // explain_one 액티비티로부터의 결과 코드 확인
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 이미지 업데이트
            imageManager.updateImages(imageView2.getId());

            // 이미지뷰에 반영
            if (imageView1 != null) {
                imageView1.setImageResource(imageManager.getBoardImage()[2]);
            }

            if (imageView2 != null) {
                imageView2.setImageResource(imageManager.getBoardImage()[4]);
            }

        } else if(requestCode == 2 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView3.getId());

            // 이미지뷰에 반영
            if (imageView2 != null) {
                imageView2.setImageResource(imageManager.getBoardImage()[4]);
            }

            if (imageView3 != null) {
                imageView3.setImageResource(imageManager.getBoardImage()[6]);
            }
        }
        else if(requestCode == 3 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView4.getId());

            // 이미지뷰에 반영
            if (imageView3 != null) {
                imageView3.setImageResource(imageManager.getBoardImage()[6]);
            }

            if (imageView4 != null) {
                imageView4.setImageResource(imageManager.getBoardImage()[8]);
            }
        }
        else if(requestCode == 4 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView5.getId());

            // 이미지뷰에 반영
            if (imageView5 != null) {
                imageView5.setImageResource(imageManager.getBoardImage()[10]);
            }

            if (imageView6 != null) {
                imageView6.setImageResource(imageManager.getBoardImage()[12]);
            }
        }
        else if(requestCode == 5 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView6.getId());

            // 이미지뷰에 반영
            if (imageView6 != null) {
                imageView6.setImageResource(imageManager.getBoardImage()[12]);
            }

            if (imageView7 != null) {
                imageView7.setImageResource(imageManager.getBoardImage()[14]);
            }
        }
        else if(requestCode == 6 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView7.getId());

            // 이미지뷰에 반영
            if (imageView7 != null) {
                imageView7.setImageResource(imageManager.getBoardImage()[14]);
            }

            if (imageView8 != null) {
                imageView8.setImageResource(imageManager.getBoardImage()[16]);
            }
        }
        else if(requestCode == 7 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView8.getId());

            // 이미지뷰에 반영
            if (imageView4 != null) {
                imageView4.setImageResource(imageManager.getBoardImage()[8]);
            }

            if (imageView8 != null) {
                imageView8.setImageResource(imageManager.getBoardImage()[16]);
            }
        }else if(requestCode == 8 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView9.getId());

            // 이미지뷰에 반영
            if (imageView8 != null) {
                imageView8.setImageResource(imageManager.getBoardImage()[16]);
            }

            if (imageView9 != null) {
                imageView9.setImageResource(imageManager.getBoardImage()[18]);
            }
        }else if(requestCode == 9 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView10.getId());

            // 이미지뷰에 반영
            if (imageView9 != null) {
                imageView9.setImageResource(imageManager.getBoardImage()[18]);
            }

            if (imageView10 != null) {
                imageView10.setImageResource(imageManager.getBoardImage()[20]);
            }
        }else if(requestCode == 10 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView11.getId());

            // 이미지뷰에 반영
            if (imageView10 != null) {
                imageView10.setImageResource(imageManager.getBoardImage()[20]);
            }

            if (imageView11 != null) {
                imageView11.setImageResource(imageManager.getBoardImage()[22]);
            }
        }else if(requestCode == 11 && resultCode == RESULT_OK) {
            imageManager.updateImages(imageView12.getId());

            // 이미지뷰에 반영
            if (imageView11 != null) {
                imageView11.setImageResource(imageManager.getBoardImage()[22]);
            }

            if (imageView12 != null) {
                imageView12.setImageResource(imageManager.getBoardImage()[24]);
            }

        }
    }

    public void rank_input_name() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.nameinput, (ViewGroup) findViewById(R.id.layout_name_alert));
        EditText editUserName;

        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        editUserName = layout.findViewById(R.id.username);

        dialog.setView(layout)
                .setTitle("환영한다냥 \uD83D\uDC3E")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = editUserName.getText().toString();
                        // 사용자 등록 시 초기 시간(0) 전달
                        dbManager.enroll(userName,0);  // enroll 메서드 호출
                        editUserName.setText("");
                        usernames.add(userName);

                        Intent intent = new Intent(MainActivity.this, RankingActivity.class);
                        intent.putStringArrayListExtra("USERNAMES", usernames);
                        dbManager.close();  // 데이터베이스 닫기
                    }
                });
        dialog.show();
    }
}
