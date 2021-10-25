package com.example.gogame.GoGame.players;

import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gogame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gogame.GameFramework.players.GameHumanPlayer;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.views.GoSurfaceView;
import com.example.gogame.R;

public class GoHumanPlayer1 extends GameHumanPlayer implements View.OnTouchListener {

    //Tag for logging
    private static final String TAG = "GoHumanPlayer1";


    //Surface view
    private GoSurfaceView goSurfaceView;

    //ID for the layout to use
    private int layoutId;


    public GoHumanPlayer1(String name, int layoutID){
        super(name);
        this.layoutId = layoutID;
    }

    @Override
    public void receiveInfo(GameInfo info) {
        if(goSurfaceView == null){
            return;
        }

        if(info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo){
            goSurfaceView.flash(Color.RED, 1000);
        }
        else if(!(info instanceof GoGameState)){
            return;
        }
        else{
            goSurfaceView.setState((GoGameState)info);
            goSurfaceView.invalidate();
            Logger.log(TAG, "receiving");
        }

    }

    @Override
    public void setAsGui(GameMainActivity activity) {
        activity.setContentView(layoutId);

        //SURFACE VIEW DOES NOT EXIST YET
        goSurfaceView = (GoSurfaceView)myActivity.findViewById(R.id.surfaceView);

        Logger.log("set listener", "onTouch");
    }


    @Override
    public View getTopView() {
        //DOESN'T EXIST YET
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    public void initAfterReady(){
        myActivity.setTitle("Go: " + allPlayerNames[0] + " vs " + allPlayerNames[1]);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Check if the action has ended
        if(event.getAction() != MotionEvent.ACTION_UP){
            return true;
        }

        //Get the coordinates of a press-location and map it to a liberty
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point p = goSurfaceView.mapPixelToLiberty(x, y);

        //Check if the location was not valid and flash screen if so
        if(p == null){
            goSurfaceView.flash(Color.RED, 1000);
        }
        else{
            //Create a new action and send it to the game
            GoMoveAction action = new GoMoveAction(this, p.x, p.y);
            Logger.log("onTouch", "Human player sending Go Move Action");
            game.sendAction(action);

            //Update the surface view
            goSurfaceView.invalidate();
        }

        //Event finished
        return true;
    }


}
