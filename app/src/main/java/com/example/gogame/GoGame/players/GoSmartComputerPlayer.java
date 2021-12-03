 /* PSEUDOCODE FOR MINIMAX W/ ALPHA-BETA PRUNING
  * @source https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-4-alpha-beta-pruning/?ref=lbp

	// first method call
	minimax(0, 0, true, -INFINITY, +INFINITY)

	// algorithmic implementation
  	minimax(node, depth, isMaximizingPlayer, alpha, beta):
    if node is a leaf node :
        return value of the node
    if isMaximizingPlayer :
        bestVal = -INFINITY
        for each child node :
            value = minimax(node, depth+1, false, alpha, beta)
            bestVal = max( bestVal, value)
            alpha = max( alpha, bestVal)
            if beta <= alpha:
                break
        return bestVal
    else :
        bestVal = +INFINITY
        for each child node :
            value = minimax(node, depth+1, true, alpha, beta)
            bestVal = min( bestVal, value)
            beta = min( beta, bestVal)
            if beta <= alpha:
                break
        return bestVal
 */
package com.example.gogame.GoGame.players;

import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.players.GameComputerPlayer;



/**
 * SmartAIMove
 *
 * this class represents a move by the smart AI from the minimax alpha-beta pruning algorithm
 *
 */
class SmartAIMove
{
	/* INSTANCE VARIABLES FOR SMART AI MOVE */
	int row;
	int col;
	int score;

	/**
	 * default constructor
	 *
	 * sets an empty constructor
	 *
	 */
	public SmartAIMove(){}//SmartAIMove

	/**
	 * score constructor
	 *
	 * sets the move's score from the passed in score
	 *
	 * @param s - the score of the move
	 */
	public SmartAIMove(int s) { score = s; }//SmartAIMove

	/**
	 * setting constructor
	 *
	 * sets the move's row, column, and score from the passed in values
	 *
	 * @param r - the row of the move
	 * @param c - the column of the move
	 * @param s - the score of the move
	 */
	public SmartAIMove(int r, int c, int s)
	{
		row = r;
		col = c;
		score = s;
	}//SmartAIMove
}
/**
 * A computerized Go player that recognizes an immediate capture of the
 * opponent or a possible capture from the other opponent, and plays
 * appropriately. If there is not an immediate capture (which it plays)
 * or loss (which it blocks), it moves randomly.
 *
 * The algorithm utilized to maximize difficulty is the Minimax Algorithm with
 * alpha-beta pruning.
 *
 * @author Brynn Harrington
 * @version December 2 2021
 */
public class GoSmartComputerPlayer extends GameComputerPlayer
{
	/* LOGGING TAGS */
	private static final String TAG = "GoSmartComputerPlayer";

	/* INSTANCE VARIABLES FOR SMART AI */
	private final int SCORE = 100;
	private final int ALPHA = Integer.MAX_VALUE;	// represents the maximum value for alpha
	private final int BETA = Integer.MIN_VALUE;		// represents the maximum value for beta

	/**
	 * constructor
	 *
	 * @param name the player's name (e.g., "John")
	 */
	public GoSmartComputerPlayer(String name) { super(name); }//GoSmartComputerPlayer


	/**
	 * receiveInfo
	 *
	 * this method receives information from the game and implements the smart AI
	 * moves accordingly
	 *
	 * @param info the current information of the game
	 */
	@Override
	protected void receiveInfo(GameInfo info)
	{
	}//receiveInfo


}//GoSmartComputerPlayer