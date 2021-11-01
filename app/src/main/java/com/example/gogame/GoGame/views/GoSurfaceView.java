package com.example.gogame.GoGame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.gogame.GameFramework.utilities.FlashSurfaceView;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;
import com.example.gogame.R;

//sv draws whatever the state shows
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
     * @author Natalie Tashchuk
     */
    public GoSurfaceView(Context context, AttributeSet attrs) {

        super(context, attrs);
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
     */
    private void init() {
        setBackgroundColor(backgroundColor());

    }// init

    /**
     * @return the color to set the background
     */
    public int backgroundColor() {
        return Color.BLUE;
    }


    public void setState(GoGameState state) {
        this.state = state;
    }


    /**
     * onDraw
     *
     *
     * @author Natalie Tashchuk
     */
    protected void onDraw(Canvas canvas) {

        //Create a bitmap of the board image. Pass in resources and ID
        //Bitmap gameBoard = BitmapFactory.decodeResource(getResources(), );
        Bitmap gameBoardImage;
        gameBoardImage = BitmapFactory.decodeResource(getResources(), R.drawable.board);
        //Draw the board onto the sv
        canvas.drawBitmap(gameBoardImage, 0.f, 0.f, null);

        Stone[][] stonesArray = state.getGameBoard();

        //offset each stone to place it directly on a liberty
        for (int i = 0; i < state.getBoardSize(); i++){
            for (int j = 0; j < state.getBoardSize(); j++){
                stonesArray[i][j].draw(canvas,(j * 350) + 250, (i * 350) + 20);

            }
        }

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
    } //onDraw

    /**
     * findStone
     * finds which index the user clicked on in the stones array
     *
     * @param x - the x location of the user click
     * @param y - the y location of the user click
     * @return an integer array containing the indices
     * @author Natalie Tashchuk
     */
    public int[] findStone(float x, float y) {
        //initialize indices for error checking
        int iIndex = -1;
        int jIndex = -1;
        int boardSize = state.getBoardSize();
        Stone[][] gameBoard = state.getGameBoard();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if ((x < gameBoard[i][j].getxRight()) && (x > gameBoard[i][j].getxLeft())) {
                    if ((y > gameBoard[i][j].getyTop()) && (y < gameBoard[i][j].getyBottom())) {
                        iIndex = i;
                        jIndex = j;
                    }
                }
            }
        }

        //Create an array to store the index values
        int[] indexArray = new int[2];
        indexArray[0] = iIndex;
        indexArray[1] = jIndex;

        return indexArray;  //Return the array with the index values of the stone
    }
}
