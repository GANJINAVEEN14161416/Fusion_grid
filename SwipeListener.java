package com.naveen.fusiongridgame;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

public class SwipeListener implements GestureDetector.OnGestureListener{
    private GestureDetector detector;
    private SwipeCallback callback;


// gesture detctor detects every method from ontouch event
    public SwipeListener (Context context, SwipeCallback callback){
        this.callback=callback;
        detector=new GestureDetector(context,this);

    }
    public void onTouchEvent(MotionEvent e){
        detector.onTouchEvent(e);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress( MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {// motion of usere1 is start of motion and e2 is end of motion
        if (Math.abs(velocityX)>Math.abs(velocityY)){
            if (velocityX>0){// opposite operation
                callback.onSwipe(SwipeCallback.Direction.RIGHT);
            }else{
                callback.onSwipe(SwipeCallback.Direction.LEFT);
            }
        }else{
            if (velocityY>0){
                callback.onSwipe(SwipeCallback.Direction.DOWN);
            }
            else{
                callback.onSwipe((SwipeCallback.Direction.UP));
            }

        }
        return false;
    }
}
