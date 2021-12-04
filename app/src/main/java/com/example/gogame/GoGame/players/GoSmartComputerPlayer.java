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
import java.util.Queue;
import java.util.Random;

/**
 * GoSmartComputer
 *
 * this class represents the hard smart computer by implementing
 * a "look-ahead" queue to determine which move will give them
 * the current score based on the current game state
 *
 * @author Brynn Harrington
 */
//from root node, look at next placement to get highest score. With this placement in mind, look at opponent
	// and his last move is the new root mode, make the lowest possible point, then go back to you 
public class GoSmartComputerPlayer extends GameComputerPlayer
{
    /* INSTANCE VARIABLES */
    GoGameState origGoGs;    		// instantiate the game state
	int boardSize;					// the current board size
    GoGameState currentGoGs;      	// instantiate a copy of the game state
    boolean SmartAIPlayer;          // track if the smart AI is playing
	int[][] bestMove;           	// instantiate an array containing the best moves
    int row = -1;                   // the x-coordinate of the move
    int col = -1;                   // the y-coordinate of the move
    int currentScore;           	// tracks the winning score
	Queue<Move> queueBestMoves;			// queue of moves

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
        origGoGs = (GoGameState) info;

        // get the current board size
		boardSize = origGoGs.getBoardSize();

        // initialize an array to store the best move
        bestMove = new int[boardSize][boardSize];

        // we always assume the smart AI is better so it will always agree to a handicap
        if(origGoGs.getTotalMoves() == 0) game.sendAction(new GoHandicapAction(this));

        // determine which player the smart AI is
        SmartAIPlayer = origGoGs.getIsPlayer1();

        // initialize the rows, columns, and current running score
        row = -1;
        col = -1;
        currentScore = 0;
        
        // initialize the queue the null before adding moves
		queueBestMoves = null;

        //TODO ---- Reset the score --- why???
        resetScore();

        // determine the scores at each position
        getPossibleScores();

        // initialize the furthest depth to go to 
		int depth = 0;
		
        // determine the best score
        findOptimalMove(depth);

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
    public void resetScore(){ for(int r = 0; r < boardSize; r++) for(int c = 0; c < boardSize; c++) bestMove[r][c] = 0; }//resetScore

    /**
	 * getPossibleScores
	 *
     * determines the possible scores at each spot from the AI
     *
     */
    public void getPossibleScores(){
		// iterate through the game state and copy at each position to give current game state
        for(int r = 0; r < origGoGs.getBoardSize(); r++){
            for(int c = 0; c < origGoGs.getBoardSize(); c++){
                // copy the current game state
                currentGoGs = new GoGameState(origGoGs);

               /* //
                if(currentGoGs.getIsPlayer1() != SmartAIPlayer){
                    currentGoGs.skipTurn();
                }*/

                // ensure the game is still going
                currentGoGs.setGameOver(false);

                // verify there is a possible move
                if(currentGoGs.getGameBoard()[r][c].getStoneColor() != Stone.StoneColor.NONE){
                    bestMove[r][c] = 0;
                    continue;
                }

                //  verify the move is valid
                boolean isValid = currentGoGs.playerMove(r, c);
                if(!isValid){
                    bestMove[r][c] = 0;
                    continue;
                }

                // place the node into the queue of moves
                if(currentGoGs.getIsPlayer1() == SmartAIPlayer) bestMove[r][c] = currentGoGs.getPlayer1Score();
                else bestMove[r][c] = currentGoGs.getPlayer2Score();
            }
        }
    }

    /**
	 * findOptimalMoves
     *
     * finds the set of optimal moves that can be played
	 *
     */
    public void findOptimalMoves()
	{
        // iterate through the best move array and find the highest scoring move
        for(int r = 0; r < boardSize; r++){
            for(int c = 0; c < boardSize; c++){
				// if greater running score,  clear the arraylist to find new equal moves
                if(bestMove[r][c] > currentScore)
                {
                	// clear the past moves
                	queueBestMoves.clear();
                	
                	// add the new optimal move
					queueBestMoves.add(new Move(r, c, bestMove[r][c]));
                }

                // if there is an equal move, update the array list to store the next best move
                if(bestMove[r][c] == currentScore)
                {
                    Move move = new Move(r, c, currentScore);
                    queueBestMoves.add(move);
                }
            }//end column loop
        }//end row loop
    }//findOptimalMove

     /**
	 * MoveQueue
	 *
	 * represents an object of a node that stores:
	 	* x: highest scoring x-coordinate
	 	* y: highest scoring y-coordinate
	 	* s: the score
	 *
	 * @author Brynn Harrington
	 */
    class MoveQueue
	{
		/* INSTANCE VARIABLES*/
		Move head;				// the head node of the queue
		int depth;				// current depth of the queue
		int bestX;				// best x-coordinate of the queue
		int bestY;				// best y-coordinate of the queue
		int score;				// best score in the queue

		/**
		 * constructor
		 *
		 * this object represents the nodes in the queue and determines the best move through
		 * traversing through the queue and including the score by using this function to
		 * initialize the instance variables
		 */
		public MoveQueue(Move r)
		{
			head = r;
		}
	}

    /**
	 * Move
	 *
	 * represents an object of a node that stores:
	 	* x: highest scoring x-coordinate
	 	* y: highest scoring y-coordinate
	 	* s: the score
	 *
	 * @author Brynn Harrington
	 */
	static class Move
	{
    	/* INSTANCE VARIABLES */
		private int x;			// x-coordinate
		private int y;			// y-coordinate
		private int s; 			// score of node

		/**
		 * constructor
		 *
		 * this object represents a node in the queue that stores the coordinates and score of the move
		 */
		public Move(int xCord, int yCord, int score)
		{
			x = xCord;
			y = yCord;
			s = score;
		}

		/**
		 * getters and setters for instance variables
		 *
		 * these methods get/set the variables based on the calls from the queue
		 */
		public int getX() { return x; }
		public int getY() { return y; }
		public int getS() { return s; }
		public void setX(int newX) { x = newX; }
		public void setY(int newY) { y = newY; }
		public void setS(int newS) { s = newS; }

	}

}//GoSmartComputerPlayer