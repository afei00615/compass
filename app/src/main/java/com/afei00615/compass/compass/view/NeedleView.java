package com.afei00615.compass.compass.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by afei on 14-8-13.
 */
public class NeedleView extends View {

    private int point = 30;

    private Paint circlePaint,linePaint;
    private int radius = 200;

    private int x,y = 0;

    private int horizontal;
    private static float MAX_horizontal = 30;
    private volatile long lastModify;

    private static final String TAG = "needle";
    public NeedleView(Context context){
        this(context,null);
    }
    public NeedleView(Context context,AttributeSet attr){
        super(context,attr);
        int[] location = new int[2];
        this.getLocationInWindow(location);
        circlePaint = new Paint();
        circlePaint.setColor(getResources().getColor(android.R.color.holo_red_dark));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(android.R.color.holo_blue_bright));
        lastModify = System.currentTimeMillis();

    }

    public void setPoint(int point,int horizontal){
        this.point = point;
        this.horizontal = horizontal;
        if(System.currentTimeMillis()-lastModify>1000){
            invalidate();
            lastModify = System.currentTimeMillis();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.d(TAG,this.getWidth()+":" + this.getHeight());
        Log.d(TAG, " origin " + point + " " + horizontal);
        if(x==0){
            x = this.getWidth()/2;
        }
        if(y==0){
            y = this.getHeight()/2;
        }


        float lineX = (float)(x + radius*Math.cos((270+point)*Math.PI/180));
        float lineY = (float)(y + radius*Math.sin((270+point)*Math.PI/180));
        canvas.drawCircle(x,y,radius,circlePaint);
        canvas.drawCircle(x,y,10,circlePaint);
        canvas.drawLine(x,y,lineX,lineY,linePaint);
        canvas.drawLine(20,40,this.getWidth()-20,40,linePaint);
        if(horizontal>MAX_horizontal){
            horizontal = (int)MAX_horizontal;
        }
        float percent = horizontal/MAX_horizontal;
        float point = x + (x-20)*percent;
        Log.d(TAG,"" + point + " " + percent);
        canvas.drawLine(point-5,40,point+5,40,circlePaint);
    }
}
