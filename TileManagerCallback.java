package com.naveen.fusiongridgame;

import android.graphics.Bitmap;

import com.naveen.fusiongridgame.sprites.Tile;

public interface TileManagerCallback {
    Bitmap getBitMap(int count);
    void finishedMoving(Tile t);
    void updateScore(int delta);
    void reached2048();

}
