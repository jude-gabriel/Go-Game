package com.example.gogame.GoGame.players;

import android.view.MotionEvent;
import android.view.View;

import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.players.GameHumanPlayer;

public class GoHumanPlayer1 extends GameHumanPlayer implements View.OnTouchListener {


    private int layoutId;


    public GoHumanPlayer1(String name, int layoutID){
        super(name);
        this.layoutId = layoutID;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {

    }

    @Override
    public void setAsGui(GameMainActivity activity) {

    }
}
