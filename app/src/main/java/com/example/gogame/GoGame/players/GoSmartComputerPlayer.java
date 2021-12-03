package com.example.gogame.GoGame.players;

import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.players.GameComputerPlayer;

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