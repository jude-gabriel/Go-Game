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

/**
 * A computerized Go player that recognizes an immediate capture of the
 * opponent or a possible capture from the other opponent, and plays
 * appropriately. If there is not an immediate capture (which it plays)
 * or loss (which it blocks), it moves randomly.
 *
 * The algorithm utilized to maximize difficulty is the Minimax Algorithm.
 *
 * @author Brynn Harrington
 * @version November 30 2021
 * @source https://www.moderndescartes.com/essays/implementing_go/
 */
public class GoSmartComputerPlayer extends GameComputerPlayer {
	/* LOGGING TAGS */
	private static final String TAG = "GoSmartComputerPlayer";

	 /* INSTANCE / MEMBER VARIABLES */
	// instantiate variables to...
	private GoGameState goGS;			// current game state
	private Stone[][] gameBoard; 		// current game board
	private boolean isSmartAI;			// track if AI is player 1 (black)
	Stone.StoneColor AIStoneColor;		// smart AI's stone color
	Stone.StoneColor oppStoneColor;		// opponent's stone color
	private int boardSize;				// determine the board size
	private static final int winningScore = 100000000;	// winning score (initially "infinity")

	/*
	 * constructor
	 *
	 * @param name the player's name (e.g., "John")
	 */
	public GoSmartComputerPlayer(String name) { super(name); }//GoSmartComputerPlayer

	/*
	 * receiveInfo
	 *
	 * this method receives information from the game and implements the smart AI
	 * moves accordingly
	 *
	 * @param info the current information of the game
	 */
	@Override
	protected void receiveInfo(GameInfo info) {
		// verify this is a valid go game state
		assert info != null;

		// verify it is the smart AI's turn
        if(info instanceof NotYourTurnInfo) return;

		// if an illegal move, return
		if (info instanceof IllegalMoveInfo) return;

		// initialize the global game state variable
		assert info instanceof GoGameState;
		goGS = (GoGameState) info;

		// get the board size
		boardSize = goGS.getBoardSize();

		// initialize the game board
		gameBoard = goGS.deepCopyArray(goGS.getGameBoard());

		// determine if it is player 1's turn
		isSmartAI = goGS.getIsPlayer1();

		// determine the current AI's and opponent's color
		if (isSmartAI)
		{
			AIStoneColor = Stone.StoneColor.BLACK;
			oppStoneColor = Stone.StoneColor.WHITE;
		}
		else
		{
			AIStoneColor = Stone.StoneColor.WHITE;
			oppStoneColor = Stone.StoneColor.BLACK;
			// assuming that AI is better player so if AI is white,
			// always give black more stones
        	if(goGS.getTotalMoves() == 0) { game.sendAction(new GoHandicapAction(this)); }
		}

		//TODO - FIGURE OUT WHERE CRASHING
		// get the current best move starting at depth
		// zero for the algorithm
		int[] nextMove = calculateNextMove(0);

		// send the move to the game object
        Logger.log(TAG, "Smart AI's Move");

        // get the x- and y-coordinates of the best move
		int xNext = nextMove[0];
		int yNext = nextMove[1];

		// have the AI skip their turn if the score is lower
		//TODO - verify this is a valid score to skip on
		// LIKELY THE ISSUE WITH HAVING THE SAME SCORES
		if(winningScore < 1000) {
			Logger.log(TAG, "Smart AI's Skip");
			game.sendAction(new GoSkipTurnAction(this));
		}

		// act as the computer is "thinking"
		sleep(1);

		// send a move action
        game.sendAction(new GoMoveAction(this, xNext, yNext));
	}//receiveInfo

	 /* HELPER FUNCTIONS */
	/*
	 * getWinningScore
	 * getter function for the winning score
	 *
	 * @return  if a winning move was found, a point object containing
	 *   the coordinates.  If no winning move was found, null.
	 */
	public int getWinningScore() { return winningScore; }//getWinningScore

	/*
	 * evaluateBoard
	 *
	 * calculates the relative score of the computer player against
	 * the other player (i.e. how likely the other player is to win
	 * the game before the computer player_
	 *
	 * @return the value to be used as the score in the minimax algorithm
	 */
	public int evaluateBoard() {
		// get the current player
		int player1Score = goGS.getPlayer1Score();
		int player2Score = goGS.getPlayer2Score();

		// determine if current player is black - ensure score is not 0 for division
		if (goGS.getPlayer() == this.playerNum + 1) if (player1Score == 0) player1Score = 1;

		// otherwise, the current player is white - ensure score is not 0 for division
		else if (player2Score == 0) player2Score = 1;

		// return the relative score
		return player2Score / player1Score;
	}//evaluateBoard

