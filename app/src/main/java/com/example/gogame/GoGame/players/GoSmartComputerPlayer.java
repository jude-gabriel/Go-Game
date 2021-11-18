package com.example.gogame.GoGame.players;

import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gogame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gogame.GameFramework.players.GameComputerPlayer;
import com.example.gogame.GameFramework.players.GameHumanPlayer;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GoGame.goActionMessage.GoDumbAIAction;
import com.example.gogame.GoGame.goActionMessage.GoForfeitAction;
import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoNetworkPlayAction;
import com.example.gogame.GoGame.goActionMessage.GoQuitGameAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.goActionMessage.GoSmartAIAction;
import com.example.gogame.GoGame.goActionMessage.GoTwoPlayerAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;
import com.example.gogame.GoGame.views.GoSurfaceView;
import com.example.gogame.R;

import java.util.ArrayList;

/**
 * A computerized Go player that recognizes an immediate capture of the
 * opponent or a possible capture from the other opponent, and plays
 * appropriately. If there is not an immediate capture (which it plays)
 * or loss (which it blocks), it moves randomly.
 * <p>
 * The algorithm utilized to maximize difficulty is the Minimax Algorithm.
 *
 * @author Brynn Harrington
 * @version October 2021
 * @source https://www.moderndescartes.com/essays/implementing_go/
 */
public class GoSmartComputerPlayer extends GameComputerPlayer {
	/* LOGGING TAGS */
	private static final String TAG = "GoSmartComputerPlayer";

	/* INSTANCE / MEMBER VARIABLES */
	// instantiate a variable to track the current board
	private Stone[][] gameBoard;

	// instantiate a variable to hold the current game state
	private GoGameState goGS;

	// instantiate a variable to hold whether the AI is player 0 or 1
	private boolean isPlayer1;

	// instantiate a variable that tracks the current stone color
	Stone.StoneColor currStoneColor;

	// set the win score to an impossible score initially (representing infinity)
	private static final int winningScore = 100000000;

	/**
	 * constructor
	 *
	 * @param name the player's name (e.g., "John")
	 */
	public GoSmartComputerPlayer(String name) {
		super(name);
	}

	/**
	 * //TODO write what this function does
	 *
	 * @param info the current information of the game
	 */
	@Override
	protected void receiveInfo(GameInfo info) {
		//TODO - verify this is the correct assertion
		// verify this is a valid go game state
		assert info != null;

		// initialize the global game state variable
		goGS = (GoGameState) info;

		// determine which player the AI is
		isPlayer1 = goGS.getPlayer() != 0;

		// determine the current AI's color
		if (isPlayer1) currStoneColor = Stone.StoneColor.WHITE;
		else currStoneColor = Stone.StoneColor.BLACK;


	}

	/* HELPER FUNCTIONS */

	/* getWinningScore
	 * getter function for the winning score
	 *
	 * @return  If a winning move was found, a Point object containing
	 *   the coordinates.  If no winning move was found, null.
	 *
	 * TODO - testing
	 */
	public static int getWinningScore() {
		return winningScore;
	}//getWinningScore

	/**
	 * evaluateBoard
	 *
	 * calculates the relative score of the computer player against
	 * the other player (i.e. how likely the other player is to win
	 * the game before the computer player_
	 *
	 * @return the value to be used as the score in the minimax algorithm
	 *
	 * TODO - testing
	 */
	public double evaluateBoard() {
		// get the current player
		int player0Score = goGS.getPlayer1Score();
		int player1Score = goGS.getPlayer1Score();

		// determine if current player is black
		if (goGS.getPlayer() == this.playerNum && !isPlayer1) {
			// ensure the score for black is not 0 for division
			if (player0Score == 0) player0Score = 1;

			// return the relative score of white against black
			return (double) (player1Score / player0Score);
		}

		// otherwise, the current player is white
		else if (goGS.getPlayer() == this.playerNum && this.playerNum == 1) {
			// ensure the score for black is not 0 for division
			if (player0Score == 0) player0Score = 1;

			// return the relative score of black against white
			return (double) (player1Score / player0Score);
		}

		// otherwise return 0
		else return 0;
	}//evaluateBoard

