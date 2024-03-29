package com.example.gogame.GameFramework.infoMessage;

import com.example.gogame.GameFramework.utilities.GameTimer;

/**
 * The a message from to a player (typically sent by a timer) that the timer's
 * clock has "ticked".
 *
 * @author Steven R. Vegdahl
 * @version July 2013
 */

public class TimerInfo extends GameInfo {
    //Tag for logging
    private static final String TAG = "TimerInfo";
    // to satisfy the Serializable interface
    private static final long serialVersionUID = -7138064704052644451L;

    // the timer that generated this message
    private final GameTimer myTimer;

    /**
     * constructor
     *
     * @param timer
     * 		the timer that generated this "tick"
     */
    public TimerInfo(GameTimer timer) {
        myTimer = timer;
    }

    /**
     * getter method for the timer
     *
     * @return
     * 		the timer that generated the "tick"
     */
    public GameTimer getTimer() {
        return myTimer;
    }

}