package com.example.gogame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;
import com.example.gogame.GameFramework.Game;
import com.example.gogame.GameFramework.actionMessage.ReadyAction;
import com.example.gogame.GameFramework.infoMessage.GameState;
import com.example.gogame.GameFramework.players.GamePlayer;
import com.example.gogame.GoGame.GoLocalGame;
import com.example.gogame.GoGame.GoMainActivity;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;
import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class GoGameTests {

    //Create a main activity instance variable
    public GoMainActivity goMainActivity;


    /**
     * Sets up our tests for the Go Game functionality
     *
     * @throws Exception
     *
     * TODO: Verify the game gets set up correctly with this test
     *
     * @author Jude Gabriel
     */
    @Before
    public void setup() throws Exception{
        goMainActivity = Robolectric.buildActivity(GoMainActivity.class).create().resume().get();
    }


    /**
     * This method verifies a normal game play
     *
     * TODO: Verify these tests are accurate
     * TODO: Add tests for capturing, repeated boards, etc...
     *
     * @author Jude Gabriel
     */
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
        //TODO: NEED TO ACCESS THE GAMEBOARD HERE, i think i did that, recheck
        Stone[][] gBoard = goGameState.getGameBoard();
        gBoard[0][0].setStoneColor(Stone.StoneColor.BLACK);


        //Testing that two moves in a row wasn't possible
        assertTrue("Game states were not equal", ((GoGameState) goLocalGame.getGameState()).equals(goGameState));

        //Can we place a stone in a place that already has a stone
        goLocalGame.sendAction(new GoMoveAction(player2, 0, 0));

        //Make sure nothing changed
        assertTrue("Game states were not equal", ((GoGameState) goLocalGame.getGameState()).equals(goGameState));


        //Make sure turns do work
        goLocalGame.sendAction(new GoMoveAction(player1, 0, 1));
        goLocalGame.sendAction(new GoMoveAction(player2, 1, 1));

        //Change the actual board to the expected output
        gBoard[0][1].setStoneColor(Stone.StoneColor.WHITE);
        gBoard[1][1].setStoneColor(Stone.StoneColor.BLACK);

        //Check if the GameStates are equal
        assertTrue("Game states were not equal", ((GoGameState) goLocalGame.getGameState()).equals(goGameState));

        //Get to a game over
        goLocalGame.sendAction(new GoSkipTurnAction(player1));
        goLocalGame.sendAction(new GoSkipTurnAction(player2));

        //Set the GameState to being over
        goGameState.setGameOver(true);

        //Check if the GameStates are equal
        assertTrue("Game states were not equal", ((GoGameState) goLocalGame.getGameState()).equals(goGameState));

        //Player 1 should win
        //TODO: Verify this test!!
        assertEquals("Player 1 did not win", 0, goLocalGame.whoWon());

        //Check if you can move after game over
        goLocalGame.sendAction(new GoMoveAction(player1, 2, 2));

        //Check if the GameStates are equal
        assertTrue("Game states were not equal", ((GoGameState) goLocalGame.getGameState()).equals(goGameState));
    }


    /**
     * Tests if an empty constructor gets copied correctly
     *
     * TODO: Verify that this test works
     *
     * @author Jude Gabriel
     */
    @Test
    public void test_CopyConstructorOfState_Empty(){
        GoGameState goGameState = new GoGameState();
        GoGameState copyState = new GoGameState(goGameState);

        assertTrue("Copy constructor did not produce equal state", goGameState.equals(copyState));
    }


    /**
     * Tests if a game in progress gets copied correctly
     *
     * TODO: Verify that these tests work
     *
     * @author Jude Gabriel
     */
    @Test
    public void test_CopyConstructorOfState_InProgress(){
        GoGameState goGameState = new GoGameState();
        goGameState.getGameBoard()[0][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[3][4].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[8][8].setStoneColor(Stone.StoneColor.WHITE);

        GoGameState copyState = new GoGameState(goGameState);

        assertTrue("Copy constructor did not produce equal state", goGameState.equals(copyState));
    }


    /**
     * Tests if a full board copies over correctly
     *
     * TODO: Verify this test works
     *
     * @author Natalie Tashchuk
     */
    @Test
    public void test_CopyConstructorOfState_Full(){
        GoGameState goGameState = new GoGameState();

        //double for loop to place stones in every position on board
        for (int c = 0; c < 9; c = c + 2){
            for (int r = 0; r < 9; r = r + 2){
                //setting white stones
                goGameState.getGameBoard()[r][c].setStoneColor(Stone.StoneColor.WHITE);
            }
        }

        for (int c = 1; c < 9; c = c + 2){
            for (int r = 1; r < 9; r = r + 2){
                //setting black stones
                goGameState.getGameBoard()[r][c].setStoneColor(Stone.StoneColor.BLACK);
            }
        }


        //Set the first row
        goGameState.getGameBoard()[0][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[0][1].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[0][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[0][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[0][4].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[0][5].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[0][6].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[0][7].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[0][8].setStoneColor(Stone.StoneColor.WHITE);

        //Set the second row
        goGameState.getGameBoard()[1][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[1][1].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[1][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[1][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[1][4].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[1][5].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[1][6].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[1][7].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[1][8].setStoneColor(Stone.StoneColor.WHITE);

        //Set the third row
        goGameState.getGameBoard()[2][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[2][1].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[2][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[2][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[2][4].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[2][5].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[2][6].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[2][7].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[2][8].setStoneColor(Stone.StoneColor.WHITE);

        //Set the fourth row
        goGameState.getGameBoard()[3][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[3][1].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[3][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[3][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[3][4].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[3][5].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[3][6].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[3][7].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[3][8].setStoneColor(Stone.StoneColor.WHITE);

        //Set the fifth row
        goGameState.getGameBoard()[4][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[4][1].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[4][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[4][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[4][4].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[4][5].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[4][6].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[4][7].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[4][8].setStoneColor(Stone.StoneColor.WHITE);

        //Set the sixth row
        goGameState.getGameBoard()[5][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[5][1].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[5][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[5][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[5][4].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[5][5].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[5][6].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[5][7].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[5][8].setStoneColor(Stone.StoneColor.WHITE);

        //Set the seventh row
        goGameState.getGameBoard()[6][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[6][1].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[6][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[6][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[6][4].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[6][5].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[6][6].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[6][7].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[6][8].setStoneColor(Stone.StoneColor.WHITE);

        //Set the eighth row
        goGameState.getGameBoard()[7][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[7][1].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[7][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[7][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[7][4].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[7][5].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[7][6].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[7][7].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[7][8].setStoneColor(Stone.StoneColor.WHITE);

        //Set the ninth row
        goGameState.getGameBoard()[8][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[8][1].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[8][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[8][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[8][4].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[8][5].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[8][6].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[8][7].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[8][8].setStoneColor(Stone.StoneColor.WHITE);

        //Create a copy
        GoGameState copyState = new GoGameState(goGameState);

        //Check if the copies are equal
        assertTrue("Copy constructor did not produce equal state", goGameState.equals(copyState));
    }

    /**
     * Tests that placing a stone works correctly
     *
     * @author Natalie Tashchuk
     *
     * TODO: Write method
     */
    public void testPlaceStone(){

    }

    /**
     * Tests that captures are successful
     *
     * TODO: Write method
     * TODO: verify if works
     *
     * @author Brynn Harrington
     */
    @Test
    public void testCapture(){

    }


    /**
     * Tests that a forfeit works correctly
     *
     * TODO: Write method
     * TODO: Verify it works
     *
     * @author Mia Anderson
     */
    @Test
    public void testForfeit(){
        //create button for Forfeit
        View forfeitButton = goMainActivity.findViewById(R.id.forfeitButton);

        //create a local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        // Get the players
        GamePlayer[] gamePlayers = goLocalGame.getPlayers();

        // Send players to the game
        for(GamePlayer gamePlayer: gamePlayers){
            goLocalGame.sendAction(new ReadyAction(gamePlayer));
        }

        // have the players make moves
        GamePlayer player1 = gamePlayers[0];
        GamePlayer player2 = gamePlayers[1];

        goMainActivity.onClick(forfeitButton);

        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();

        boolean didForfeit = goGameState.forfeit();

        assertEquals("Turn did not forfeit", true, didForfeit);

    }

    /**
     * Tests that a turn can be skipped successfully
     *
     * TODO: Write method
     * TODO: Verify it works
     *
     * @author Jude Gabriel
     */
    @Test
    public void testSkipTurn(){
        //Create the button for skipping
        View skipButton = goMainActivity.findViewById(R.id.skipTurnButton);

        //Initialize a local game
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

        goMainActivity.onClick(skipButton);

        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();

        int thePlayer = goGameState.getPlayer();
        assertEquals("Turn did not skip", 2, thePlayer);
    }


    /**
     * Test that a game over happens correctly
     *
     * TODO: Write method
     * TODO: Verify it works
     *
     * @author Brynn Harrington
     */
    @Test
    public void testGameOver(){

    }


    /**
     * Tests that a handicap gets placed correctly
     *
     * TODO: Write method
     * TODO: Verify that it works
     *
     * @author Mia Anderson
     */
    @Test
    public void testHandicap(){

    }


    /**
     * Tests that a repeated position does not work
     *
     * TODO: Write method
     * TODO: Verify that it works
     *
     * @author Jude Gabriel
     */
    @Test
    public void testRepeatedPosition(){

    }


    /**
     * Tests that empty states are equal
     *
     * TODO: Verify that it works
     *
     * @author Jude Gabriel
     */
    @Test
    public void test_Equals_Empty_State(){
        //Create the game states
        GoGameState goGameState = new GoGameState();
        GoGameState equalsState = new GoGameState();

        //Checks if they are equal
        assertTrue("Equals method lists gamestates as not equal", goGameState.equals(equalsState));
    }


    /**
     *  Tests that states in progress are equal
     *
     * TODO: Write method
     * TODO: Verify that it works
     */
    @Test
    public void test_equals_state_inProgress(){

    }


    /**
     * Tests that full states are equal
     *
     * TODO: Write method
     * TODO: Verify that it works
     */
    @Test
    public void test_equals_State_Full(){

    }
}
