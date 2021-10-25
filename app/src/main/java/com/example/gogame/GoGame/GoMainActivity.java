package com.example.gogame.GoGame;

import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.LocalGame;
import com.example.gogame.GameFramework.gameConfiguration.GameConfig;
import com.example.gogame.GameFramework.infoMessage.GameState;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GameFramework.utilities.Saving;
import com.example.gogame.GoGame.infoMessage.GoGameState;

public class GoMainActivity extends GameMainActivity {
    //Tag for logging
    private static final String TAG = "GoMainActivity"

    @Override
    public GameConfig createDefaultConfig() {
        return null;
    }

    @Override
    public LocalGame createLocalGame(GameState gameState) {
        if(gameState == null){
            return new GoLocalGame();
        }
        return new GoLocalGame((GoGameState) gameState);
    }

    /**
     * saveGame, adds this games prepend to the filename
     */
    @Override
    public GameState saveGame(String gameName){
        return super.saveGame(getGameString(gameName));
    }

    /**
     * loadGame, adds this games prepend to the desire file to open and creates the
     * game specific state
     * @param gameName
     *
     * @return The loaded GameState
     *
     */
    @Override
    public GameState loadGame(String gameName){
        String appName = getGameString(gameName);
        super.loadGame(appName);
        Logger.log(TAG, "Loading: " + gameName);
        return (GameState) new GoGameState((GoGameState) Saving.readFromFile(appName, this.getApplicationContext()));

    }
}
