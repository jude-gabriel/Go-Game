package com.example.gogame.GoGame.goActionMessage;

import com.example.gogame.GameFramework.actionMessage.GameAction;
import com.example.gogame.GameFramework.players.GamePlayer;

public class GoSkipTurnAction extends GameAction {

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     *
     * @author Jude Gabriel
     */
    public GoSkipTurnAction(GamePlayer player) {
        super(player);
    }
}
