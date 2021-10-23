package com.example.gogame.GoGame.players;

import android.view.View;

import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.players.GameHumanPlayer;

public class GoHumanPlayer2 extends GameHumanPlayer implements View.OnClickListener {


    public GoHumanPlayer2(String name){
        super(name);
    }

    @Override
    public void onClick(View v) {

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
