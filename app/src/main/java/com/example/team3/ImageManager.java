package com.example.team3;

import android.widget.ImageView;

public class ImageManager {
    // 이미지뷰 배열
    private ImageView[] imageViews;
    private static ImageManager instance;

    private int[] boardImage;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9, imageView10, imageView11, imageView12;


    private ImageManager() {
        // 이미지 배열 초기화
        boardImage = new int[]{R.drawable.one, R.drawable.one_cat, R.drawable.one_f, R.drawable.two, R.drawable.two_cat, R.drawable.two_f,
                R.drawable.three, R.drawable.three_cat, R.drawable.three_f, R.drawable.four, R.drawable.four_cat, R.drawable.four_f,
                R.drawable.five, R.drawable.five_cat, R.drawable.five_f, R.drawable.six, R.drawable.six_cat, R.drawable.six_f,
                R.drawable.seven, R.drawable.seven_cat, R.drawable.seven_f, R.drawable.eight, R.drawable.eight_cat, R.drawable.eight_f,
                R.drawable.nine, R.drawable.nine_cat, R.drawable.nine_f, R.drawable.ten, R.drawable.ten_cat, R.drawable.ten_f,
                R.drawable.eleven, R.drawable.eleven_cat, R.drawable.eleven_f, R.drawable.twelve, R.drawable.twelve_cat, R.drawable.twelve_f};
    }

    public static synchronized ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    public void setImageViews(ImageView imageView1, ImageView imageView2, ImageView imageView3, ImageView imageView4
            , ImageView imageView5, ImageView imageView6, ImageView imageView7, ImageView imageView8, ImageView imageView9
            , ImageView imageView10, ImageView imageView11, ImageView imageView12) {
        this.imageView1 = imageView1;
        this.imageView2 = imageView2;
        this.imageView3 = imageView3;
        this.imageView4 = imageView4;
        this.imageView5 = imageView5;
        this.imageView6 = imageView6;
        this.imageView7 = imageView7;
        this.imageView8 = imageView8;
        this.imageView9 = imageView9;
        this.imageView10 = imageView10;
        this.imageView11 = imageView11;
        this.imageView12 = imageView12;
    }

    // 이미지뷰 배열 초기화
    public void setImageViews(ImageView... views) {
        imageViews = views;
    }

    // 이미지뷰를 인덱스로 가져오기
    public ImageView getImageView(int index) {
        if (imageViews != null && index >= 0 && index < imageViews.length) {
            return imageViews[index];
        }
        return null;
    }

    // 이미지 리소스 배열 초기화
    public void setBoardImage(int[] boardImage) {
        this.boardImage = boardImage;
    }
    // 이미지 리소스 배열 가져오기
    public int[] getBoardImage(){
        return boardImage;
    }

    // 이미지 업데이트 메서드
    public void updateImages(int clickedImageResource) {
        // 이미지 업데이트 로직 추가 
        if (clickedImageResource == R.id.imageView2) {
            boardImage[2] = R.drawable.one_f;
            boardImage[4] = R.drawable.two_cat;

            if (imageView1 != null) {
                imageView1.setImageResource(boardImage[2]);
            }
            if (imageView2 != null) {
                imageView2.setImageResource(boardImage[4]);
            }
        } else if (clickedImageResource == R.id.imageView3) {
            // 이미지2와 이미지3를 변경
            boardImage[4] = R.drawable.two_f;
            boardImage[6] = R.drawable.three_cat;

            if (imageView2 != null) {
                imageView2.setImageResource(boardImage[4]);
            }
            if (imageView3 != null) {
                imageView3.setImageResource(boardImage[6]);
            }
        } else if (clickedImageResource == R.id.imageView4) {
            // 이미지3와 이미지4를 변경
            boardImage[6] = R.drawable.three_f;
            boardImage[8] = R.drawable.four_cat;
            if (imageView3 != null) {
                imageView3.setImageResource(boardImage[6]);
            }
            if (imageView4 != null) {
                imageView4.setImageResource(boardImage[8]);
            }
        } else if (clickedImageResource == R.id.imageView5) {
            // 이미지4와 이미지5를 변경
            boardImage[12] = R.drawable.six_f;
            boardImage[10] = R.drawable.five_cat;

            if (imageView5 != null) {
                imageView5.setImageResource(boardImage[10]);
            }
            if (imageView6 != null) {
                imageView6.setImageResource(boardImage[12]);
            }
        } else if (clickedImageResource == R.id.imageView6) {
            boardImage[14] = R.drawable.seven_f;
            boardImage[12] = R.drawable.six_cat;

            if (imageView6 != null) {
                imageView6.setImageResource(boardImage[12]);
            }
            if (imageView7 != null) {
                imageView7.setImageResource(boardImage[14]);
            }
        } else if (clickedImageResource == R.id.imageView7) {
            boardImage[16] = R.drawable.eight_f;
            boardImage[14] = R.drawable.seven_cat;

            if (imageView7 != null) {
                imageView7.setImageResource(boardImage[14]);
            }
            if (imageView8 != null) {
                imageView8.setImageResource(boardImage[16]);
            }
        } else if (clickedImageResource == R.id.imageView8) {
            boardImage[8] = R.drawable.four_f;
            boardImage[16] = R.drawable.eight_cat;

            if (imageView8 != null) {
                imageView8.setImageResource(boardImage[16]);
            }
            if (imageView4 != null) {
                imageView4.setImageResource(boardImage[8]);
            }
        } else if (clickedImageResource == R.id.imageView9) {
            boardImage[10] = R.drawable.five_f;
            boardImage[18] = R.drawable.nine_cat;

            if (imageView5 != null) {
                imageView5.setImageResource(boardImage[10]);
            }
            if (imageView9 != null) {
                imageView9.setImageResource(boardImage[18]);
            }
        } else if (clickedImageResource == R.id.imageView10) {
            boardImage[18] = R.drawable.nine_f;
            boardImage[20] = R.drawable.ten_cat;

            if (imageView9 != null) {
                imageView9.setImageResource(boardImage[18]);
            }
            if (imageView10 != null) {
                imageView10.setImageResource(boardImage[20]);
            }
        } else if (clickedImageResource == R.id.imageView11) {
            boardImage[20] = R.drawable.ten_f;
            boardImage[22] = R.drawable.eleven_cat;

            if (imageView10 != null) {
                imageView10.setImageResource(boardImage[20]);
            }
            if (imageView11 != null) {
                imageView11.setImageResource(boardImage[22]);
            }
        } else if (clickedImageResource == R.id.imageView12) {
            boardImage[22] = R.drawable.eleven_f;
            boardImage[24] = R.drawable.twelve_cat;

            if (imageView11 != null) {
                imageView11.setImageResource(boardImage[22]);
            }
            if (imageView12 != null) {
                imageView12.setImageResource(boardImage[24]);
            }
        }
    }
}

