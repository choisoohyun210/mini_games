package com.example.team3;

public class MemoryCard {
    private  int imageId;
    private boolean isFaceUp;
    private boolean isMatched;

    public MemoryCard(int imageId, boolean isFaceUp, boolean isMatcheds){
        this.imageId = imageId;
        this.isFaceUp =isFaceUp;
        this.isMatched= isMatcheds;
    }

    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId){
        this.imageId=imageId;
    }

    public boolean isFaceUp(){
        return isFaceUp;
    }
    public void setFaceUp(boolean faceUp){
        isFaceUp=faceUp;
    }

    public boolean isMatched() {
        return isMatched;
    }
    public void setMatched(boolean matched) {
        isMatched = matched;
    }
}
