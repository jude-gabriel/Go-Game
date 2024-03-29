package com.example.gogame.GoGame;

import android.app.Activity;

import com.example.gogame.GameFramework.LocalGame;
import com.example.gogame.GameFramework.actionMessage.GameAction;
import com.example.gogame.GameFramework.actionMessage.TimerAction;
import com.example.gogame.GameFramework.infoMessage.GameState;
import com.example.gogame.GameFramework.infoMessage.TimerInfo;
import com.example.gogame.GameFramework.players.GamePlayer;
import com.example.gogame.GameFramework.utilities.GameTimer;
import com.example.gogame.GameFramework.utilities.Tickable;
import com.example.gogame.GoGame.goActionMessage.GoDumbAIAction;
import com.example.gogame.GoGame.goActionMessage.GoForfeitAction;
import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoQuitGameAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;
import com.example.gogame.GoGame.players.GoDumbComputerPlayer;
import com.example.gogame.GoGame.players.GoHumanPlayer1;

import java.lang.annotation.Target;
import java.util.Timer;

/* GoLocalGame
 * The TTTLocalGame class for a simple tic-tac-toe game.  Defines and enforces
 * the game rules; handles interactions with players.
 *
 * @author Brynn Harrington
 * @version October 24
 */
public class GoLocalGame extends LocalGame {

	/* Instance Variables */
    private static final String TAG = "GoLocalGame";	//Logging tag
    private final GameTimer timer;						//Game timer
    private static final int TICK = 1000;				//Tick interval


    /**
	 * Constructor
     * This method builds the Go local game instance.
     */
    public GoLocalGame() {
        // initialize with the superclass
        super();

        // create a new empty Go State object
        super.state = new GoGameState();

        //Initialize the timer
		timer = this.getTimer();
		timer.setInterval(TICK);
    }


    /**
	 * Copy Constructor
     * This method copies the the Go local game instance.
     */
    public GoLocalGame(GoGameState gameState) {
        // initialize with the superclass
        super();

        // create a new empty Go State object
        super.state = new GoGameState(gameState);

        //Copy the timer
		timer = this.getTimer();
		timer.setInterval(TICK);
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
	 */
	@Override
	protected String checkIfGameOver()
    {
		// initialize the current instance of the go game state
		GoGameState state = (GoGameState) super.state;

		//Check if a winning condition is still possible
		if(state.getTotalMoves() > 2) {
			boolean gameOver = state.isOver();

			//If a winning condition is not possible, end game and find winner
			if (gameOver) {
				int p1Score = state.getPlayer1Score();
				int p2Score = state.getPlayer2Score();
				if (p1Score > p2Score) {
					return playerNames[0] + " is the winner. ";
				} else if (p2Score > p1Score) {
					return playerNames[1] + " is the winner. ";
				} else {
					return "Tie game!";
				}
			}
		}

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

		//Initialize forfeit variables
		boolean p1Forfeit = state.getPlayer1Forfeit();
		boolean p2Forfeit = state.getPlayer2Forfeit();

		//Check for a forfeit
		if(p1Forfeit){
			return playerNames[1] + " wins by forfeit! ";
		}
		if(p2Forfeit){
			return playerNames[0] + " wins by forfeit! ";
		}

		// determine which score is greater
		if (currPlayerScore > oppPlayerScore) {
			if (currStoneColor == Stone.StoneColor.BLACK) gameWinner = 0;
			else gameWinner = 1;
		}
		else if(oppPlayerScore > currPlayerScore){
			if(currStoneColor == Stone.StoneColor.WHITE) gameWinner = 0;
			else gameWinner = 1;
		}
		else{
			return "Tie game! ";
		}

		// return the winner of the game
		return playerNames[gameWinner] + " is the winner.";
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
	 *
	 * @author Brynn Harrington
	 * @author Jude Gabriel
	 */
	@Override
	protected boolean takeAction(GameAction action) {
		// ensure the moveAction is not null
		if (action == null ) return false;

		// ensure the state is not null
		assert state != null;

		// get the current game state by calling the super class instructor
		GoGameState state = (GoGameState) super.state;

		//Start the timer after the first move
		if(state.getTotalMoves() == 0){
			timer.start();
		}

		// determine the action to perform based on the action provided
		// handicap action
		if (action instanceof GoHandicapAction) return state.setHandicap();

		// move action
		else if (action instanceof GoMoveAction) {
			// get the row and column position of the player's move
			int row = ((GoMoveAction) action).getX();
			int col = ((GoMoveAction) action).getY();

			// place the player's stone at the selected liberty if possible
			return state.playerMove(row, col);
		}

		// skip turn action
		else if (action instanceof GoSkipTurnAction) return state.skipTurn();

		// forfeit action
		else if (action instanceof GoForfeitAction) return state.forfeit();
		else if(action instanceof GoQuitGameAction){
			System.exit(0);
			return false;
		}

		// otherwise, invalid return false
		else return false;
	}


	/**
	 * Finds which user won the game
	 *
	 * @return 0 if player 0 won, else returns 1
	 * @author Brynn Harrington
	 */
	public int whoWon(){
		// get the string for game over
		String gameOver = checkIfGameOver();

		// if null, return -1
		if(gameOver == null) return -1;

		// if game over, return 0
		if(gameOver.equals(playerNames[0]+" is the winner. ")) return 0;

		// otherwise return 1
		return 1;
	}

	/**
	 * Updates the timer info for all players in the game
	 *
	 * @author Jude Gabriel
	 */
	@Override
	protected void timerTicked(){
		//Set the state and the timer
		GoGameState state = (GoGameState) super.state;
		state.setTime(timer.getTicks());

		//Send the updated state to all users
		this.sendAllUpdatedState();
	}
}