	/**
	 * getScore
	 * calculates the board score of the specified player
	 * (i.e. How good a player's general standing on the board by considering how many
	 * consecutive 2's, 3's, 4's it has, how many of them are blocked etc...)
	 *
	 * @return the board score for the specified player
	 *
	 * TODO - testing AND FINISH
	 */
	public int getScore() { return evaluateHorizontal() + evaluateVertical() +evaluateDiagonal(); }//getScore

	/**
	 * calculateNextMove
	 * This function calculates the next move given the current depth of the board.
	 *
	 * @param depth - the current depth we are searching for the best move at
	 * @return an integer array for the best move
	 *
	 * //TODO FINISH
	 */
	public int[] calculateNextMove(int depth) {
		// act as the computer is "thinking"
		sleep(1000);

		// initialize an array to store the move
		int move[] = new int[2];

		// return the move
		return move;
	}

	/**
	 * miniMaxSearchAB
	 * This function takes the best possible AI move (the maximum), the best player move (min),
	 * and returns the score for moves at 0 and 1.
	 *
	 * @param depth - the current depth to perform the search on
	 * @param alpha - the best AI move (maximizing player)
	 * @param beta  - the best player move (minimizing player)
	 * @return the score and moves (an object with {score, move[0], move[1]}
	 * <p>
	 * //TODO FINISH
	 */
	private Object[] miniMaxSearchAB(int depth, boolean max, double alpha, double beta) {
		// if at terminal node, return the score, move[0], and move[1]
		if (depth == 0) return new Object[]{evaluateBoard(), null, null};


		return null; // dummy
	}

	/**
	 * searchWinningMove
	 * This function looks for a move that can win the game
	 *
	 * @param gameBoard - the current instance of the game board
	 * @return the winning move
	 * <p>
	 * //TODO FINISH and testing
	 */
	private Object[] searchWinningMove(Stone[][] gameBoard) {
		// initialize a new array list for all possible moves
		// TODO - write generate moves function
		ArrayList<int[]> moveList = generateMoves();

		// initialize the winning move
		Object[] winMove = new Object[3];

		// iterate through the moves
		for (int[] move : moveList)
		{
			// create a new board from the current game state
			GoGameState board = new GoGameState(goGS);
		}

		// initialize an object to store the winning move
		Object[] winningMove = new Object[3];

		return null;
	}

