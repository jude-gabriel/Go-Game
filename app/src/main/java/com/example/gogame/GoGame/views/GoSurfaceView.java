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
    private final Paint blackPaint;     //Paint for black stones
    private final Paint whitePaint;     //Paint for white stones
    private final int radius;           //The radius
    protected GoGameState state;        //The current gamestate

    /**
     * Constructor for the GoSurfaceView class.
     *
     * @param context - a reference to the activity this animation is run under
     * @author Natalie Tashchuk
     */
    public GoSurfaceView(Context context, AttributeSet attrs) {
        //Initialize surface view by calling super
        super(context, attrs);
        init();

        //Initialize board to draw
        setWillNotDraw(false);

        //Populate paint colors
        blackPaint = new Paint();
        blackPaint.setARGB(255, 0, 0, 0);
        whitePaint = new Paint();
        whitePaint.setARGB(255, 255, 255, 255);

        //Set the radius
        radius = 25;
    }

    /**
     * Helper-method for the constructors
     */
    private void init() {
        setBackgroundColor(backgroundColor());

    }// init

    /**
     * Set's the background color
     *
     * @return the color to set the background
     */
    public int backgroundColor() {
        return Color.BLUE;
    }

    /**
     * Setter for the state
     *
     * @param state
     * @author Natalie Tashchuk
     */
    public void setState(GoGameState state) {
        this.state = state;
    }


    /**
     * onDraw
     *
     * @author Natalie Tashchuk
     */
    protected void onDraw(Canvas canvas) {

        //Create a bitmap of the board image. Pass in resources and ID
        //Bitmap gameBoard = BitmapFactory.decodeResource(getResources(), );
        Bitmap gameBoardImage;
        gameBoardImage = BitmapFactory.decodeResource(getResources(), R.drawable.gameboard);
        //Draw the board onto the sv
        canvas.drawBitmap(gameBoardImage, 0.f, 0.f, null);

        //Create an array of stones
        Stone[][] stonesArray = new Stone[9][9];

        //Check if the state exists if not create it
        if (state != null) {
            stonesArray = state.getGameBoard();
        } else {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    stonesArray[i][j] = new Stone((j * 83) + 46, (i * 81) + 48);
                }
            }
        }

        //offset each stone to place it directly on a liberty
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                stonesArray[i][j].draw(canvas, (j * 83) + 46, (i * 81) + 48);
            }
        }
    } //onDraw


    /**
     * findStone
     * finds which index the user clicked on in the stones array
     *
     * @param x - the x location of the user click
     * @param y - the y location of the user click
     * @return an integer array containing the indices
     * @author Natalie Tashchuk
     * @author Jude Gabriel
     */
    public int[] findStone(float x, float y) {
        //Return an error if the state does not exist
        if (state == null) {
            int[] error = {-1, -1};
            return error;
        }
        //initialize indices for error checking
        int iIndex = -1;
        int jIndex = -1;
        int boardSize = state.getBoardSize();
        Stone[][] gameBoard = state.getGameBoard();

        //Iterate through the stones and find which one was clicked
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if ((x < gameBoard[i][j].getxRight() + 20) && (x > gameBoard[i][j].getxLeft() - 20)) {
                    if ((y > gameBoard[i][j].getyTop() - 20) && (y < gameBoard[i][j].getyBottom() + 20)) {
                        //Store the indices of the click
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
