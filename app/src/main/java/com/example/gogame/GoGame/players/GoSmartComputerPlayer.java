package com.example.gogame.GoGame.players;

import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gogame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gogame.GameFramework.players.GameComputerPlayer;
import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;
import java.util.ArrayList;
import java.util.Random;

/**
 * GoSmartComputer
 *
 * this class represents the hard smart computer by implementing
 * a "look-ahead" tree to determine which move will give them
 * the current score based on the current game state
 *
 * @author Brynn Harrington
 */
public class GoSmartComputerPlayer extends GameComputerPlayer
{
    /* INSTANCE VARIABLES */
    GoGameState goGS;    			// instantiate the game state
	int boardSize;					// the current board size
    GoGameState copyState;      	// instantiate a copy of the game state
    boolean SmartAIPlayer;          // track if the smart AI is playing
	int[][] bestMove;           	// instantiate an array containing the best moves
    int row = -1;                    // the x-coordinate of the move
    int col = -1;                    // the y-coordinate of the move
    int runningScore;           	// tracks the winning score

	/**
	 * constructor
	 *
	 * initializes the smart computer by taking in the player's name from its parent
	 *
	 * @param name get the current player's score
	 */
	public GoSmartComputerPlayer(String name) { super(name); }//GoSmartComputerPlayerConstructor

	/**
	 * receiveInfo
	 *
     * updates the game based on the information given
     *
     * @param info the current information of the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {
    	/* ERROR CHECKING */
		assert info != null;						// verify non-null information
		if(info instanceof NotYourTurnInfo) return;	// verify it is the smart AI's turn
		if(info instanceof IllegalMoveInfo) return;	// verify it is a legal move for the AI

        // parse the current information into the game state
        goGS = (GoGameState) info;

        // get the current board size
		boardSize = goGS.getBoardSize();

        // initialize an array to store the best move
        bestMove = new int[boardSize][boardSize];

        // we always assume the smart AI is better so it will always agree to a handicap
        if(goGS.getTotalMoves() == 0) game.sendAction(new GoHandicapAction(this));

        // determine which player the smart AI is
        SmartAIPlayer = goGS.getIsPlayer1();

        // initialize the rows, columns, and current running score
        row = -1;
        col = -1;
        runningScore = 0;

        //Reset the score --- why???
        resetScore();

        // determine the scores at each position
        getPossibleScores();

        // determine the best score
        findBestScore();

        //Sleep to simulate the AI thinking
        sleep(1);

        // verify the move is valid - if not skip
        if(row == -1 || col == -1) game.sendAction(new GoSkipTurnAction(this));
		else game.sendAction(new GoMoveAction(this, row, col));
    }//receiveInfo

    /**
     * resetScore
	 * 
	 * resets the current scores 
     *
     */
    public void resetScore(){ for(int i = 0; i < boardSize; i++) for(int j = 0; j < boardSize; j++) bestMove[i][j] = 0; }//resetScore

    /**
	 * getPossibleScores
	 *
     * determines the possible scores at each spot from the AI
     *
     */
    public void getPossibleScores(){
        //Iterate through the game board and create a copy of the state each iteration
        for(int i = 0; i < goGS.getBoardSize(); i++){
            for(int j = 0; j < goGS.getBoardSize(); j++){
                // copy the current game state
                copyState = new GoGameState(goGS);

                //Make it always be our turn
                if(copyState.getIsPlayer1() != SmartAIPlayer){
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
                if(copyState.getIsPlayer1() == SmartAIPlayer) {
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
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(bestMove[i][j] > runningScore){
                    //Store the location of the highest scoring move
                    row = i;
                    col = j;

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
            row = equalMoves.get(index)[0];
            col = equalMoves.get(index)[1];
        }
    }

    /**
	 * Node
	 *
	 * represents an object of a node that stores:
	 	* x: highest scoring x-coordinate
	 	* y: highest scoring y-coordinate
	 	* s: the score
	 *
	 * @author Brynn Harrington
	 */
    class Node
	{
    	/* INSTANCE VARIABLES */
		private int x;			// x-coordinate
		private int y;			// y-coordinate
		private int s; 		// score of node

		/**
		 * constructor
		 *
		 * this object represents a node in the tree that stores the coordinates and score of the move
		 * */
		public Node(int xCord, int yCord, int score)
		{
			x = xCord;
			y = yCord;
			s = score;
		}

		/**
		 * lambda???
		 *
		 * */

	}

}//GoSmartComputerPlayer