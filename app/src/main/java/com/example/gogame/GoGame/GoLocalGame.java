package com.example.gogame.GoGame;

import com.example.gogame.GameFramework.LocalGame;
import com.example.gogame.GameFramework.actionMessage.GameAction;
import com.example.gogame.GameFramework.players.GamePlayer;

/* GoLocalGame
 * A class that knows how to play the game Go. The data in this class represent
 * the state of a game. The state represented by an instance of this class can
 * be a complete state (as might be used by the main Go game activity) or a partial
 * state as it would be seen from the perspective of an individual player.
 *
 * Each game of Go has a unique state definition, so this abstract base class
 * has little inherent functionality.
 *
 * @author Brynn Harrington
 * @version October 22
 */
public abstract class GoLocalGame extends LocalGame {
    // initialize a tage for logging the current local game
    private static final String TAG = "LocalGoGame";

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
