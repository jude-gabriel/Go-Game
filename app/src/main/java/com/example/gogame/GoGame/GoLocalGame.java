package com.example.gogame.GoGame;

import com.example.gogame.GameFramework.LocalGame;
import com.example.gogame.GameFramework.actionMessage.GameAction;
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
        super.state = new GoGameState();
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

    }

    @Override
    protected boolean canMove(int playerIdx) {
        return false;
    }

    @Override
    protected String checkIfGameOver() {
        return null;
    }

    @Override
    protected boolean makeMove(GameAction action) {
        return false;
    }
}