	/*
	 * getScore
	 * calculates the board score of the specified player
	 * (i.e. How good a player's general standing on the board by considering how many
	 * consecutive 2's, 3's, 4's it has, how many of them are blocked etc...)
	 *
	 * @return the board score for the specified player
	 */
	public int getScore() { return evaluateHorizontal() + evaluateVertical() +evaluateDiagonal(); }//getScore

	/*
	 * calculateNextMove
	 * This function calculates the next move given the current depth of the board.
	 *
	 * @param depth - the current depth we are searching for the best move at
	 * @return an integer array for the best move
	 *
	 */
	public int[] calculateNextMove(int depth) {
		// define an integer to store the move on the board
		int[] move = new int[2];

		// store an object with the score and x/y coordinates of the best move
		Object[] bestMove = searchWinningMove();

		// verify the best move is not null
		if (bestMove == null) {
			// recall the miniMax search with alpha beta pruning algorithm
			bestMove = miniMaxSearchAB(depth, true, -1.0, getWinningScore());

			// set the moves to the best moves
			if (bestMove[0] != null && bestMove[1] != null)
			{
				move[0] = (Integer) bestMove[1];
				move[1] = (Integer) bestMove[2];
			}
			// otherwise its a null move
			else move = null;
		}

		// otherwise its the best move
		else
		{
			move[0] = (Integer) bestMove[1];
			move[1] = (Integer) bestMove[2];
		}
		// return the move
		return move;
	}//calculateNextMove

	/*
	 * miniMaxSearchAB
	 * This function takes the best possible AI move (the maximum), the best player move (min),
	 * and returns the score for moves at 0 and 1.
	 *
	 * @param depth - the current depth to perform the search on
	 * @param alpha - the best AI move (maximizing player)
	 * @param beta  - the best player move (minimizing player)
	 * @return the score and moves (an object with {score, move[0], move[1]}
	 */
	private Object[] miniMaxSearchAB(int depth, boolean max, double alpha, double beta) {
		// if at terminal node, return the score, move[0], and move[1]
		if (depth == 0) return new Object[]{evaluateBoard(), null, null};

		// initialize an array list for all possible moves
		ArrayList<int[]> allPossibleMoves = generateMoves();

		// determine if there are any possible moves
		if (allPossibleMoves.size() == 0) return new Object[]{evaluateBoard(), null, null};

		// track the best move
		Object[] bestMove = new Object[3];

		// determine if maximizing player
		if (max)
		{
			// reset the best move to an impossible number
			bestMove[0] = -1.0;

			// iterate through the possible moves
			for (int[] move : allPossibleMoves)
			{
				// copy the current game state to access the board
				GoGameState testBoard = new GoGameState(goGS);

				// add the current move to non-displaying GUI
				addStoneNoGUI(testBoard, move[1], move[0]);

				// initialize a temporary move for the other player
				Object[] temporaryMove = miniMaxSearchAB(depth - 1, false, alpha, beta);

				// determine if the move's score is better than the current alpha score
				if ((double) temporaryMove[0] > alpha) alpha = (double) temporaryMove[0];
				if ((double) temporaryMove[0] >= beta) return temporaryMove;

				// reset the best move if the score is higher in the temporary
				if ((double) temporaryMove[0] > (double) bestMove[0])
				{
					bestMove = temporaryMove;
					bestMove[1] = temporaryMove[0];
					bestMove[2] = temporaryMove[1];
				}
			}
		}

		// if the opponent's turn, determine best move
		else
		{
			// set the best move to an unreasonable number
			bestMove[0] = winningScore;

			// reset the possible moves
			bestMove[1] = allPossibleMoves.get(0)[0];
			bestMove[2] = allPossibleMoves.get(0)[1];

			// iterate through the possible moves
			for (int[] move : allPossibleMoves)
			{
				// copy the current game state to access the board
				GoGameState testBoard = new GoGameState(goGS);

				// add the current move to non-displaying GUI
				addStoneNoGUI(testBoard, move[1], move[0]);

				// initialize a temporary move for the other player
				Object[] temporaryMove = miniMaxSearchAB(depth - 1, false, alpha, beta);

				// determine if the move's score is better than the current alpha score
				if ((double) temporaryMove[0] > beta) beta = (double) temporaryMove[0];
				if ((double) temporaryMove[0] >= alpha) return temporaryMove;

				// reset the best move if the score is higher in the temporary
				if ((double) temporaryMove[0] > (double) bestMove[0])
				{
					bestMove = temporaryMove;
					bestMove[1] = temporaryMove[0];
					bestMove[2] = temporaryMove[1];
				}
			}
		}
		// return the best move
		return bestMove;
	}//miniMaxSearchAB

