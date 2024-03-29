package com.example.gogame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;
import com.example.gogame.GameFramework.Game;
import com.example.gogame.GameFramework.actionMessage.MyNameIsAction;
import com.example.gogame.GameFramework.actionMessage.ReadyAction;
import com.example.gogame.GameFramework.infoMessage.GameState;
import com.example.gogame.GameFramework.players.GameComputerPlayer;
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
import com.example.gogame.GoGame.players.GoSmartComputerPlayer;

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
        //Create the main activity
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

        //Get the main activity and start the game
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //Get the created game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        //Make sure the game loads properly
        assertTrue(goLocalGame != null);
        assertTrue(goLocalGame.getGameState() != null);
        assertTrue(((GoGameState) goLocalGame.getGameState()).getGameBoard() != null);
        assertNotNull(goLocalGame);

        //Get the players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Initialize the player names
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

        //Set expected result
        GoGameState gs = (GoGameState) goLocalGame.getGameState();
        GoGameState goGameState = new GoGameState();
        Stone[][] gBoard = goGameState.getGameBoard();
        gBoard[0][0].setStoneColor(Stone.StoneColor.BLACK);

        //Testing that two moves in a row wasn't possible
        assertTrue(gBoard[0][0].getStoneColor() == gs.getGameBoard()[0][0].getStoneColor());

        //Can we place a stone in a place that already has a stone
        goLocalGame.sendAction(new GoMoveAction(player2, 0, 0));

        //Make sure nothing changed
        assertTrue("Game states were equal", goLocalGame.getGameState().equals(goGameState));


        //Make sure turns do work
        goLocalGame.sendAction(new GoMoveAction(player2, 0, 1));
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 1));

        //Change the actual board to the expected output
        gBoard[0][1].setStoneColor(Stone.StoneColor.WHITE);
        gBoard[1][1].setStoneColor(Stone.StoneColor.BLACK);

        //Check if the GameStates are equal
        assertTrue("Game states were not equal",
                goLocalGame.getGameState().equals(goGameState));

        //Get to a game over
        goLocalGame.sendAction(new GoSkipTurnAction(player2));
        goLocalGame.sendAction(new GoSkipTurnAction(player1));

        //Set the GameState to being over
        goGameState.setGameOver(true);

        //Check if the GameStates are equal
        assertTrue("Game states were not equal",
                goLocalGame.getGameState().equals(goGameState));

        //Player 1 should win
        assertEquals("Player 1 did not win", 1, goLocalGame.whoWon());

        //Check if you can move after game over
        goLocalGame.sendAction(new GoMoveAction(player1, 2, 2));

        //Check if the GameStates are equal
        assertTrue("Game states were not equal",
                goLocalGame.getGameState().equals(goGameState));
    }


    /**
     * Tests the copy constructor for an empty constructor
     *
     * @author Jude Gabriel
     */
    @Test
    public void Test_CopyConstructor_Empty(){
        //Get the view and click to start
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //Get the created game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        //Make sure the game loaded
        assertTrue(goLocalGame != null);

        //Get the players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Initialize the players
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

        //Create constructor and copy constructor
        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();
        GoGameState copyState = new GoGameState(goGameState);

        //Check if they are equal
        assertTrue(goGameState.testCopyConstructor(copyState));
    }


    /**
     * Tests the copy constructor for a partially full constructor
     *
     * @author Jude Gabriel
     */
    @Test
    public void Test_CopyConstructor_MidGame(){
        //Get the main activity and click to start
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //Get the created game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        //Make sure the game loads correctly
        assertTrue(goLocalGame != null);

        //Get the players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Initialize the player
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

        //Continue making different types of moves
        goLocalGame.sendAction(new GoHandicapAction(player1));
        goLocalGame.sendAction(new GoHandicapAction(player2));
        goLocalGame.sendAction(new GoMoveAction(player1, 0, 0));
        goLocalGame.sendAction(new GoMoveAction(player2, 1, 1));
        goLocalGame.sendAction(new GoSkipTurnAction(player1));
        goLocalGame.sendAction(new GoMoveAction(player2, 1, 4));
        goLocalGame.sendAction(new GoMoveAction(player1, 5, 4));
        goLocalGame.sendAction(new GoMoveAction(player2, 3, 4));

        //Get the gamestate and make a copy
        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();
        GoGameState copyState = new GoGameState(goGameState);

        //copyState.setTime(15);  //Used to test when false!
        boolean isTrue = goGameState.testCopyConstructor(copyState);

        //Assert that the gamestates are equal
        assertEquals("Constructors are equal", true, isTrue);
    }

    /**
     * Tests the copy constructor for a finished game
     *
     * @author Jude Gabriel
     */
    @Test
    public void Test_CopyConstructor_GameOver(){
        //Get the main activity and click to start
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //Get the created game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        //Make sure the game loads correctly
        assertTrue(goLocalGame != null);

        //Get the players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Initialize the players
        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new MyNameIsAction(gamePlayer,
                    gamePlayer.getClass().toString()));
        }

        //Send the names of the players to the game
        for(GamePlayer gamePlayer : gamePlayers){
            goLocalGame.sendAction(new ReadyAction(gamePlayer));
        }

        /* Assign the players */
        GamePlayer player1 = gamePlayers[0];
        GamePlayer player2 = gamePlayers[1];

        /* Make a bunch of moves */
        goLocalGame.sendAction(new GoHandicapAction(player1));
        goLocalGame.sendAction(new GoHandicapAction(player2));
        goLocalGame.sendAction(new GoMoveAction(player1, 0, 0));
        goLocalGame.sendAction(new GoMoveAction(player2, 1, 1));
        goLocalGame.sendAction(new GoSkipTurnAction(player1));
        goLocalGame.sendAction(new GoMoveAction(player2, 1, 4));
        goLocalGame.sendAction(new GoMoveAction(player1, 5, 4));
        goLocalGame.sendAction(new GoMoveAction(player2, 3, 4));
        goLocalGame.sendAction(new GoMoveAction(player1, 0, 1));
        goLocalGame.sendAction(new GoSkipTurnAction(player2));
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 0));
        goLocalGame.sendAction(new GoMoveAction(player2, 3, 6));
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 2));
        goLocalGame.sendAction(new GoMoveAction(player2, 3, 7));
        goLocalGame.sendAction(new GoMoveAction(player1, 2, 1));
        goLocalGame.sendAction(new GoMoveAction(player2, 3, 8));
        goLocalGame.sendAction(new GoMoveAction(player1, 6, 1));
        goLocalGame.sendAction(new GoMoveAction(player2, 6, 2));
        goLocalGame.sendAction(new GoMoveAction(player1, 6, 4));
        goLocalGame.sendAction(new GoMoveAction(player2, 6, 4));

        /* Both player skip to end the game */
        goLocalGame.sendAction(new GoSkipTurnAction(player1));
        goLocalGame.sendAction(new GoSkipTurnAction(player2));

        /* Get the gamestate and copy it */
        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();
        GoGameState copyState = new GoGameState(goGameState);

        //copyState.setTime(15);  //Used to test when false!

        //Check if the copies are equal
        boolean isTrue = goGameState.testCopyConstructor(copyState);

        //Assert message passes
        assertEquals("Constructors are equal", true, isTrue);
    }


    /**
     * Tests if an empty constructor gets copied correctly
     *
     * @author Jude Gabriel
     */
    @Test
    public void test_CopyArray_Empty(){
        //Copy and empty array
        GoGameState goGameState = new GoGameState();
        GoGameState copyState = new GoGameState(goGameState);

        //Test if they are equal
        assertTrue("Copy constructor did not produce equal state",
                goGameState.equals(copyState));
    }


    /**
     * Tests if a game in progress gets copied correctly
     *
     * @author Jude Gabriel
     */
    @Test
    public void test_CopyArray_InProgress(){
        //Create the gamestate
        GoGameState goGameState = new GoGameState();

        //Place a couple stones
        goGameState.getGameBoard()[0][0].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[3][4].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[8][8].setStoneColor(Stone.StoneColor.WHITE);

        //Create a copy of the gamestate
        GoGameState copyState = new GoGameState(goGameState);

        //Test if they are equal
        assertTrue("Copy constructor did not produce equal state",
                goGameState.equals(copyState));
    }


    /**
     * Tests if a full board copies over correctly
     *
     *
     * @author Jude Gabriel
     * @author Natalie Tashchuk
     */
    @Test
    public void test_CopyArray_Full(){
        //Create a new gamestate
        GoGameState goGameState = new GoGameState();

        //Populate the entire board with stones
        for (int r = 0; r < goGameState.getBoardSize(); r++){
            for (int c = 0; c < goGameState.getBoardSize(); c++){
                if (c % 2 != 0){
                    goGameState.getGameBoard()[r][c].setStoneColor(Stone.StoneColor.BLACK);
                }
                else {
                    goGameState.getGameBoard()[r][c].setStoneColor(Stone.StoneColor.WHITE);
                }
            }
        }

        //Create a copy
        GoGameState copyState = new GoGameState(goGameState);

        //Check if the copies are equal
        assertTrue("Copy constructor produced equal state", goGameState.equals(copyState));
    }

    /**
     * Tests that placing a stone works correctly
     *
     * @author Natalie Tashchuk
     */
    @Test
    public void testPlaceStone(){

        // get the current view
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        // create a local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();
        assertNotNull(goLocalGame);

        //create an emptyBoard for comparison
        GoGameState emptyBoard = new GoGameState();

        //get players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Initialize the players
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

        //test that board is equal to empty board before move is made
        assertEquals(emptyBoard, goLocalGame.getGameState());

        //make a move
        goLocalGame.sendAction(new GoMoveAction(player1, 0, 0));

        // get the current game state
        GoGameState board = (GoGameState) goLocalGame.getGameState();

        //test that board is no longer empty
        assertNotEquals(board, emptyBoard);

        //test stone is set to right color
        assertSame(board.getGameBoard()[0][0].getStoneColor(), Stone.StoneColor.BLACK);

    }

    /**
     * Tests that captures are successful on corners
     *
     *
     * @author Natalie Tashchuk
     */
    @Test
    public void testCaptureOnCorner(){
        // get the current view
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        // create a local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();
        assertNotNull(goLocalGame);

        //get players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Initialize the players
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

        //set up board for black to make a capture in the top right corner
        goLocalGame.sendAction(new GoMoveAction(player1, 7,0));
        goLocalGame.sendAction(new GoMoveAction(player2, 8, 0));
        goLocalGame.sendAction(new GoMoveAction(player1, 7, 1));
        goLocalGame.sendAction(new GoMoveAction(player2, 0, 8));  //for next capture

        GoGameState board = (GoGameState) goMainActivity.getGame().getGameState();  //get ggs
        int blackScore = board.calculateScore(Stone.StoneColor.BLACK,
                Stone.StoneColor.WHITE);  //should be 2

        //place stone that completes the capture
        goLocalGame.sendAction(new GoMoveAction(player1, 8, 1));  //bs should now be 4, ws 1

        // get the current game state
         GoGameState updatedBoard = (GoGameState) goMainActivity.getGame().getGameState();

         assertNotEquals(blackScore,updatedBoard.calculateScore(Stone.StoneColor.BLACK,
                 Stone.StoneColor.WHITE));
         assertEquals(updatedBoard.calculateScore(Stone.StoneColor.BLACK,
                 Stone.StoneColor.WHITE), blackScore+2);
         assertEquals(1, updatedBoard.calculateScore(Stone.StoneColor.WHITE,
                 Stone.StoneColor.BLACK));

        //set up board for black to make a capture in the bottom left corner
        goLocalGame.sendAction(new GoMoveAction(player2, 5,5));  //random move for white, ws = 2
        goLocalGame.sendAction(new GoMoveAction(player1, 0,7));  //bs = 5
        goLocalGame.sendAction(new GoMoveAction(player2, 0, 3));  //ws = 3
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 7));  //bs = 6
        goLocalGame.sendAction(new GoMoveAction(player2, 2, 3));  //ws = 4
        goLocalGame.sendAction((new GoMoveAction(player1, 1,8)));  //capture, bs = 8, ws = 3

        assertEquals(8, updatedBoard.calculateScore(Stone.StoneColor.BLACK,
                Stone.StoneColor.WHITE));
        assertEquals(3,updatedBoard.calculateScore(Stone.StoneColor.WHITE,
                Stone.StoneColor.BLACK));
    }



    /**
     * Tests that captures are successful
     *
     * add score tests, empty test
     *
     * STATUS - PASSED
     *
     * @author Brynn Harrington
     */
    @Test
    public void testCapture(){
        // get the current view
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        // create a local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();
        assertNotNull(goLocalGame);

        // get the players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();
        GamePlayer player1 = gamePlayers[0];
        GamePlayer player2 = gamePlayers[1];

        // make moves on the board so a capture for black is made
        goLocalGame.sendAction(new GoMoveAction(player1, 0, 0));
        goLocalGame.sendAction(new GoMoveAction(player2, 1, 1));
        goLocalGame.sendAction(new GoMoveAction(player1, 0, 1));
        goLocalGame.sendAction(new GoMoveAction(player2, 9, 6));
        goLocalGame.sendAction(new GoMoveAction(player1, 0, 2));
        goLocalGame.sendAction(new GoMoveAction(player2, 9, 5));
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 0));
        goLocalGame.sendAction(new GoMoveAction(player2, 9, 4));
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 2));
        goLocalGame.sendAction(new GoMoveAction(player2, 9, 3));
        goLocalGame.sendAction(new GoMoveAction(player1, 2, 0));
        goLocalGame.sendAction(new GoMoveAction(player2, 9, 2));
        goLocalGame.sendAction(new GoMoveAction(player1, 2, 1));
        goLocalGame.sendAction(new GoMoveAction(player2, 9, 0));
        goLocalGame.sendAction(new GoMoveAction(player1, 2, 2));
        goLocalGame.sendAction(new GoMoveAction(player2, 8, 6));

        // get the current game state
        GoGameState board = (GoGameState) goMainActivity.getGame().getGameState();

        // verify black's score went up by one and place on board empty
        assertSame(board.getGameBoard()[1][1].getStoneColor(), Stone.StoneColor.NONE);
    }


    /**
     * Tests that a forfeit works correctly
     *
     * @author Mia Anderson
     */
    @Test
    public void testForfeit(){
        //Get the main activity and click to start it
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //create a local game and make sure it exists
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();
        assertTrue(goLocalGame != null);

        //Create the button and make sure it exists
        Button forfeitButton = goMainActivity.findViewById(R.id.forfeitButton);
        assertTrue(forfeitButton != null);
        assertNotNull(goLocalGame);

        // Get the players
        GamePlayer[] gamePlayers = goLocalGame.getPlayers();

        //Initialize the players
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

        //Send the forfeit action
        goLocalGame.sendAction(new GoForfeitAction(player1));

        //Get the gamestate
        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();

        //Get the boolean forfeit value
        boolean didForfeit = goGameState.forfeit();

        //Test the output
        assertEquals("Turn did not forfeit", true, didForfeit);
    }

    /**
     * Tests that a turn can be skipped successfully
     *
     * @author Jude Gabriel
     */
    @Test
    public void testSkipTurn(){
        //Get the main activity and click to start
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //Initialize a local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        //Get the button
        View skipButton = goMainActivity.findViewById(R.id.skipTurnButton);

        //Make sure everything exists
        assertTrue(goLocalGame != null);
        assertTrue(skipButton != null);
        assertTrue(goLocalGame.getGameState() != null);

        //Get the players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Initialize the players
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
        int thePlayer = ((GoGameState)goLocalGame.getGameState()).getPlayer();
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 1));
        thePlayer = ((GoGameState)goLocalGame.getGameState()).getPlayer();
        goLocalGame.sendAction(new GoMoveAction(player2, 1, 2));
        thePlayer = ((GoGameState)goLocalGame.getGameState()).getPlayer();
        goLocalGame.sendAction(new GoSkipTurnAction(player1));


        //Assert it is player 2's turn
        thePlayer = ((GoGameState)goLocalGame.getGameState()).getPlayer();
        assertTrue(thePlayer == 1);
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
        //Get the main activity and click to start
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //new local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();

        //Get the button
        View handicapButton = goMainActivity.findViewById(R.id.handicapButton);

        //Make sure everything exists
        assertTrue(goLocalGame != null);
        assertTrue(handicapButton != null);
        assertTrue(((GoGameState) goLocalGame.getGameState()).getGameBoard() != null);

        //get players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Initialize the players
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
        goLocalGame.sendAction(new GoMoveAction(player1, 1, 1));

        //Get the gamestate
        GoGameState goGameState = (GoGameState) goLocalGame.getGameState();

        //Test the correct stone colors
        assertEquals(Stone.StoneColor.WHITE, goGameState.getGameBoard()[2][2].getStoneColor());
        assertEquals(Stone.StoneColor.WHITE, goGameState.getGameBoard()[6][6].getStoneColor());
    }


    /**
     * Tests that a repeated position does not work
     *
     * @author Jude Gabriel
     */
    @Test
    public void testRepeatedPosition(){
        //Get the main activity and click to start
        View view = goMainActivity.findViewById(R.id.playGameButton);
        goMainActivity.onClick(view);

        //new local game
        GoLocalGame goLocalGame = (GoLocalGame) goMainActivity.getGame();
        assertTrue(goLocalGame != null);

        //get players
        GamePlayer[] gamePlayers= goLocalGame.getPlayers();

        //Initialize the players
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

        //Compare the outputs
        assertTrue("Game states were not equal", goGameState.equals(gsTest));
    }


    /**
     * Tests that empty states are equal
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
     * @author Mia Anderson
     */
    @Test
    public void test_equals_state_inProgress(){
        // Create a gamestate
        GoGameState goGameState = new GoGameState();

        //Populate the gamestate with stones
        goGameState.getGameBoard()[1][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[2][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[3][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameState.getGameBoard()[1][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[2][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameState.getGameBoard()[3][3].setStoneColor(Stone.StoneColor.BLACK);

        //Create a second gamestate
        GoGameState goGameStateOther = new GoGameState();

        //Populate the second gamestate with the same configuration
        goGameStateOther.getGameBoard()[1][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameStateOther.getGameBoard()[2][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameStateOther.getGameBoard()[3][2].setStoneColor(Stone.StoneColor.WHITE);
        goGameStateOther.getGameBoard()[1][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameStateOther.getGameBoard()[2][3].setStoneColor(Stone.StoneColor.BLACK);
        goGameStateOther.getGameBoard()[3][3].setStoneColor(Stone.StoneColor.BLACK);

        //Check that the gamestates are equal
        assertTrue("Equals method did not agree states were equal",
                goGameState.equals(goGameStateOther));

    }
}
