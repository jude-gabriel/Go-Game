package com.example.gogame.GoGame.goActionMessage;

import com.example.gogame.GameFramework.actionMessage.GameAction;
import com.example.gogame.GameFramework.players.GamePlayer;

public class GoMoveAction extends GameAction {

    //Tag for logging
    private static final String TAG = "GoMoveAction";
    private static final long serialVersionUID = -2242980258970485343L;

    /* Instance Variables */
    private int x;  //The x index value of the liberty
    private int y;  //The y index value of the liberty


    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     * @param x the x index of the selected liberty
     * @param y the y index of the selected liberty
     *
     * @author Jude Gabriel
     */
    public GoMoveAction(GamePlayer player, int x, int y) {
        //Invoke the super class
        super(player);

        //set the x and y indices of the liberty
        this.x = x;
        this.y = y;
    }


    /**
     * get the objects x index value
     *
     * @return the x index value
     *
     * @author Jude Gabriel
     */
    public int getX(){
        return x;
    }


    /**
     * get the objects y index value
     *
     * @return the y index value
     *
     * @author Jude Gabriel
     */
    public int getY(){
        return y;
    }

}
