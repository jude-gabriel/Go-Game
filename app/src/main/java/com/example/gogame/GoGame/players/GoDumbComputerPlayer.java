package com.example.gogame.GoGame.players;

import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gogame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gogame.GameFramework.players.GameComputerPlayer;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GoGame.goActionMessage.GoForfeitAction;
import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;

import java.util.Random;

public class GoDumbComputerPlayer extends GameComputerPlayer {

    //Instance Variable used to count illegal moves
    private int count;


    /**
     * Constructor for the Dumb AI
     *
     * @param name of the dumb AI
     *
     * @author Mia Anderson
     */
   public GoDumbComputerPlayer(String name){
       //Call super on name and initialize the count
       super(name);
       count = 0;
   }

    /**
     * Receives the current game info and updates the game based on the info recieved
     *
     * @param info
     *
     * @author Mia Anderson
     */
    @Override
    protected void receiveInfo(GameInfo info) {
       //make random variable
        Random rand = new Random();

        //If there have been 81 illegal moves the game is over, forfeit
        if(info instanceof IllegalMoveInfo){
            count++;
            if(count == 81){
                game.sendAction(new GoForfeitAction(this));
            }
            return;
        }
        else{
            count = 0;
        }

        //Check if it is the AI's turn
        if(info instanceof NotYourTurnInfo) return;

        //log message
        Logger.log("GoComputer1", "Computers Turn");


        //Always agree to a handicap if the human does
        GoGameState goGameState = (GoGameState) info;
        if(goGameState.getTotalMoves() == 0){
            game.sendAction(new GoHandicapAction(this));
        }

        //If the human skips turn, dumb ai skips turn
        // --this kind of doesn't make sense because by skipping turn then the human
        // automatically wins
        /*
        if(goGameState.skipTurn()){
            game.sendAction(new GoSkipTurnAction(this));
        }
*/

        // pick random values from 0-9 for the x and y values and a rando value
        //for if the AI will skip a turn
        int xVal = rand.nextInt(9);
        int yVal = rand.nextInt(9);
        int skipVal = rand.nextInt(5);

        // delayed for a second to make the computer think were thinking
        sleep(1);

        // Dumb Ai will only skip randomly after the first 10 moves
        if(goGameState.getTotalMoves() > 10) {
            //If skipVal is 1 skip a turn
            if (skipVal == 1) {
                Logger.log("GoComputer1", "Skipping turn");
                game.sendAction(new GoSkipTurnAction(this));
            }
        }
        // send the move to the game object, if it is not valid this method
        // will continue to be called until a valid position is chosen
        Logger.log("GoComputer1", "Sending move");
        game.sendAction(new GoMoveAction(this, xVal, yVal));

    }
}
