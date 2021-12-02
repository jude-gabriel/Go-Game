package com.example.gogame.GoGame.players;



import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gogame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gogame.GameFramework.players.GameComputerPlayer;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;


import java.util.ArrayList;
import java.util.Random;

public class GoSmartComputerPlayer2 extends GameComputerPlayer {


    int[][] bestMove;
    GoGameState goGameState;
    GoGameState copyState;
    boolean isPlayer1;
    int x;
    int y;
    int runningScore;




    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public GoSmartComputerPlayer2(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        //Check info exists
        if(info == null){
            return;
        }

        //Check if it was an not your turn
        if(info instanceof NotYourTurnInfo){
            return;
        }

        //Check if the last move was legal
        if(info instanceof IllegalMoveInfo){
            return;
        }

        //Get the current gamestate
        goGameState = (GoGameState) info;

        //Initialize the best move array
        bestMove = new int[9][9];

        //If it is the first move of the game agree to a handicap
        if(goGameState.getTotalMoves() == 0){
            game.sendAction(new GoHandicapAction(this));
        }


        //Check which color stone this AI is
        if(goGameState.getIsPlayer1() == true){
            isPlayer1 = true;
        }
        else{
            isPlayer1 = false;
        }

        x = -1;
        y = -1;
        runningScore = 0;

        resetScore();
        populateScores();
        findBestScore();

        sleep(0.05);
        if(x == -1 || y == -1){
            game.sendAction(new GoSkipTurnAction(this));
        }
        game.sendAction(new GoMoveAction(this, x, y));
    }


    /**
     * resets the scoring array
     */
    public void resetScore(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                bestMove[i][j] = 0;
            }
        }
    }

    /**
     * Finds the score at each spot
     */
    public void populateScores(){
        for(int i = 0; i < goGameState.getBoardSize(); i++){
            for(int j = 0; j < goGameState.getBoardSize(); j++){
                //Create the new gamestate
                copyState = new GoGameState(goGameState);

                //Set it equal to our turn
                if(copyState.getIsPlayer1() != isPlayer1){
                    copyState.skipTurn();
                }

                //Always make sure the game is not over
                copyState.setGameOver(false);

                if(copyState.getGameBoard()[i][j].getStoneColor() != Stone.StoneColor.NONE){
                    bestMove[i][j] = 0;
                    continue;
                }
                boolean isValid = true;
                isValid = copyState.playerMove(i, j);
                if(isValid == false){
                    bestMove[i][j] = 0;
                    continue;
                }

                //Place the score in the array
                if(copyState.getIsPlayer1() == isPlayer1) {
                    bestMove[i][j] = copyState.getPlayer1Score();
                }
                else{
                    bestMove[i][j] = copyState.getPlayer2Score();
                }

            }
        }
    }


    /**
     * Finds the location of the highest score
     */
    public void findBestScore(){
        ArrayList<Integer[]> equalMoves = new ArrayList<Integer[]>();

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(bestMove[i][j] > runningScore){
                    x = i;
                    y = j;
                    runningScore = bestMove[i][j];
                    equalMoves.clear();
                }
                if(bestMove[i][j] == runningScore){
                    Integer[] move = {i, j};
                    equalMoves.add(move);
                }
            }
        }

        if (equalMoves.isEmpty() == false){
            Logger.log("Size", Integer.toString(equalMoves.size()));
            Random rand = new Random();
            int index = 0;
            if(equalMoves.size() > 1) {
                index = rand.nextInt(equalMoves.size() - 1);
            }
            x = equalMoves.get(index)[0];
            y = equalMoves.get(index)[1];
        }
    }
}
