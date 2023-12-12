package com.naveen.fusiongridgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.naveen.fusiongridgame.sprites.EndGame;
import com.naveen.fusiongridgame.sprites.Grid;
import com.naveen.fusiongridgame.sprites.Score;
import com.naveen.fusiongridgame.sprites.Tile;

import java.util.jar.Attributes;
//surfaceview class provides place to draw all tiles and users clicks
//surfaceholder used for created,changed,destroyed
public class GameManager extends SurfaceView implements SurfaceHolder.Callback,SwipeCallback,GameManagerCallback{

    private static final String APP_NAME="Fusion Grid Game";
    private MainThread thread; //access all that method into this class

    private Grid grid;
    private int scWidth,scHeight,standardSize;
    private TileManager tileManager;
    private boolean endGame=false;

    private EndGame endgameSprite;
    private Score score;

    private Bitmap restartButton;
    private int restartButtonX,restartButtonY,restartButtonSize;
    private SwipeListener swipe;// class



//canvas is used provide put all images and maintain game state ,tiles

    public GameManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLongClickable(true);// call ontouchevent
        getHolder().addCallback(this);//this is most imp below methids are called using getholder()
        swipe=new SwipeListener(getContext(),this);// calling here swipelistener
        DisplayMetrics dm=new DisplayMetrics();// for window
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        scWidth=dm.widthPixels;// for screeen width size given by display metrics
        scHeight=dm.heightPixels;
        standardSize=(int) (scWidth*.88)/4;// percentage of screen in window

        grid=new Grid(getResources(),scWidth,scHeight,standardSize);// grid class is instanced here actual size of tiles size
        tileManager=new TileManager(getResources(),standardSize,scWidth,scHeight,this);
        endgameSprite=new EndGame(getResources(),scWidth,scHeight);
        score=new Score(getResources(),scWidth,scHeight,standardSize,getContext().getSharedPreferences(APP_NAME,Context.MODE_PRIVATE));
        restartButtonSize = (int) getResources().getDimension(R.dimen.restart_button_size);
        Bitmap bmpRestart = BitmapFactory.decodeResource(getResources(),R.drawable.restart);
        restartButton = Bitmap.createScaledBitmap(bmpRestart,restartButtonSize,restartButtonSize,false);
        restartButtonX=scWidth/2+2*standardSize-restartButtonSize;
        restartButtonY=scHeight/2-2*standardSize-3*restartButtonSize/2;


    }
    public void initGame(){
        endGame=false;
        tileManager.initGame();// call fromtilemanager
        score=new Score(getResources(),scWidth,scHeight,standardSize,getContext().getSharedPreferences(APP_NAME,Context.MODE_PRIVATE));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread=new MainThread(holder,this);//obj for thread
        thread.setRunning(true);
        thread.start();// here we stating the thread
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceHolder(holder);// when system changes the surface we need to hold the thread
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {//destroying the surface works when we app goes to background and comes again
        boolean retry=true;//when we return we want same background
        while (retry){
            try{
                thread.setRunning(false);// off the thread now
                thread.join();//to run first thread
                retry=false;
            }
            catch(InterruptedException e){
                e.printStackTrace();//tells the error in line
            }

        }

    }
    public void update() {
        if (!endGame) {// when endgame of false
            tileManager.update();// update here tilemanager connected here
            System.out.println("donaiphfvaerrih");
        }
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);//this method calls into surfaceview class

        canvas.drawRGB(255,255,255);// for white brackground in window
        grid.draw(canvas);
        tileManager.draw(canvas);
        score.draw(canvas);
        canvas.drawBitmap(restartButton,restartButtonX,restartButtonY,null);
        if (endGame){
            endgameSprite.draw(canvas);

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {// updated into screen by ontouch event
        if (endGame){
            if (event.getAction()==MotionEvent.ACTION_DOWN){//again initalize the game
                initGame();
            }
        }else{
            float eventX=event.getAxisValue(MotionEvent.AXIS_X);
            float eventY=event.getAxisValue(MotionEvent.AXIS_Y);
            if (event.getAction()==MotionEvent.ACTION_DOWN && eventX>restartButtonX && eventX<restartButtonX +restartButtonSize && eventY>restartButtonY && eventY<restartButtonY+restartButtonSize){
                initGame();//know it after some time
            } else{
                swipe.onTouchEvent(event);
            }

        }
        return super.onTouchEvent(event);// this method iverrides the screen
    }

    @Override
    public void onSwipe(Direction direction) {
        tileManager.onSwipe(direction);
    }

    @Override
    public void gameover() {
        endGame=true;
    }

    @Override
    public void updateScore(int delta) {
        score.updateScore(delta);
    }

    @Override
    public void reached2048() {
        score.reached2048();
    }
}