	/**
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
	 * TODO - testing - UNSURE IF CURRENT PLAYER FUNCTIONING PROPERLY
	 *
	 * @return the score
	 */
	public int evaluateHorizontal() {
		// initialize the consecutive, blocks, and score variables
		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		// initialize a variable to determine whose turn it is
		boolean isCurrentPlayer = false;

		// determine the board size (row = col so will be the same)
		int boardSize = goGS.getBoardSize();

		// iterate through each index in each row
		for (int row = 0; row < boardSize; row++) {
			for (int index = 0; index < boardSize; index++) {
				// increment the consecutive if AI has a stone in current cell
				if (gameBoard[row][index].getStoneColor() == currStoneColor)
				{
					consecutive++;

					// set is current player
					isCurrentPlayer = true;
				}

					// check if the current index is empty
				else if (gameBoard[row][index].getStoneColor() == Stone.StoneColor.NONE) {
					// verify there were consecutive stones before this empty cell
					if (consecutive > 0) {
						// the consecutive set is not blocked by the opponent so
						// decrement block count
						blocks--;

						// get the consecutive set score
						score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);

						// reset the consecutive stone count
						consecutive = 0;

						// since the current cell is empty, the next consecutive set will have
						// at most 1 blocked side
						blocks = 1;
					}
					// no consecutive stones - current cell is empty so next consecutive set will
					// have at most 1 blocked side
					else blocks = 1;
				}
				// if the cell is occupied by the opponent, check if there were any consecutive stones
				// are before this empty cell
				else if (consecutive > 0) {
					// get the consecutive set score
					score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);

					// reset consecutive to zero
					consecutive = 0;

					// current cell blocked by opponent, may have 2 blocked sides
					blocks = 2;
				}
				// current cell blocked by opponent, may have 2 blocked sides
				else blocks = 2;
			}//index loop
			// at the end of the row check if any consecutive stones reached right border
			if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);
		}//row loop

		// return the score
		return score;
	}

	/**
	 * evaluateVertical
	 * This function calculates the score by evaluating stone vertical positions
	 *
	 * @return the score
	 *
	 * TODO testing
	 */
	public int evaluateVertical() {
		// initialize the consecutive, blocks, and score variables
		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		// initialize a variable to determine whose turn it is
		boolean isCurrentPlayer = false;

		// determine the board size (row = col so will be the same)
		int boardSize = goGS.getBoardSize();

		// iterate through the cells in each column
		for (int index = 0; index < boardSize; index++) {
			for (int col = 0; col < boardSize; col++) {
				// increment the consecutive if AI has a stone in current cell
				if (gameBoard[index][col].getStoneColor() == currStoneColor)
				{
					consecutive++;

					// set to is current player
					isCurrentPlayer = true;
				}

				// determine if current index is empty
				else if(gameBoard[index][col].getStoneColor() == Stone.StoneColor.NONE)
				{
					// verify consecutive is greater than zero
					if (consecutive > 0) {
						// decrement the blocks
						blocks--;

						// increment the score
						score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);
					}

					// otherwise there's a block
					else blocks = 1;
				}

				// determine if consecutive moves are greater than zero
				else if (consecutive > 0)
				{
					// increment the consecutive set score
					score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);

					// reset consecutive to zero
					consecutive = 0;

					// set blocks to two
					blocks = 2;
				}

				// otherwise blocked by two
				else blocks = 2;
			}

			// determine if consecutive is greater than zero, increment score
			if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);

			// reset the consecutive and blocks value to zero and two
			consecutive = 0;
			blocks = 2;
		}

		// return the score
		return score;
	}//evaluateVertical

	/**
	 * evaluateDiagonal
	 * This function calculates the score by evaluating stone horizontal positions
	 *
	 * @param gameBoard   - the current instance of the game board
	 * @param forPlayer0  - whether evaluating for player 0
	 * @param playersTurn - which player it is
	 * @return the score
	 *
	 * //TODO FINISH and testing
	 */
	public int evaluateDiagonal(Stone[][] gameBoard, boolean forPlayer0, boolean playersTurn) {
		// initialize the consecutive, blocks, and score variables
		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		// initialize a variable to determine whose turn it is
		boolean isCurrentPlayer = false;

		// determine the board size (row = col so will be the same)
		int boardSize = goGS.getBoardSize();

		// iterate through the diagonal moves
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				// increment the consecutive if AI has a stone in current cell
				if (gameBoard[row][col].getStoneColor() == currStoneColor)
				{
					consecutive++;

					// set is current player
					isCurrentPlayer = true;
				}

				// determine if current index is empty
				else if(gameBoard[row][col].getStoneColor() == Stone.StoneColor.NONE)
				{
					// verify consecutive is greater than zero
					if (consecutive > 0) {
						// decrement the blocks
						blocks--;

						// increment the score
						score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);

						// reset consecutive
						consecutive = 0;

						// set blocks to one
						//TODO - SEE WHETHER DECREMENT OR SET BLOCKS TO ONE
						// ALSO DETERMINE WHETHER THE EARLIER EVAL NEEDS IT
						blocks = 1;
					}

					// otherwise there's a block
					else blocks = 1;
				}

				// determine if consecutive moves are greater than zero
				else if (consecutive > 0)
				{
					// increment the consecutive set score
					score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);

					// reset consecutive to zero
					consecutive = 0;

					// set blocks to two
					blocks = 2;
				}

				// otherwise blocked by two
				else blocks = 2;
			}

			// determine if consecutive is greater than zero, increment score
			if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);

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
				if (gameBoard[row][col].getStoneColor() == currStoneColor) consecutive++;

					// determine if empty
				else if (gameBoard[row][col].getStoneColor() == Stone.StoneColor.NONE) {
					if (consecutive > 0) {
						// decrement the blocks
						blocks--;

						// increment the score
						score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);

						// reset consecutive
						consecutive = 0;
					}
					// set blocks
					blocks = 1;
				}
				// determine if consecutive is greater than 0
				else if (consecutive > 0) {
					// add to score
					score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);
					// reset consecutive
					consecutive = 0;
				}
				// set blocks
				blocks = 2;
			}

			// if consecutive is greater than zero, add to the score
			if(consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks, isCurrentPlayer);

			// rest consecutive and blocks
			consecutive = 0;
			blocks = 2;
		}
		// return the score
		return score;
	}//evaluateDiagonal

	/**
	 * getConsecutiveSetScore
	 *
	 * this function determines the score from a given consecutive set
	 *
	 * @param count  - the current count
	 * @param blocks - the number of blocks
	 * @return the score
	 * <p>
	 * //TODO FINISH and testing
	 */
	public int getConsecutiveSetScore(int count, int blocks, boolean isCurrentPlayer) {
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
					if (isCurrentPlayer) return 7;
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
					if (isCurrentPlayer) return 5000;
					// if not blocked, this forces opponent to block one of the sides
				}
			// four consecutive stones
			case 4:
				// determine if current turn - if so can win by capturing
				if (isCurrentPlayer) return win;
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

	/**
	 * generateMoves
	 *
	 * generates moves given the current board state
	 *
	 * @return the list of moves
	 */
	public ArrayList<int[]> generateMoves() {
		// initialize the a list of different moves
		ArrayList<int[]> moveList = new ArrayList<int[]>();

		// get the current game board
		Stone[][] gameBoard = goGS.getGameBoard();

		// determine the board size (row = col so will be the same)
		int boardSize = goGS.getBoardSize();

		// look for cells that has at least one stone in an adjacent cell
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {

				// verify there is at least one adjacent cell
				if (gameBoard[row][col].getStoneColor() == Stone.StoneColor.NONE)
					continue;

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
		// return the move list
		return moveList;
	}//generateMoves
	/**
	 * addStoneNoGUI
	 *
	 * adds a stone to the board without displaying it onto the GUI
	 *
	 * @param board - the board to make the moves on
	 * @param row - the row to be placed
	 * @param col - the column to be placed
	 *
	 *            TODO - TESTING
	 */
	public void addStoneNoGUI(Stone[][] board, int row, int col) {
		board[row][col] = isPlayer1 ? Stone.StoneColor.BLACK : Stone.StoneColor.WHITE;
	}
}//generateMoves

