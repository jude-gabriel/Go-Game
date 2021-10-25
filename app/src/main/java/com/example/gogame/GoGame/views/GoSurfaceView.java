package com.example.gogame.GoGame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.gogame.GameFramework.utilities.FlashSurfaceView;
import com.example.gogame.GoGame.infoMessage.GoGameState;

public class GoSurfaceView extends FlashSurfaceView {

    //Tag for logging
    private static final String TAG = "GoSurfaceView";

    //constants, which are percentages with respect to the minimum
    //of the height and width
    private final static float BORDER_PERCENT = 5; // size of the border
    private Paint blackPaint;
    private Paint whitePaint;
    private int radius;


    protected GoGameState state;

    /**
     * Constructor for the GoSurfaceView class.
     *
     * @param context - a reference to the activity this animation is run under
     */
    public GoSurfaceView(Context context) {

        super(context);
        init();

        //Initialize board to draw
        setWillNotDraw(false);

        //Populate paint colors
        blackPaint = new Paint();
        blackPaint.setARGB(255, 0, 0, 0);
        whitePaint = new Paint();
        whitePaint.setARGB(255, 255, 255, 255);
        radius = 25;
    }

    /**
     * Helper-method for the constructors
     * sets the backgroundColor
     */
    private void init() {
        setBackgroundColor(backgroundColor());
    }// init

    /**
     * @return
     * 		the color to set the background
     */
    public int backgroundColor() {
        return Color.BLUE;
    }

    //method that takes x y float value and maps it to the right liberty
    //and return the r and c index of where i am in the for loop
    //to be called in human player class

    public void setState(GoGameState state) {
        this.state = state;
    }


    protected void onDraw(Canvas canvas){
        //Create a bitmap of the board image. Pass in resources and ID
        //Bitmap gameBoard = BitmapFactory.decodeResource(getResources(), );
        Bitmap gameBoard = BitmapFactory.decodeResource(getResources(),R.);
        //Draw the board on the canvas
        canvas.drawBitmap(gameBoard, 0.f, 0.f, null);

        //Draw a few black chips on the board to represent mid-game
        canvas.drawCircle(70, 70, radius, blackPaint);
        canvas.drawCircle(395, 390, radius, blackPaint);
        canvas.drawCircle(500, 500, radius, blackPaint);
        canvas.drawCircle(500, 390, radius, blackPaint);
        canvas.drawCircle(500, 170, radius, blackPaint);
        canvas.drawCircle(170, 390, radius, blackPaint);
        canvas.drawCircle(500, 280, radius, blackPaint);

        //Draw a few white chips on the board to represent mid-game
        canvas.drawCircle(500, 70, radius, whitePaint);
        canvas.drawCircle(395, 280, radius, whitePaint);
        canvas.drawCircle(610, 390, radius, whitePaint);
        canvas.drawCircle(610, 170, radius, whitePaint);
        canvas.drawCircle(610, 280, radius, whitePaint);
        canvas.drawCircle(500, 500, radius, whitePaint);
        canvas.drawCircle(830, 830, radius, whitePaint);
    }

}
