package com.example.gogame.GoGame.players;

import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gogame.GameFramework.players.GameComputerPlayer;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;

import java.util.Random;

public class GoDumbComputerPlayer extends GameComputerPlayer {

   public GoDumbComputerPlayer(String name){
       super(name);
   }

    /**
     *
     *
     *
     * @param info
     *
     * @author Mia Anderson
     */
    @Override
    protected void receiveInfo(GameInfo info) {
       //make random variable
        Random rand = new Random();
        if(info instanceof NotYourTurnInfo) return;
        //log message?
        Logger.log("GoComputer1", "Computers Turn");

        // pick random values from 0-9 for the x and y values
        int xVal = rand.nextInt(9);
        int yVal = rand.nextInt(9);
        int skipVal = rand.nextInt(7);

        // delayed for a second to make the computer think were thinking
        sleep(1);

        // send the move to the game object, if it is not valid this method
        // will continue to be called until a valid position is chosen

        if(skipVal == 1) {
            Logger.log("GoComputer1", "Skipping turn");
            game.sendAction(new GoSkipTurnAction(this));
        }
        Logger.log("GoComputer1", "Sending move");
        game.sendAction(new GoMoveAction(this, xVal, yVal));

    }
}
