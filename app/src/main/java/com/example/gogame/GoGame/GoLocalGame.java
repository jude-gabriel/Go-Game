package com.example.gogame.GoGame;

import com.example.gogame.GameFramework.LocalGame;
import com.example.gogame.GameFramework.actionMessage.GameAction;
import com.example.gogame.GameFramework.infoMessage.GameState;
import com.example.gogame.GameFramework.players.GamePlayer;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;

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

    /**
	 * Constructor
     * This method builds the Go local game instance.
     */
    public GoLocalGame() {
        // initialize with the superclass
        super();
        // create a new empty Go State object
        super.state = new GameState();
    }

    /**
	 * checkIfGameOver
	 *
	 * Check if the game is over. It is over, return a string that tells
	 * who the winner, if any, is. If the game is not over, return null;
	 *
     * @author Brynn Harrington
     *
	 * @return - a message that tells who has won the game, or null if the
     * game is not over
	 * TODO: Verify the function works
	 */
	@Override
	protected String checkIfGameOver()
    {

        // check if both players have passed
        // calculate the score if the game is over and return the winner

		// initialize the current instance of the go game state
		GoGameState state = (GoGameState) super.state;

		// check if the game is over, if its not return null
        if (!state.isGameOver()) return null;

        // determine the current player color
		Stone.StoneColor currStoneColor;
		Stone.StoneColor oppStoneColor;

		// set the players to the correct stones
		if (state.getPlayer() == 0) {
			currStoneColor = Stone.StoneColor.BLACK;
			oppStoneColor = Stone.StoneColor.WHITE;
		}
		else {
			currStoneColor = Stone.StoneColor.WHITE;
			oppStoneColor = Stone.StoneColor.BLACK;
		}

        // if game is over, then determine the player's scores
		int currPlayerScore = state.calculateScore(currStoneColor, oppStoneColor);
		int oppPlayerScore = state.calculateScore(oppStoneColor, currStoneColor);

		// determine who the winner is
		int gameWinner = -1;

		// determine which schore is greater
		if (currPlayerScore > oppPlayerScore) {
			if (currStoneColor == Stone.StoneColor.BLACK) gameWinner = 0;
			else gameWinner = 1;
		}

		// return the winner of the game
		return playerNames[gameWinner]+" is the winner.";
	}

	/**
	 * sendUpdatedStateTo
	 *
	 * Notify the given player that its state has changed. This should involve sending
	 * a GameInfo object to the player. If the game is not a perfect-information game
	 * this method should remove any information from the game that the player is not
	 * allowed to know.
	 *
	 * @param p - the player to notify
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// make a copy of the state, and send it to the player
		p.sendInfo(new GoGameState(((GoGameState) state)));

	}

	/**
	 * Tell whether the given player is allowed to make a move at the
	 * present point in the game.
	 *
	 * @param playerIdx - the player's player-number (ID)
	 * @return - true iff the player is allowed to move
	 */
	protected boolean canMove(int playerIdx) {
		//Verify the player index is the current player
		return ((GoGameState) state).getPlayer() == playerIdx;
	}

	/**
	 * Makes a move on behalf of a player.
	 *
	 * @param action - The action that the player has sent to the game
	 * @return - Tells whether the move was a legal one.
	 * //TODO - testing
	 */
	//@Override /// unsure if needed // TODO
	protected boolean makeMove(GameAction action) {
		// ensure the moveAction is not null
		if (action == null ) return false;

		// get the current game state by calling the super class instructor
		GoGameState state = (GoGameState) super.state;

		// ensure the state is not null
		assert state != null;

		// determine the action to perform based on the action provided
		// move action
		if (action instanceof GoMoveAction) {
			// get the row and column position of the player's move
			int row = ((GoMoveAction) action).getX();
			int col = ((GoMoveAction) action).getY();

			// get the ID of the current player
			int playerID = getPlayerIdx(action.getPlayer());

			// place the player's stone at the selected liberty if possible
			return state.playerMove(row, col);
		}

		//
/*				int row = ((GoMoveAction) action).getX();
		int col = ((GoMoveAction) action).getY();

		// get the 0/1 id of our player
		int playerId = getPlayerIdx(((GoMoveAction) action).getPlayer());

		// if that space is not blank, indicate an illegal move
		if (state.isValidLocation(row, col)) return false;

		// get the 0/1 id of the player whose move it is
		int whoseMove = state.getPlayer();

		// place the player's piece on the selected liberty
		// NOTE: the player's turn will be switched inside this function
		state.playerMove(row, col);

		// return true, indicating the it was a legal move
		return true;*/

		// otherwise, invalid return false
		return false;
	}

	//TESTING

	public int whoWon(){
		// get the string for game over
		String gameOver = checkIfGameOver();

		// if null, return -1
		if(gameOver == null) return -1;

		// if game over, return 0
		if(gameOver.equals(playerNames[0]+" is the winner.")) return 0;

		// otherwise return 1
		return 1;
	}
}
