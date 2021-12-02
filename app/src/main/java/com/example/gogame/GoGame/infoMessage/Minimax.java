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



}
