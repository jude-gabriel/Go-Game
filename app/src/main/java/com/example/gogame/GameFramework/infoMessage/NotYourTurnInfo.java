package com.example.gogame.GameFramework.infoMessage;

/**
 * The a message from the game to a player that the move just attempted
 * was made at a time when they were not allowed to move.
 *
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class NotYourTurnInfo extends GameInfo {
    //Tag for logging
    private static final String TAG = "NotYourTurnInfo";
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 3417491177980351323L;

}