package com.example.gogame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;
import com.example.gogame.GameFramework.Game;
import com.example.gogame.GameFramework.actionMessage.MyNameIsAction;
import com.example.gogame.GameFramework.actionMessage.ReadyAction;
import com.example.gogame.GameFramework.infoMessage.GameState;
import com.example.gogame.GameFramework.players.GamePlayer;
import com.example.gogame.GoGame.GoLocalGame;
import com.example.gogame.GoGame.GoMainActivity;
import com.example.gogame.GoGame.goActionMessage.GoDumbAIAction;
import com.example.gogame.GoGame.goActionMessage.GoForfeitAction;
import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;
import android.view.View;
import android.widget.Button;

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
     * @author Jude Gabriel
     */
    @Before
    public void setup() throws Exception{
        goMainActivity = Robolectric.buildActivity(GoMainActivity.class).create().resume().get();
    }


    /**
     * This method verifies a normal game play
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

        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //Get the created game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        assertTrue(goLocalGame != null);

        //Get the players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new MyNameIsAction(gamePlayer, gamePlayer.getClass().toString()));
        }

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
        Stone[][] gBoard = goGameState.getGameBoard();
        gBoard[0][0].setStoneColor(Stone.StoneColor.BLACK);
        gBoard[1][1].setStoneColor(Stone.StoneColor.WHITE);


        //Testing that two moves in a row wasn't possible
        assertTrue("Game states equal", !((GoGameState) goLocalGame.getGameState()).equals(goGameState));

        //Can we place a stone in a place that already has a stone
        goLocalGame.sendAction(new GoMoveAction(player2, 0, 0));

        //Make sure nothing changed
        assertTrue("Game states were equal", !((GoGameState) goLocalGame.getGameState()).equals(goGameState));


        //Make sure turns do work
        goLocalGame.sendAction(new GoMoveAction(player2, 0, 1));
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 1));

        //Change the actual board to the expected output
        gBoard[0][1].setStoneColor(Stone.StoneColor.WHITE);
        gBoard[1][1].setStoneColor(Stone.StoneColor.BLACK);

        //Check if the GameStates are equal
        assertTrue("Game states were not equal", ((GoGameState) goLocalGame.getGameState()).equals(goGameState));

        //Get to a game over
        goLocalGame.sendAction(new GoSkipTurnAction(player2));
        goLocalGame.sendAction(new GoSkipTurnAction(player1));

        //Set the GameState to being over
        goGameState.setGameOver(true);

        //Check if the GameStates are equal
        assertTrue("Game states were not equal", ((GoGameState) goLocalGame.getGameState()).equals(goGameState));

        //Player 1 should win
        //TODO: Verify this test!!
        assertEquals("Player 1 did not win", 1, goLocalGame.whoWon());

        //Check if you can move after game over
        goLocalGame.sendAction(new GoMoveAction(player1, 2, 2));

        //Check if the GameStates are equal
        assertTrue("Game states were not equal", ((GoGameState) goLocalGame.getGameState()).equals(goGameState));
    }


    /**
     * Tests if an empty constructor gets copied correctly
     *
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
     *
     * @author Jude Gabriel
     */
    @Test
    public void test_CopyConstructorOfState_Full(){
        GoGameState goGameState = new GoGameState();

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
     * @author Mia Anderson
     */
    @Test
    public void testForfeit(){
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);


        //create a local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();
        assertTrue(goLocalGame != null);
        Button forfeitButton = goMainActivity.findViewById(R.id.forfeitButton);
        assertTrue(forfeitButton != null);

        // Get the players
        GamePlayer[] gamePlayers = goLocalGame.getPlayers();

        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new MyNameIsAction(gamePlayer, gamePlayer.getClass().toString()));
        }

        // Send players to the game
        for(GamePlayer gamePlayer: gamePlayers){
            goLocalGame.sendAction(new ReadyAction(gamePlayer));
        }

        // have the players make moves
        GamePlayer player1 = gamePlayers[0];
        GamePlayer player2 = gamePlayers[1];

        goLocalGame.sendAction(new GoForfeitAction(player1));

        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();

        boolean didForfeit = goGameState.forfeit();

        assertEquals("Turn did not forfeit", true, didForfeit);
    }

    /**
     * Tests that a turn can be skipped successfully
     *
     * @author Jude Gabriel
     */
    @Test
    public void testSkipTurn(){
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //Initialize a local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        View skipButton = goMainActivity.findViewById(R.id.skipTurnButton);
        assertTrue(goLocalGame != null);
        assertTrue(skipButton != null);

        //Get the players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new MyNameIsAction(gamePlayer, gamePlayer.getClass().toString()));
        }

        //Send the names of the players to the game
        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new ReadyAction(gamePlayer));
        }

        /* Start making moves */
        GamePlayer player1 = gamePlayers[0];
        GamePlayer player2 = gamePlayers[1];

        //Check if two moves in a row is possible
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 1));
        goLocalGame.sendAction(new GoMoveAction(player2, 1, 2));
        goLocalGame.sendAction(new GoSkipTurnAction(player1));

        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();

        int thePlayer = goGameState.getPlayer();
        assertEquals("Turn did not skip", 1, thePlayer);
    }


    /**
     * Test that a game over happens correctly
     *
     * STATUS: PASSED
     *
     * @author Brynn Harrington
     */
    @Test
    public void testGameOver(){
        /* CHECK AT THE BEGINNING OF A GAME */
        // initialize a game state
        GoGameState boardEmpty = new GoGameState();

        // have each player skip their turn
        for (int i = 0; i < 2; i++) boardEmpty.skipTurn();

        // verify the game is over
        assertTrue(boardEmpty.isGameOver());

        /* CHECK IN THE MIDDLE OF A GAME */
        // initialize a game state
        GoGameState boardMid = new GoGameState();

        // place moves onto the board
        for (int row = 0; row < boardMid.getBoardSize(); row++) {
            for (int col = 1; col < boardMid.getBoardSize() - 1; col++) {
                // place stones on the board
                if (row % 4 == 0)
                    boardMid.getGameBoard()[row][col].setStoneColor(Stone.StoneColor.WHITE);
                if (col % 3 == 0) boardMid.playerMove(row, col);
                    boardMid.getGameBoard()[row][col].setStoneColor(Stone.StoneColor.BLACK);
            }
        }

        // have each player skip their turn
        for (int i = 0; i < 2; i++) boardEmpty.skipTurn();

        // verify the game is over
        assertTrue(boardEmpty.isGameOver());

        /* CHECK THE END OF A GAME */
        // initialize a game state
        GoGameState boardEnd = new GoGameState();

        // place moves onto the board
        for (int row = 0; row < boardEnd.getBoardSize(); row++) {
            for (int col = 0; col < boardEnd.getBoardSize(); col++) {
                // place stones on the board
                if (row % 2 == 0)
                    boardMid.getGameBoard()[row][col].setStoneColor(Stone.StoneColor.WHITE);
                if (col % 2 == 1)
                    boardMid.getGameBoard()[row][col].setStoneColor(Stone.StoneColor.BLACK);
            }
        }

        // have each player skip their turn
        for (int i = 0; i < 2; i++) boardEmpty.skipTurn();

        // verify the game is over
        assertTrue(boardEmpty.isGameOver());
    }


    /**
     * Tests that a handicap gets placed correctly
     *
     * @author Mia Anderson
     */
    @Test
    public void testHandicap(){
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //new local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        View handicapButton = goMainActivity.findViewById(R.id.handicapButton);
        assertTrue(goLocalGame != null);
        assertTrue(handicapButton != null);

        //get players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new MyNameIsAction(gamePlayer, gamePlayer.getClass().toString()));
        }

        //Send the names of the players to the game
        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new ReadyAction(gamePlayer));
        }

        // create player 1 and 2
        GamePlayer player1 = gamePlayers[0];
        GamePlayer player2 = gamePlayers[1];

        //send handicap button action to local game
        // both players have to agree to handicap
        goLocalGame.sendAction(new GoHandicapAction(player1));
        goLocalGame.sendAction(new GoHandicapAction(player2));

        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();

        assertEquals(Stone.StoneColor.WHITE, goGameState.getGameBoard()[2][2].getStoneColor());
        assertEquals(Stone.StoneColor.WHITE, goGameState.getGameBoard()[6][6].getStoneColor());
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
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //new local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();
        assertTrue(goLocalGame != null);

        //get players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new MyNameIsAction(gamePlayer, gamePlayer.getClass().toString()));
        }

        //Send the names of the players to the game
        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new ReadyAction(gamePlayer));
        }


        //Set up the board and make the two successive captures
        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();
        goGameState.testRepeatedPosition();
        goGameState.playerMove(2, 1);
        goGameState.playerMove(0, 2);
        goGameState.playerMove(1, 2);
        goGameState.playerMove(1, 1);


        //Generate the expected output
        GoGameState gsTest = new GoGameState();
        gsTest.testRepeatedPosition();
        gsTest.getGameBoard()[2][1].setStoneColor(Stone.StoneColor.BLACK);
        gsTest.getGameBoard()[1][2].setStoneColor(Stone.StoneColor.BLACK);
        gsTest.getGameBoard()[0][2].setStoneColor(Stone.StoneColor.WHITE);
        gsTest.getGameBoard()[1][1].setStoneColor(Stone.StoneColor.NONE);
        gsTest.getGameBoard()[2][1].setStoneColor(Stone.StoneColor.BLACK);



        assertTrue("Game states were not equal", goGameState.equals(gsTest));

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
        assertTrue("Equals method lists game states as not equal", goGameState.equals(equalsState));
    }


    /**
     *  Tests that states in progress are equal
     *
     * TODO: Write method
     * TODO: Verify that it works
     */
    @Test
    public void test_equals_state_inProgress(){
        // Create two game states
        GoGameState goGameState = new GoGameState();
        goGameState.getGameBoard()[1][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[2][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[3][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[1][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[2][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[3][3].setStoneColor(Stone.StoneColor.BLACK);
        GoGameState goGameStateOther = new GoGameState();
        goGameStateOther.getGameBoard()[1][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameStateOther.getGameBoard()[2][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameStateOther.getGameBoard()[3][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameStateOther.getGameBoard()[1][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameStateOther.getGameBoard()[2][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameStateOther.getGameBoard()[3][3].setStoneColor(Stone.StoneColor.BLACK);
        assertTrue("Equals method did not agree states were equal", goGameState.equals(goGameStateOther));



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
