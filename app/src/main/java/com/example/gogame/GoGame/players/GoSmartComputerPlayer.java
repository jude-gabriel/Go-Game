package com.example.gogame.GoGame.players;

import android.graphics.Point;

import com.example.gogame.GameFramework.Game;
import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.players.GameComputerPlayer;
import com.example.gogame.GameFramework.players.GamePlayer;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;

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
	/** LOGGING TAGS **/
	private static final String TAG = "GoSmartComputerPlayer";

	/** INSTANCE / MEMBER VARIABLES **/
	// current stone color being played - set once player finds out which
	// player they are
	protected Stone.StoneColor stone;

	// current size of the board - initialized to a string for easier
	// copying/printing
	// TODO - alter so have access to current board size
	//int N;
	//char BLACK = 'X';
	//char WHITE = 'O';
	//char EMPTY = '.';
	//int EMPTY_BOARD = EMPTY * N * N;
	// store the current game state
	private GoGameState goGS;

	// store the winning score (storing with a score impossible to begin)
	private static final int winningScore = 1000000000;


	/**
	 * constructor for the smart player
	 *
	 * @param name the player's name
	 */
	public GoSmartComputerPlayer(String name) {
		// invoke superclass constructor
		super(name);
	}// constructor

	/*
	 * perform any initialization that needs to be done after the player
	 * knows what their game-position and opponents' names are.
	 */
	///// TODO figure out how to implement this function

	/*	protected void initAfterReady() {
		// initialize our piece
		piece = "XO".charAt(playerNum);
	}// initAfterReady*/

	/**
	 * Called when the player receives a game-state (or other info) from the
	 * game.
	 *
	 * @param info the message from the game
	 */
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
		/*Point win = findWin(goGS, piece);
		if (win != null) {
			Logger.log("TTTComputer", "sending action");
			game.sendAction(new TTTMoveAction(this, win.y, win.x));
			return;
		}*/

		/*// if we find a threat of a loss (i.e., a direct win for out opponent),
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
		}*/
	}// receiveInfo
	/*
	 * getter function for the winning score
	 *
	 * @return  If a winning move was found, a Point object containing
	 *   the coordinates.  If no winning move was found, null.
	 */

	public static int getWinningScore() {
		return winningScore;
	}
	/*
	 * flattens the 2D board (avoids using a deep copy)
	 *
	 * @param state  the state of the game
	 * @param theStone  the stone we're trying to place ('X' or 'O') for a
	 *   win
	 * @return  If a winning move was found, a Point object containing
	 *   the coordinates.  If no winning move was found, null.
	 */
	//private ?? flattenBoard(GoGameState state, char theStone) {

	/*
	 * un-flattens the 2D board
	 *
	 * @param state  the state of the game
	 * @param flatStone  the stone we're trying to place ('X' or 'O') for a
	 *   win
	 * @return  If a winning move was found, a Point object containing
	 *   the coordinates.  If no winning move was found, null.
	 */
	//private ??? unflattenStone (GoGameState state, char flatStone)
	/**
	 * determine the neighbors of a
	 *
	 * @param state  the state of the game
	 * @param theStone  the stone we're trying to place ('X' or 'O') for a
	 *   win
	 * @return If a winning move was found, a Point object containing
	 *   the coordinates.  If no winning move was found, null.
	 */

	/**
	 * finds a winning move for a player
	 *
	 * @param state    the state of the game
	 * @param thePiece the piece we're trying to place ('X' or 'O') for a
	 *                 win
	 * @return If a winning move was found, a Point object containing
	 * the coordinates.  If no winning move was found, null.
	 */
	private Point findWin(GoGameState state, char thePiece) {

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
	}// findWin

	/**
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
	private Point helpFindWin(TTTState state, char thePiece, int rowStart,
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
			char pc = state.getPiece(row, col);

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
	}// helpFindWin
}
