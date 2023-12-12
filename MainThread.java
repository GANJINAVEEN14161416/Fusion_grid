package com.naveen.fusiongridgame;

import android.graphics.Canvas;
import android.view.Surface;
import android.view.SurfaceHolder;

public class MainThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private GameManager gameManager;
    private int targetFPS=60;//frames per second
    private Canvas canvas;//for images
    private boolean running;//for thread rumming state
    public MainThread(SurfaceHolder surfaceHolder,GameManager gameManager){//two parameters
        super();
        this.surfaceHolder=surfaceHolder;// all methods are asseceble from this variable
        this.gameManager=gameManager;//all method comes to this method
    }
    public void setRunning(boolean isRunning){
        running= isRunning;
    }// for verifying running thread
    public void setSurfaceHolder(SurfaceHolder surfaceHolder){
        this.surfaceHolder=surfaceHolder;// for holding the surface

    }
    @Override
    public void run(){// i need to study well this one method
        long startTime,timeMillis,waitTime;
        long totalTime=0;
        int frameCount=0;// run only certain amount of time
        long targetTime=1000/targetFPS; //frames per second which gives time for each frame
        while (running){
            startTime=System.nanoTime();//canvas time
            canvas=null;//stop the surface
            try{
                canvas=surfaceHolder.lockCanvas();// start editing the pixel
                synchronized (surfaceHolder){//synchronized the multiple threads
                    gameManager.update();//update as moves take place
                    gameManager.draw(canvas);//giving access to draw to put some data inside of it

                }

                }catch(Exception e){
                e.printStackTrace();
            }
            finally {
                if (canvas!=null){//if user not kept in background
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);//stop the editing the pixels
                        //finished pixel editing
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }//if phone is very fast its calls hundered times in second
            timeMillis=(System.nanoTime()-startTime)/1000000;
            waitTime=targetTime-timeMillis;
            try{
                if(waitTime>0){
                    sleep(waitTime);//sleping the thread
                }

            }
            catch(Exception e){
                e.printStackTrace();
            }

        }


    }

}