/* */

// OLD CODE - MAY NEED LATER
/*
	// current stone color being played - set once player finds out which
	// player they are
	//protected Stone.StoneColor stone;

	// current size of the board - initialized to a string for easier
	// copying/printing
	// TODO - alter so have access to current board size
	int N;
	char BLACK = 'X';
	char WHITE = 'O';
	char EMPTY = '.';
	int EMPTY_BOARD = EMPTY * N * N;

	// store the current game state
	private GoGameState goGS;

	// store the winning score (storing with a score impossible to begin)
	//private static final int winningScore = 1000000000;


	*//*
	 * constructor for the smart player
	 *
	 * @param name the player's name
	 *//*
	public GoSmartComputerPlayer(String name) {
		// invoke superclass constructor
		super(name);
	}// constructor

	*//*
	 * perform any initialization that needs to be done after the player
	 * knows what their game-position and opponents' names are.
	 *//*
	///// TODO figure out how to implement this function

	*//*	protected void initAfterReady() {
		// initialize our piece
		piece = "XO".charAt(playerNum);
	}// initAfterReady*//*

	*//*
	 * Called when the player receives a game-state (or other info) from the
	 * game.
	 *
	 * @param info the message from the game
	 *//*
	@Override
	protected void receiveInfo(GameInfo info) {

		// verify this is an instance of the GoGameState
		if (!(info instanceof GoGameState)) return;

		// initialize a variable to track the current state
		goGS = (GoGameState) info;

		// if it's not our move, ignore it
		if (goGS.getPlayer() != this.playerNum) return;

		// sleep for a second to make any observers think that we're thinking
		sleep(1);


		// if we find a win, select that move
		Point win = findWin(goGS, piece);
		if (win != null) {
			Logger.log("TTTComputer", "sending action");
			game.sendAction(new TTTMoveAction(this, win.y, win.x));
			return;
		}

		// if we find a threat of a loss (i.e., a direct win for out opponent),
		// select that position as a blocking move.
		char opponentPiece = piece == 'X' ? 'O' : 'X';
		Point loss = findWin(goGS, opponentPiece);
		if (loss != null) {
			Logger.log("TTTComputer", "sending action");
			game.sendAction(new TTTMoveAction(this, loss.y, loss.x));
			return;
		}

		// otherwise, make a move that is randomly selected from the
		// blank squares ...

		// count the spaces
		int spaceCount = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (goGS.getPiece(j, i) == ' ') spaceCount++;
			}
		}

		// generate a random integer in range 0 through #spaces-1
		int selectCount = (int)(spaceCount*Math.random());

		// re-find the space that corresponds to the random integer we
		// just generated; make that move
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (goGS.getPiece(j, i) == ' ') {
					if (selectCount == 0) {
						// make the move
						game.sendAction(new TTTMoveAction(this, j, i));
						return;
					}
					selectCount--;
				}
			}
		}
	}// receiveInfo*/
	/*
	 * finds a winning move for a player
	 *
	 * @param state    the state of the game
	 * @param thePiece the piece we're trying to place ('X' or 'O') for a
	 *                 win
	 * @return If a winning move was found, a Point object containing
	 * the coordinates.  If no winning move was found, null.
	 */
	/*private Point findWin(GoGameState state, char thePiece) {

		// the winning move--initialized to null because we haven't found
		// one yet
		Point found = null;

		// iterate through each of the positions 0, 1 and 2, examining a
		// vertical, horizontal and diagonal on each iteration
		//
		for (int i = 0; i < 3; i++) {

			// winning value we found, if any
			Point temp = null;

			// examine row that begins at (i, 0)
			if ((temp = helpFindWin(state, thePiece, i, 0, 0, 1)) != null) {
				found = temp;
			}

			// examine column that begins at (0, i)
			if ((temp = helpFindWin(state, thePiece, 0, i, 1, 0)) != null) {
				found = temp;
			}

			// examine diagonal that beings at (i, 0).  (When i = 1, we'll
			// actually be redundantly examining a row.)
			if ((temp = helpFindWin(state, thePiece, i, 0, 1 - i, 1)) != null) {
				found = temp;
			}
		}

		// return whatever we've found--either a winning move or null
		return found;
	}// findWin*/

	/*
	 * examines a particular row, column or diagonal to see if a move there
	 * would cause a given player to win.  <p>
	 * <p>
	 * We can examine row by specifying rowDelta=0 and colDelta=1.  We can
	 * examine a column by specifying rowDelta=1 and colDelta=0.  We can
	 * examine a diagonal by specifying rowDelta=1 and colDelta=-1 or
	 * vice versa.
	 *
	 * @param state    the state of the game
	 * @param thePiece the piece that we would place to achieve the win
	 * @param rowStart the row-position of first square in the row/col
	 *                 we're examining
	 * @param colStart the columnPosition of the first square in the row/col
	 *                 we're examining
	 * @param rowDelta the amount to change the row-position to get to the
	 *                 next square we're examining
	 * @param colDelta the amount to change the column-position to get to
	 *                 the next square we're examining
	 * @return If a winning move was found, a Point object containing
	 * the coordinates.  If no winning move was found, null.
	 */
	// helper method to find a winning move
	/*private Point helpFindWin(GoGameState state, char thePiece, int rowStart,
							  int colStart, int rowDelta, int colDelta) {

		// our starting position
		int row = rowStart;
		int col = colStart;

		// number of pieces we've found so far on our line
		int matchingPieceCount = 0;

		// the last spot we've found that contains a blank, if any
		Point blankSpot = null;

		// determine if the three squares in question contain exactly two
		// square of the given piece and one square of that is blank
		//
		for (int i = 0; i < 3; i++) {

			// get the piece at the position
			//char pc = state.getPiece(row, col);

			// if we match the given piece, bump the matching piece-count; otherwise,
			// if we match a blank, set the blank-spot
			if (pc == thePiece) {
				matchingPieceCount++;
			} else if (pc == ' ') {
				blankSpot = new Point(col, row);
			}

			// bump row and column positions for next iteration
			row += rowDelta;
			col += colDelta;
		}

		// at this point, we've examined all three squares.  We have a
		// candidate for a "win" if we matched two pieces and had one blank
		// (i.e., pieceCount and blankSpot is non-null)
		if (matchingPieceCount == 2 && blankSpot != null) {
			// have a winning move
			return blankSpot;
		} else {
			// no winner this time
			return null;
		}
	}// helpFindWin*/