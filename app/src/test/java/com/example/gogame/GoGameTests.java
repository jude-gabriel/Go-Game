package com.example.gogame;

import com.example.gogame.GameFramework.Game;
import com.example.gogame.GameFramework.actionMessage.ReadyAction;
import com.example.gogame.GameFramework.players.GamePlayer;
import com.example.gogame.GoGame.GoLocalGame;
import com.example.gogame.GoGame.GoMainActivity;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;


public class GoGameTests {

    public GoMainActivity goMainActivity;

    @Before
    public void setup() throws Exception{
        goMainActivity = Robolectric.buildActivity(GoMainActivity.class).create().resume().get();
    }


    @Test
    public void test_checkGamePlay(){
        /* Starting the game */

        //Set up the buttons
        View skipButton = goMainActivity.findViewById(R.id.skipTurnButton);
        View forfeitButton = goMainActivity.findViewById(R.id.forfeitButton);
        View handicapButton = goMainActivity.findViewById(R.id.handicapButton);

        //Get the created game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        //Get the players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Send the names of the players to the game
        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new ReadyAction(gamePlayer));
        }

        /* Start making moves */
        GamePlayer player1 = gamePlayers[0];
        GamePlayer player2 = gamePlayers[1];

        //Check if two moves in a row is possible
        goLocalGame.sendAction(new GoMoveAction(player1, 0, 0));
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 1));

        //Set expected result
        GoGameState goGameState = new GoGameState();
        //TODO: NEED TO ACCESS THE GAMEBOARD HERE



    }








    //Test Capture
    @Test
    public void testCapture(){

    }


    //Test forfeit
    @Test
    public void testForfeit(){

    }



    //Test Skip turn
    @Test
    public void testSkipTurn(){

    }

    //test game over
    @Test
    public void testGameOver(){

    }

    //test handicap
    @Test
    public void testHandicap(){

    }


    //test repeated board
    @Test
    public void testRepeatedPosition(){

    }

    //test equals method for empty state
    @Test
    public void test_Equals_Empty_State(){

    }


    // test equals method for a state in progress
    @Test
    public void test_equals_state_inProgress(){

    }

    //test equals method for a state that is full
    @Test
    public void test_equals_State_Full(){

    }
}
