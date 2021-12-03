package com.example.gogame.GoGame.infoMessage;

/**
 * Minimax
 *
 * This class implements the minimax alpha-beta pruning algorithm in Go.
 * The method evaluates the board in order to calculate the best move
 * possible to make the highest score.
 *
 * @author Brynn Harrington
 */

public class Minimax {
    /* LOGGING TAGS */
	private static final String TAG = "MiniMaxAlphaBetaClass";

	/* INSTANCE / MEMBER VARIABLES */
	// instantiate variables to...
	private GoGameState goGS;			// current game state
	private Stone[][] gameBoard; 		// current game board
    private static final int winningScore = 100000000;	// winning score (initially "infinity")

    /**
     *  MINIMAX CONSTRUCTOR
     *
     * This constructor takes in the current GoGameState Board to perform the
     * minimax alpha-beta pruning algorithm on it.
     *
     * @param board - the current game board
     *
     */
    public Minimax(Stone[][] board) { gameBoard = board; }

    /**
     * getWinningScore
	 * getter function for the winning score
	 *
	 * @return  if a winning move was found, a point object containing
	 *   the coordinates.  If no winning move was found, null.
	 */
	public int getWinningScore() { return winningScore; }//getWinningScore

		/**
	 * evaluateBoard
	 *
	 * calculates the relative score of the computer player against
	 * the other player (i.e. how likely the other player is to win
	 * the game before the computer player_
	 *
	 * @return the value to be used as the score in the minimax algorithm
	 *
	 */
	/*public int evaluateBoard() {
		// get the current player scores
		int player0Score = goGS.getPlayer1Score();
		int player1Score = goGS.getPlayer2Score();

		// determine if current player is black - ensure score is not 0 for division
		//if (goGS.getPlayer() == this.playerNum) if (player0Score == 0) player0Score = 1;

		// otherwise, the current player is white - ensure score is not 0 for division
		//else if (player1Score == 0) player1Score = 1;

		// return the relative score
		return player1Score / player0Score;
	}//evaluateBoard*/

}
