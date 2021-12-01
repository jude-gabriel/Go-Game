package com.example.gogame.GoGame;

import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.LocalGame;
import com.example.gogame.GameFramework.ProxyGame;
import com.example.gogame.GameFramework.gameConfiguration.GameConfig;
import com.example.gogame.GameFramework.gameConfiguration.GamePlayerType;
import com.example.gogame.GameFramework.infoMessage.GameState;
import com.example.gogame.GameFramework.players.GamePlayer;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GameFramework.utilities.Saving;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.players.GoDumbComputerPlayer;
import com.example.gogame.GoGame.players.GoHumanPlayer1;
import com.example.gogame.GoGame.players.GoSmartComputerPlayer;
import com.example.gogame.R;

import java.util.ArrayList;

/**
 * Main Activity for Go Game
 *
 * @author Mia Anderson
 * @version
 */
public class GoMainActivity extends GameMainActivity {
    //Tag for logging
    private static final String TAG = "GoMainActivity";
    public static final int PORT_NUMBER = 5213;

    /**
     * The Default Configuration is the human vs the "dumb" computer
     * @return GameConfig
     */
    @Override
    public GameConfig createDefaultConfig() {

        //define allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        //add human player1
        playerTypes.add(new GamePlayerType("Player 1") {
            public GamePlayer createPlayer(String name) {
                return new GoHumanPlayer1(name, R.layout.go_human_player1);
            }
        });

        //add human player
        playerTypes.add(new GamePlayerType("Player 2") {
            public GamePlayer createPlayer(String name) {
                return new GoHumanPlayer1(name, R.layout.go_human_player1);
            }
        });


        //add dumb computer player
        playerTypes.add(new GamePlayerType("Computer Player dumb") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new GoDumbComputerPlayer(name);
            }
        });

        //add smart computer player
        playerTypes.add(new GamePlayerType("Computer Player smart") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new GoSmartComputerPlayer(name);
            }
        });

        //create a game configuration class for Go
        GameConfig defaultConfig = new GameConfig(playerTypes, 2, 2, "Go", PORT_NUMBER);

        //Add default players
        defaultConfig.addPlayer("Human", 0);
        defaultConfig.addPlayer("DumbComputer", 3); // dumb computer player

        // Set the initial information for the remote
        defaultConfig.setRemoteData("Remote Player", "", 1);

        return defaultConfig;
    }

    /**
     * createLocal Game - Creates a new game to run on the server tablet
     * @param gameState
     *              The desired gameState to start at or null for new game
     *
     * @return a new, game-specific instance of a sub-class of the LocalGame class
     */
    @Override
    public LocalGame createLocalGame(GameState gameState) {
        //Check if the gamestate exists
        if (gameState == null) {
            return new GoLocalGame();
        }

        //Return the local game
        return new GoLocalGame((GoGameState) gameState);
    }


    /**
     * saveGame, adds this games prepend to the filename
     *
     * @param gameName
     *              The save name
     * @return a String representation of the save
     */
    @Override
    public GameState saveGame(String gameName) {
        return super.saveGame(getGameString(gameName));
    }

    /**
     * loadGame, adds this games prepend to the desire file to open and creates the
     * game specific state
     *
     * @param gameName
     *              file to open
     * @return The loaded GameState
     */
    @Override
    public GameState loadGame(String gameName) {
        //Get the game name and load the game
        String appName = getGameString(gameName);
        super.loadGame(appName);
        Logger.log(TAG, "Loading: " + gameName);

        //Return the gamestate
        return new GoGameState((GoGameState) Saving.readFromFile(appName, this.getApplicationContext()));

    }
}
