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
 import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
 import com.example.gogame.GoGame.infoMessage.GoGameState;
 import com.example.gogame.GoGame.infoMessage.Stone;


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
}//SmartAIMove

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
	private final int MAX = Integer.MAX_VALUE;		// represents the maximum value for alpha
	private final int MIN = Integer.MIN_VALUE;		// represents the maximum value for beta
	private final int MAX_DEPTH = 5;				// maximum depth of the tree
	private boolean isSmartAI;						// tracks if its the smart AI's turn
	private int boardSize;							// current size of hte board
	private GoGameState goGS;    					// current game state

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
		// assert the information passed is a non-null instance of a valid GoGameState
		assert info != null;
		assert info instanceof GoGameState;

		// get a copy of the current state passed in
		GoGameState origGoGS = (GoGameState) info;

		// copy the state into the instance variable for modification
		goGS = new GoGameState(origGoGS);

		// set the board size
		boardSize = goGS.getBoardSize();

		// assuming the smart AI is the better player so always do handicap
		if (this.playerNum != 0 && goGS.getTotalMoves() == 0) game.sendAction(new GoHandicapAction(this));

		// change the smart AI's turn to true
		isSmartAI = true;

		// determine the best smart AI move from the algorithm
		SmartAIMove smartAIMove = miniMaxABSearch(0, 0, isSmartAI, 0, MIN, MAX);
	}//receiveInfo

//////////////////	TODO - ADD HEADERS
	public SmartAIMove miniMaxABSearch(int previousRow, int previousCol, boolean isSmartAI, int depth, int alpha, int beta)
	{
		// initialize a variable to store the best move
		SmartAIMove bestMove = new SmartAIMove();

		// determine if the max level is reached
		if (depth == MAX_DEPTH)
		{
			// determine which player it is and set according score
			if (isSmartAI) bestMove.score = SCORE - depth;
			else if (!isSmartAI) bestMove.score = -(SCORE) + depth;
			else bestMove.score = -(depth);
		}//max depth reached

		// determine if it is the smart AI's turn
		else if(isSmartAI)
		{
			// set the best move's score to alpha
			bestMove.score = alpha;

			// initialize a for loop type to break for smart AI
			forLoopSmartAI:
			for (int r = 0; r < boardSize; r++) {
				for (int c = 0; c < boardSize; c++) {
					// get the current liberty color
					Stone.StoneColor stoneColor = goGS.getGameBoard()[r][c].getStoneColor();

					// determine if the current liberty is empty
					if (stoneColor == Stone.StoneColor.NONE)
					{
						// play a move on the board's copy
						goGS.playerMove(r, c);

						// determine the current score
						int s = miniMaxABSearch(r, c, !isSmartAI, depth + 1, bestMove.score, beta).score;

						// if score is greater, reset the values
						if (s > bestMove.score)
						{
							bestMove.row   = r;
							bestMove.col   = c;
							bestMove.score = s;
						}

						// if the beta value is greater than maximum for AI, break
						if (beta <= bestMove.score) break forLoopSmartAI;
					}//end no stone color
				}//end column
			}//end row
		}//smartAI - max depth not reached

		// determine if it is not the smart AI's turn
		else {
			// set the best move's score to beta
			bestMove.score = beta;

			// initialize a for loop type to break for smart AI
			forLoopOpponent:
			for (int r = 0; r < boardSize; r++) {
				for (int c = 0; c < boardSize; c++) {
					// get the current liberty color
					Stone.StoneColor stoneColor = goGS.getGameBoard()[r][c].getStoneColor();

					// determine if the current liberty is empty
					if (stoneColor == Stone.StoneColor.NONE) {
						// play a move on the board's copy
						goGS.playerMove(r, c);

						// determine the current score
						int s =
								miniMaxABSearch(r, c, isSmartAI, depth + 1, alpha, bestMove.score).score;

						// if score is greater, reset the values
						if (s > bestMove.score) {
							bestMove.row = r;
							bestMove.col = c;
							bestMove.score = s;
						}

						// if the beta value is greater, break
						if (bestMove.score <= alpha) break forLoopOpponent;
					}//end no stone color
				}//end column
			}//end row
		}//opponent - max not reached

		// return the best move
		return bestMove;
	}//miniMaxSearchAB

	public SmartAIMove getBestMove(int previousRow, int previousCol, boolean isSmartAI, int depth)
	{
		// initialize a variable to store the best move
		SmartAIMove bestMove = new SmartAIMove();

		// determine if the max level is reached
		if (depth == MAX_DEPTH)
		{
			// determine which player it is and set according score
			if (isSmartAI) bestMove.score = SCORE - depth;
			else if (!isSmartAI) bestMove.score = -(SCORE) + depth;
			else bestMove.score = -(depth);
		}//max depth reached

		// verify it is the smart AI's turn
		if (isSmartAI)
		{
			// set the score to the minimum
			bestMove.score = MIN;

			// iterate through the rows and columns of the current board
			for (int r = 0; r < boardSize; r++) {
				for (int c = 0; c < boardSize; c++) {
					// get the current stone color
					Stone.StoneColor stoneColor = goGS.getGameBoard()[r][c].getStoneColor();

					// determine if the liberty is empty
					if (stoneColor == Stone.StoneColor.NONE)
					{
						// place the smart AI's stone in this position
						goGS.playerMove(r, c);

						// determine the current best move by the alpha beta algorithm
						int s = getBestMove(r, c, !isSmartAI, depth + 1).score;

						// reset moves if the current score is better
						if (s > bestMove.score)
						{
							bestMove.row   = r;
							bestMove.col   = c;
							bestMove.score = s;
						}
					}//end no stone color
				}//end column
			}//end row
		}//end smart AI - max depth not reached

		// determine if it is not the smart AI's turn
		else {
			// set the best move to maximum
			bestMove.score = MAX;
			for (int r = 0; r < boardSize; r++) {
				for (int c = 0; c < boardSize; c++) {
					// get the current liberty color
					Stone.StoneColor stoneColor = goGS.getGameBoard()[r][c].getStoneColor();

					// determine if the current liberty is empty
					if (stoneColor == Stone.StoneColor.NONE) {
						// play a move on the board's copy
						goGS.playerMove(r, c);

						// determine the current score
						int s =
								getBestMove(r, c, isSmartAI, depth + 1).score;

						// if score is greater, reset the values
						if (s < bestMove.score) {
							bestMove.row = r;
							bestMove.col = c;
							bestMove.score = s;
						}
					}//end no stone color
				}//end column
			}//end row
		}

		// return the best move
		return bestMove;
	}//searchBestMove
}//GoSmartComputerPlayer