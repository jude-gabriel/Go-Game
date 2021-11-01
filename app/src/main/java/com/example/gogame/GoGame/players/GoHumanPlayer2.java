package com.example.gogame.GoGame.players;

import android.view.View;

import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.players.GameHumanPlayer;
import com.example.gogame.GoGame.infoMessage.GoGameState;

public class GoHumanPlayer2 extends GameHumanPlayer implements View.OnClickListener {

    //Tag for logging
    private static final String TAG = "GoHumanPlayer2";

    //Current game state
    GoGameState goGameState = null;


    /**
     * Construtor for the human player 2
     *
     * @param name the players name
     */
    public GoHumanPlayer2(String name){
        super(name);
    }


    /**
     * Performs any initialization that needs to be done after the player
     * knows what their game-position adn opponents' names are
     *
     * TODO: Not sure how method needs to come together. Needs more work
     */
    protected void initAfterReady(){
        myActivity.setTitle("Go Game: " + allPlayerNames[0] + " vs " + allPlayerNames[1]);
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



    //Will need a method to get the players score and update it


}