	/*
	 * searchWinningMove
	 * This function looks for a move that can win the game
	 *
	 * @return the winning move
	 */
	private Object[] searchWinningMove() {
		// initialize a new array list for all possible moves
		ArrayList<int[]> moveList = generateMoves();

		// initialize the winning move
		Object[] winMove = new Object[3];

		// iterate through the moves
		for (int[] move : moveList)
		{
			// copy the current game state to access the board
			GoGameState testBoard = new GoGameState(goGS);

			// add the stone to the board without displaying
			addStoneNoGUI(testBoard, move[1], move[0]);

			// determine if the score is greater than the winning score
			if (getScore() >= winningScore)
			{
				// reset the winning moves to the x/y coordinates (first is score)
				winMove[1] = move[0];
				winMove[2] = move[1];
				return winMove;
			}
		}
		// otherwise return null
		return null;
	}//searchWinningMove

	/*
	 * evaluateHorizontal
	 * This function calculates the score by evaluating stone horizontal positions
	 *
	 * ALGORITHMIC IDEA:
	 * If a consecutive stone set is blocked by the opponent or the board border.
	 * If both sides of a consecutive set are blocked, the blocks will be set to two.
	 * If only a single side is blocked, the block variable will be set to one. Otherwise,
	 * if the consecutive set is free and blocks will be zero. By default, the first cell
	 * in a row is blocked by the left border of the board. If the first cell in a row
	 * is empty, then the block count will be decremented by one and if there is another
	 * empty cell after a consecutive stone set, the block count will also be decremented
	 * by one.
	 *
	 * @return the score
	 */
	public int evaluateHorizontal() {
		// initialize the consecutive, blocks, and score variables
		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		// iterate through each index in each row
		for (int row = 0; row < boardSize; row++)
		{
			for (int index = 0; index < boardSize; index++)
			{
				// increment the consecutive if AI has a stone in current cell
				if (gameBoard[row][index].getStoneColor() == AIStoneColor) consecutive++;

				// check if the current index is empty
				else if (gameBoard[row][index].getStoneColor() == Stone.StoneColor.NONE)
				{
					// verify there were consecutive stones before this empty cell
					if (consecutive > 0) {
						// the consecutive set is not blocked by the opponent so
						// decrement block count
						blocks--;

						// get the consecutive set score
						score += getConsecutiveSetScore(consecutive, blocks);

						// reset the consecutive stone count
						consecutive = 0;
					}
					// no consecutive stones - current cell is empty so next consecutive set will
					// have at most 1 blocked side
					blocks = 1;
				}
				// if the cell is occupied by the opponent, check if there were any consecutive stones
				// are before this empty cell
				else if (consecutive > 0) {
					// get the consecutive set score
					score += getConsecutiveSetScore(consecutive, blocks);

					// reset consecutive to zero
					consecutive = 0;

					// current cell blocked by opponent, may have 2 blocked sides
					blocks = 2;
				}
				// current cell blocked by opponent, may have 2 blocked sides
				else blocks = 2;
			}//index loop

			// at the end of the row check if any consecutive stones reached right border
			if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks);
		}//row loop

