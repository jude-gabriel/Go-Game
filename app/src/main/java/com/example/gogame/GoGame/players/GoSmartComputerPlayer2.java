package com.example.gogame.GoGame.players;



import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gogame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gogame.GameFramework.players.GameComputerPlayer;
import com.example.gogame.GoGame.goActionMessage.GoForfeitAction;
import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;


import java.util.ArrayList;
import java.util.Random;

/**
 * The Medium smart computer player
 *
 * @author Jude Gabriel
 */
public class GoSmartComputerPlayer2 extends GameComputerPlayer {

    /* Instance variables for the smart computer player 2 */
    int[][] bestMove;           //Stores the amount of points each move can make
    GoGameState goGameState;    //Most recent gamestate
    GoGameState copyState;      //Copy of the gamestate
    boolean isPlayer1;          //Check who's turn it is
    int x;                      //Stores the x coordinate of the move
    int y;                      //Stores the y coordinate of the move
    int runningScore;           //Holds the current highest score


    /**
     * constructor
     *
     * @param name the player's name
     * @author Jude Gabriel
     */
    public GoSmartComputerPlayer2(String name) {
        //Call super on the name of the player
        super(name);
    }


    /**
     * Receives the current game info and updates the game based on the info received
     *
     * @param info
     *
     * @author Jude Gabriel
     */
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
        bestMove = new int[goGameState.getBoardSize()][goGameState.getBoardSize()];

        //If it is the first move of the game agree to a handicap
        if(goGameState.getTotalMoves() == 0){
            game.sendAction(new GoHandicapAction(this));
        }

        //Find what color stone this AI is
        isPlayer1 = goGameState.getIsPlayer1();

        //Initialize the x and y coordinate as well as the running score
        x = -1;
        y = -1;
        runningScore = 0;

        //Reset the score
        resetScore();

        //Find the score for each possible move
        populateScores();

        //Find which one is the highest scoring move
        findBestScore();

        //Sleep to simulate the AI thinking
        sleep(0.05);

        //Get the AI's score and opponents score
        int aiScore = 0;
        int oppScore = 0;
        if(isPlayer1 == true){
            aiScore = goGameState.getPlayer2Score();
            oppScore = goGameState.getPlayer1Score();
        }
        else{
            aiScore = goGameState.getPlayer1Score();
            oppScore = goGameState.getPlayer2Score();
        }

        //Check if the Ai is being beat a while after the game has started
        if(oppScore > aiScore && goGameState.getTotalMoves() > 10){
            if((oppScore - aiScore) > 30){
                //If the AI is losing by at least 30, send a forfeit
                game.sendAction(new GoForfeitAction(this));
            }
        }

        //See if there is a valid move
        if(x == -1 || y == -1){
            game.sendAction(new GoSkipTurnAction(this));
        }

        //Send a move action with the highest scoring move
        game.sendAction(new GoMoveAction(this, x, y));
    }


    /**
     * Resets the scoring array
     *
     * @author Jude Gabriel
     */
    public void resetScore(){
        //Iterate through the array and set the scores to 0
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                bestMove[i][j] = 0;
            }
        }
    }

    /**
     * Finds the score at each spot
     *
     * @author Jude Gabriel
     */
    public void populateScores(){
        //Iterate through the gameboard and create a copy of the state each iteration
        for(int i = 0; i < goGameState.getBoardSize(); i++){
            for(int j = 0; j < goGameState.getBoardSize(); j++){
                //Create the new gamestate
                copyState = new GoGameState(goGameState);

                //Make it always be our turn
                if(copyState.getIsPlayer1() != isPlayer1){
                    copyState.skipTurn();
                }

                //Always make sure the game is not over
                copyState.setGameOver(false);

                //Check that a move is possible
                if(copyState.getGameBoard()[i][j].getStoneColor() != Stone.StoneColor.NONE){
                    bestMove[i][j] = 0;
                    continue;
                }

                //Check if the move is valid or not
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
     *
     * @author Jude Gabriel
     */
    public void findBestScore(){
        //Create an array list to store the locations of equally scoring moves
        ArrayList<Integer[]> equalMoves = new ArrayList<Integer[]>();

        //Iterate through the best move array and find the highest scoring move
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(bestMove[i][j] > runningScore){
                    //Store the location of the highest scoring move
                    x = i;
                    y = j;

                    //Change the running score and clear the arraylist to find new equal moves
                    runningScore = bestMove[i][j];
                    equalMoves.clear();
                }

                //If there is an equal move, update the array list to store it
                if(bestMove[i][j] == runningScore){
                    Integer[] move = {i, j};
                    equalMoves.add(move);
                }
            }
        }

        //Check if there are moves that produce the same highest score
        if (equalMoves.isEmpty() == false){
            //Generate a new random to select the move
            Random rand = new Random();

            //Initialze the random value
            int index = 0;

            //Check if the array list is big enough to generate a random number
            if(equalMoves.size() > 1) {
                //Create a random value
                index = rand.nextInt(equalMoves.size() - 1);
            }

            //Set the locations of the moves
            x = equalMoves.get(index)[0];
            y = equalMoves.get(index)[1];
        }
    }
}
