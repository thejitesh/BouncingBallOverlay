package com.jiteshvartak.movingball;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;

/**
 * Created by jiteshvartak on 3/31/18.
 */

public class BallView extends View {

    private Context mContext;
    private AttributeSet mAttrs;
    private Point viewDimension;
    private Bitmap mBitmap;
    private Paint mPaint;
    private PointF mPosition;
    private PointF mVelocity;
    private PointF mGravity;
    private Point ballDimension;
    private Handler handler;
    private Runnable runnable;

    MediaPlayer  player;

    public BallView(Context context) {
        super(context);
        init(context, null);
    }

    public BallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    public void init(Context context, @Nullable AttributeSet attrs) {

        this.mContext = context;
        this.mAttrs = attrs;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
        mBitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, false);
        mPaint = new Paint();
        mPosition = new PointF();
        mVelocity= new PointF(10,6);
        mGravity= new PointF();
        ballDimension = new Point(70, 70);
        mGravity.x = 0.001f;
        mGravity.y = 0.03f;
        handler = new Handler();



        try {


            AssetManager am = context.getAssets();
            AssetFileDescriptor afd =  context.getResources().openRawResourceFd(R.raw.ball_bounce);
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getLength());
            player.prepare();
//            player.start();
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    // TODO Auto-generated method stub
//                    mp.release();
//                }
//
//            });
 //           player.setLooping(false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        viewDimension = new Point(w,h);
        onResume();
    }

    public void doPhysics(){

        mVelocity.x += mGravity.x;
        mVelocity.y += mGravity.y;

        if(mVelocity.x > 5){
            mVelocity.x = 5;
        }

        if(mVelocity.x < -5){
            mVelocity.x = -5;
        }

        if(mVelocity.y > 5){
            mVelocity.y = 5;
        }

        if(mVelocity.y < -5){
            mVelocity.y = -5;
        }

        if(mPosition.x + ballDimension.x >viewDimension.x){
            mVelocity.x = - Math.abs(mVelocity.x);
            mPosition.x = viewDimension.x - ballDimension.x;
            playSoundAndVibrate();
        }else if(mPosition.x <0){
            mVelocity.x = Math.abs(mVelocity.x);
            mPosition.x = 0;
            playSoundAndVibrate();

        }

        if(mPosition.y + ballDimension.y >viewDimension.y){
            mVelocity.y = - Math.abs(mVelocity.y);
            mPosition.y = viewDimension.y - ballDimension.y;
            playSoundAndVibrate();

        }else if(mPosition.y <0){
            mVelocity.y = Math.abs(mVelocity.y);
            mPosition.y = 0;
            playSoundAndVibrate();

        }

        mPosition.x += mVelocity.x;
        mPosition.y += mVelocity.y;
    }

    private void playSoundAndVibrate() {
        if(player.isPlaying()){
            player.stop();
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        player.start();
        vibrate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mPosition.x, mPosition.y, mPaint);

    }

    public void onResume() {

        resetRunnable();
        runnable = new Runnable() {
            @Override
            public void run() {

                doPhysics();
                invalidate();

                handler.removeCallbacks(this);
                handler.postDelayed(this , 5);
            }
        };
         if(isShown()) {
             handler.postDelayed(runnable, 5);
         }
    }

    private void resetRunnable() {
        if(runnable!= null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void onPause() {

        resetRunnable();
    }


    public void vibrate(){

        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        if (v != null) {
            v.vibrate(20);
        }
    }

}
