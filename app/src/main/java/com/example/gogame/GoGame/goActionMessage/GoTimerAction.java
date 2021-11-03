package com.example.gogame.GoGame.goActionMessage;

import com.example.gogame.GameFramework.actionMessage.TimerAction;
import com.example.gogame.GameFramework.utilities.GameTimer;

public class GoTimerAction extends TimerAction {

    /**
     * constructor
     *
     * @param timer the timer that caused this action
     */
    public GoTimerAction(GameTimer timer) {
        super(timer);
    }
}