		// return the score
		return score;
	}//evaluateHorizontal

	/*
	 * evaluateVertical
	 * This function calculates the score by evaluating stone vertical positions
	 *
	 * @return the score
	 */
	public int evaluateVertical() {
		// initialize the consecutive, blocks, and score variables
		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		// iterate through the cells in each column
		for (int index = 0; index < boardSize; index++) {
			for (int col = 0; col < boardSize; col++) {
				// increment the consecutive if AI has a stone in current cell
				if (gameBoard[index][col].getStoneColor() == AIStoneColor) consecutive++;

				// determine if current index is empty
				else if(gameBoard[index][col].getStoneColor() == Stone.StoneColor.NONE)
				{
					// verify consecutive is greater than zero
					if (consecutive > 0) {
						// decrement the blocks
						blocks--;

						// increment the score
						score += getConsecutiveSetScore(consecutive, blocks);
					}

					// otherwise there's a block
					else blocks = 1;
				}

				// determine if consecutive moves are greater than zero
				else if (consecutive > 0)
				{
					// increment the consecutive set score
					score += getConsecutiveSetScore(consecutive, blocks);

					// reset consecutive to zero
					consecutive = 0;

					// set blocks to two
					blocks = 2;
				}

				// otherwise blocked by two
				else blocks = 2;
			}

			// determine if consecutive is greater than zero, increment score
			if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks);

			// reset the consecutive and blocks value to zero and two
			consecutive = 0;
			blocks = 2;
		}

		// return the score
		return score;
	}//evaluateVertical

	/*
	 * evaluateDiagonal
	 * This function calculates the score by evaluating stone horizontal positions
	 *
	 * @return the score
	 */
	public int evaluateDiagonal() {
		// initialize the consecutive, blocks, and score variables
		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		// iterate through the diagonal moves
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				// increment the consecutive if AI has a stone in current cell
				if (gameBoard[row][col].getStoneColor() == AIStoneColor) consecutive++;


				// determine if current index is empty
				else if(gameBoard[row][col].getStoneColor() == Stone.StoneColor.NONE)
				{
					// verify consecutive is greater than zero
					if (consecutive > 0) {
						// decrement the blocks
						blocks--;

						// increment the score
						score += getConsecutiveSetScore(consecutive, blocks);

						// reset consecutive
						consecutive = 0;
					}

					// otherwise there's a block
					blocks = 1;
				}

				// determine if consecutive moves are greater than zero
				else if (consecutive > 0)
				{
					// increment the consecutive set score
					score += getConsecutiveSetScore(consecutive, blocks);

					// reset consecutive to zero
					consecutive = 0;

					// set blocks to two
					blocks = 2;
				}

				// otherwise blocked by two
				else blocks = 2;
			}

			// determine if consecutive is greater than zero, increment score
			if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks);

			// reset the consecutive and blocks value to zero and two
			consecutive = 0;
			blocks = 2;
		}

		// iterate from top-left to the bottom-right across the board diagonally
		for (int diagonal = 1 - boardSize; diagonal < boardSize; diagonal++) {
			// initialize a the start and end for a diagonal row
			int diagonalStart = Math.max(0, diagonal);
			int diagonalEnd = Math.min(boardSize + diagonal - 1, boardSize - 1);

			// iterate through the rows and columns of the board
			for (int row = diagonalStart; row <= diagonalEnd; row++) {
				// get the current column
				int col = diagonal - row;

				// increment the consecutive if AI has a stone in current cell
				if (gameBoard[row][col].getStoneColor() == AIStoneColor) consecutive++;

					// determine if empty
				else if (gameBoard[row][col].getStoneColor() == Stone.StoneColor.NONE) {
					if (consecutive > 0) {
						// decrement the blocks
						blocks--;

						// increment the score
						score += getConsecutiveSetScore(consecutive, blocks);

						// reset consecutive
						consecutive = 0;
					}
					// set blocks - TODO figure out why this value isn't ever used
					//blocks = 1;
				}
				// determine if consecutive is greater than 0
				else if (consecutive > 0) {
					// add to score
					score += getConsecutiveSetScore(consecutive, blocks);
					// reset consecutive
					consecutive = 0;
				}
				// set blocks
				blocks = 2;
			}

			// if consecutive is greater than zero, add to the score
			if(consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks);

			// rest consecutive and blocks
			consecutive = 0;
			blocks = 2;
		}
		// return the score
		return score;
	}//evaluateDiagonal

	/*
	 * getConsecutiveSetScore
	 *
	 * this function determines the score from a given consecutive set
	 *
	 * @param count  - the current count
	 * @param blocks - the number of blocks
	 * @return the score
	 */
	public int getConsecutiveSetScore(int count, int blocks) {
		// initialize a guaranteed winning score for a base point
		final int win = 100000;

		// if both sides of a set are blocked, return zero points
		if (blocks == 2 && count < 5) return 0;

		// determine the consecutive stones in a set
		switch (count)
		{
			// one consecutive stone
			case 1:
				return 1;
			// two consecutive stones
			case 2:
				// determine if not blocked and current turn
				if (blocks == 0)
				{
					// only one side blocked
					if (isSmartAI) return 7;
					else return 5;
				}
				else return 3;
			// three consecutive stones
			case 3:
				// determine if not blocked
				if (blocks == 0) {
					// since neither side blocked, if it's the current player's turn,
					// a win is guaranteed since a user can place a stone to make the
					// count of consecutive stones 4. the opponent may win the game in
					// the next turn though so not maximum score
					if (isSmartAI) return 5000;
					// if not blocked, this forces opponent to block one of the sides
				}
			// four consecutive stones
			case 4:
				// determine if current turn - if so can win by capturing
				if (isSmartAI) return win;
				else {
					// determine if either side blocked - if neither side blocked
					// 4 consecutive stones guarantees a win in the next turn
					if (blocks == 0) return win / 4;

					// if only one side blocked, opponent's moves are limited to placing
					// a stone to block the remaining side
					else return 200;
				}
			// five consecutive stones
			case 5:
				// five consecutive blocks guarantees a capture
				return win;
		}
		// otherwise return 0
		return 0;
	}//getConsecutiveSetScore

	/*
	 * generateMoves
	 *
	 * generates moves given the current board state
	 */
	public ArrayList<int[]> generateMoves() {
		//int emptyCount = 0;

		// initialize the a list of different moves
		ArrayList<int[]> moveList = new ArrayList<>();

		// look for cells that has at least one stone in an adjacent cell
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {

				//TODO - FIGURE OUT WHY THERE IS AN ERROR
				// verify there is at least one adjacent cell
				if (gameBoard[row][col].getStoneColor() == Stone.StoneColor.NONE){
					//emptyCount++;
					continue;
				}

				// determine the row and column are in bounds
				if (row > 0) {
					if (col > 0) {
						// verify the liberty is not empty
						if (gameBoard[row - 1][col - 1].getStoneColor() != Stone.StoneColor.NONE ||
								gameBoard[row][col - 1].getStoneColor() != Stone.StoneColor.NONE) {

							// create a move on the free space
							int[] move = {row, col};

							// add the move to the list
							moveList.add(move);

							// continue to determine if more moves
							continue;
						}
					}

					// verify the liberty is not empty
					if (col < boardSize - 1) {
						if (gameBoard[row - 1][col + 1].getStoneColor() != Stone.StoneColor.NONE ||
								gameBoard[row][col + 1].getStoneColor() != Stone.StoneColor.NONE) {
							// create a move on the free space
							int[] move = {row, col};

							// add the move to the list
							moveList.add(move);

							// continue to determine if more moves
							continue;
						}
					}

					// verify the liberty is not empty
					if (gameBoard[row - 1][col].getStoneColor() != Stone.StoneColor.NONE) {
						// create a move on the free space
							int[] move = {row, col};

							// add the move to the list
							moveList.add(move);

							// continue to determine if more moves
							continue;
					}
				}

				// verify the liberties are in bounds
				if (row < boardSize - 1) {
					if (col > 0) {
						// verify the liberty is not empty
						if (gameBoard[row + 1][col - 1].getStoneColor() != Stone.StoneColor.NONE ||
								gameBoard[row][col - 1].getStoneColor() != Stone.StoneColor.NONE) {

							// create a move on the free space
							int[] move = {row, col};

							// add the move to the list
							moveList.add(move);

							// continue to determine if more moves
							continue;
						}
					}

					// verify the liberty is not empty
					if (col < boardSize - 1) {
						if (gameBoard[row + 1][col + 1].getStoneColor() != Stone.StoneColor.NONE ||
								gameBoard[row][col + 1].getStoneColor() != Stone.StoneColor.NONE) {
							// create a move on the free space
							int[] move = {row, col};

							// add the move to the list
							moveList.add(move);

							// continue to determine if more moves
							continue;
						}
					}

					// verify the liberty is not empty
					if (gameBoard[row + 1][col].getStoneColor() != Stone.StoneColor.NONE) {
						// create a move on the free space
							int[] move = {row, col};

							// add the move to the list
							moveList.add(move);
					}
				}
			}//endCol
		}//endRow

		if (moveList.size() == 0) {
			// initialize a new move at a random place
			int[] move = {1, 1};
			moveList.add(move);
		}

		// return the move list
		return moveList;
	}//generateMoves

	/*
	 * addStoneNoGUI
	 *
	 * adds a stone to the board without displaying it onto the GUI
	 *
	 * @param row - the row to be placed
	 * @param col - the column to be placed
	 */
	public void addStoneNoGUI(GoGameState test, int row, int col) {
		// place a stone on the board
		test.playerMove(row, col);
	}//addStoneNoGUI
}//GoSmartComputerPlayer