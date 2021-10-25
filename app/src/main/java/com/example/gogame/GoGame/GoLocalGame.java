package com.example.gogame.GoGame;

import com.example.gogame.GameFramework.LocalGame;
import com.example.gogame.GameFramework.actionMessage.GameAction;
import com.example.gogame.GameFramework.infoMessage.GameState;
import com.example.gogame.GameFramework.players.GamePlayer;
import com.example.gogame.GoGame.infoMessage.GoGameState;

/* GoLocalGame
 * The TTTLocalGame class for a simple tic-tac-toe game.  Defines and enforces
 * the game rules; handles interactions with players.
 *
 * @author Brynn Harrington
 * @version October 24
 */
public class GoLocalGame extends LocalGame {
    // initialize a tage for logging the current local game
    private static final String TAG = "GoLocalGame";

    // mark the colors for the players and empty
    public enum Color {
        BLACK,
        WHITE,
        EMPTY
    }
    /* Constructor
     * This method builds the Go local game instance.
     * */
    public GoLocalGame() {
        // initialize with the superclass
        super();
        // create a new empty Go State object
        super.state = new GameState();
    }

    /**
	 * Check if the game is over. It is over, return a string that tells
	 * who the winner, if any, is. If the game is not over, return null;
	 *
     * @author Brynn Harrington
     *
	 * @return - a message that tells who has won the game, or null if the
     * game is not over
	 */
	@Override
	protected String checkIfGameOver()
    {

        // check if both players have passed
        // calculate the score if the game is over and return the winner

		// the character that will eventually contain an 'X' or 'O' if we
		// find a winner

		GoGameState state = (GoGameState) super.state;

		// check if the player forfeited
        if (state.)

			////////////////////////////////////////////////////////////
			// At this point, if any of our three variables is non-blank
			// then we have found a winner.
			////////////////////////////////////////////////////////////

			// if we find a winner, indicate such by setting 'resultChar'
			// to the winning mark.
			if (rowToken != ' ') resultChar = rowToken;
			else if (colToken != ' ') resultChar = colToken;
			else if (diagToken != ' ') resultChar = diagToken;
		}

		// if resultChar is blank, we found no winner, so return null,
		// unless the board is filled up. In that case, it's a cat's game.
		if (resultChar == ' ') {
			if  (moveCount >= 9) {
				// no winner, but all 9 spots have been filled
				return "It's a cat's game.";
			}
			else {
				return null; // no winner, but game not over
			}
		}

		// if we get here, then we've found a winner, so return the 0/1
		// value that corresponds to that mark; then return a message
		int gameWinner = resultChar == mark[0] ? 0 : 1;
		return playerNames[gameWinner]+" is the winner.";
	}

	/**
	 * Notify the given player that its state has changed. This should involve sending
	 * a GameInfo object to the player. If the game is not a perfect-information game
	 * this method should remove any information from the game that the player is not
	 * allowed to know.
	 *
	 * @param p
	 * 			the player to notify
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// make a copy of the state, and send it to the player
		p.sendInfo(new TTTState(((TTTState) state)));

	}

	/**
	 * Tell whether the given player is allowed to make a move at the
	 * present point in the game.
	 *
	 * @param playerIdx
	 * 		the player's player-number (ID)
	 * @return
	 * 		true iff the player is allowed to move
	 */
	protected boolean canMove(int playerIdx) {
		return playerIdx == ((TTTState)state).getWhoseMove();
	}

	/**
	 * Makes a move on behalf of a player.
	 *
	 * @param action
	 * 			The move that the player has sent to the game
	 * @return
	 * 			Tells whether the move was a legal one.
	 */
	@Override
	protected boolean makeMove(GameAction action) {

		// get the row and column position of the player's move
		TTTMoveAction tm = (TTTMoveAction) action;
		TTTState state = (TTTState) super.state;

		int row = tm.getRow();
		int col = tm.getCol();

		// get the 0/1 id of our player
		int playerId = getPlayerIdx(tm.getPlayer());

		// if that space is not blank, indicate an illegal move
		if (state.getPiece(row, col) != ' ') {
			return false;
		}

		// get the 0/1 id of the player whose move it is
		int whoseMove = state.getWhoseMove();

		// place the player's piece on the selected square
		state.setPiece(row, col, mark[playerId]);

		// make it the other player's turn
		state.setWhoseMove(1 - whoseMove);

		// bump the move count
		moveCount++;

		// return true, indicating the it was a legal move
		return true;
	}

	//TESTING

	public int whoWon(){
		String gameOver = checkIfGameOver();
		if(gameOver == null || gameOver.equals("It's a cat's game.")) return -1;
		if(gameOver.equals(playerNames[0]+" is the winner.")) return 0;
		return 1;
	}
}
