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

import java.util.Queue;

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
        int currentScore = 0;

		// reset the score for each computation
        resetScore();

        // determine the scores at each position
		Queue<Move> queueBestMoves = getPossibleScores(null);

        // determine the optimal moves
        findOptimalMoves(currentScore, queueBestMoves);

        //Sleep to simulate the AI thinking
        sleep(1);

        // verify the move is valid - if not skip
        if(queueBestMoves.size() == 0) game.sendAction(new GoSkipTurnAction(this));
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
     */
    public Queue<Move> getPossibleScores(Queue<Move> queueMoves){
		// iterate through the game state and copy at each position to give current game state
        for(int r = 0; r < origGoGs.getBoardSize(); r++){
            for(int c = 0; c < origGoGs.getBoardSize(); c++){
                // copy the current game state
                currentGoGs = new GoGameState(origGoGs);

                // ensure the game is still going
                currentGoGs.setGameOver(false);

                // verify there is a possible move
                if(currentGoGs.getGameBoard()[r][c].getStoneColor() != Stone.StoneColor.NONE){
                    queueMoves.add(new Move(r, 1));
                    bestMove[r][c] = 0;
                    continue;
                }

                //  verify the move is valid
                if(!currentGoGs.playerMove(r, c))
                {
                    bestMove[r][c] = -1;
                    continue;
                }

                // place the node into the queue of moves
                if(currentGoGs.getIsPlayer1() == SmartAIPlayer) bestMove[r][c] = currentGoGs.getPlayer1Score();
                else bestMove[r][c] = currentGoGs.getPlayer2Score();
            }
        }
        // return the moves in the queue
		return queueMoves;
    }//getPossible Scores

    /**
	 * findOptimalMoves
     *
     * finds the set of optimal moves that can be played
	 *
	 ****** NOTE: this is not entirely optimal because it still relies on random
	 * generations of which move is the best move - however, due to time constraints
	 * the mini max alpha-beta pruning search was unable to be implemented effectively
     */
    public int findOptimalMoves(int currentScore, Queue<Move> queueBestMoves)
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
					queueBestMoves.add(new Move(r, bestMove[r][c]));
                }

                // if there is an equal move, update the array list to store the next best move
                if(bestMove[r][c] == currentScore) queueBestMoves.add(new Move(r, currentScore));
            }//end column loop
        }//end row loop

		// determine a value based on the queue's size
		row = queueBestMoves.element().getX();
        col = queueBestMoves.element().getX();

		// if there are more than one move with the same score, pick a random move
		if(queueBestMoves.size() > 1)
		{
			for (Move ignored : queueBestMoves) {
				// determine the current move
				Move currentMove = queueBestMoves.remove();

				// call the best move on each tied score
				int newScore = findOptimalMoves(currentScore, queueBestMoves);

				// verify the new score isn't better than the initialize in the queue
				assert currentMove != null;
				if (newScore > currentMove.getS()) queueBestMoves.remove();
			}
		}
		// return the best current score
		return currentScore;
    }//findOptimalMove


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
		// y-coordinate
		private int s; 			// score of node

		/**
		 * constructor
		 *
		 * this object represents a node in the queue that stores the coordinates and score of the move
		 */
		public Move(int xCord, int score)
		{
			x = xCord;
			s = score;
		}

		/**
		 * getters and setters for instance variables
		 *
		 * these methods get/set the variables based on the calls from the queue
		 */
		public int getX() { return x; }//getX
		public int getS() { return s; }//getS
		public void setX(int newX) { x = newX; }//setX
		public void setS(int newS) { s = newS; }//setS

	}//Move
}//GoSmartComputerPlayer