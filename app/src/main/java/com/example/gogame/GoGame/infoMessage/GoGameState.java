/**
 * GoGameState.java
 *
 * @author Natalie Tashchuk
 * @author Mia Anderson
 * @author Brynn Harrington
 * @author Jude Gabriel
 */


package com.example.gogame.GoGame.infoMessage;


import com.example.gogame.GameFramework.infoMessage.GameState;
import com.example.gogame.GoGame.infoMessage.moveData.Move;
import com.example.gogame.GoGame.infoMessage.moveData.MoveResult;
import com.example.gogame.GoGame.infoMessage.moveData.MoveType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GoGameState extends GameState implements Serializable {

    /* Private Instance Variables */
    private final float userXClick;         //The x coordinate the user clicks
    private final float userYClick;         //The y coordinate the user clicks
    private final int boardSize;            //The dimensions of the this
    private boolean isPlayer1;              //boolean value for which player's turn it is
    private Stone[][] gameBoard;            //Stones array for locations on the this
    private int player1Score;               //Stores Player 1's score
    private int player2Score;               //Stores Player 2's score
    private boolean gameOver;               //Tracks whether the game is over
    private Stone[][] stoneCopiesFirst;     //Stores the this from two moves ago
    private Stone[][] stoneCopiesSecond;    //Stores the this from one move ago
    private int totalMoves;                 //Total number of moves made in game
    private int numSkips;                   //Tracks whether two consecutive skips
    private boolean p1Handicap;             //Tracks if Player 1 agrees to handicap
    private boolean p2Handicap;             //Tracks if Player 2 agrees to handicap
    private boolean player1Forfeit;         //Tracks if Player 1 forfeits
    private boolean player2Forfeit;         //Tracks if Player 2 forfeits
    private int time;                       //Tracks the time of the game
    private final int[] mostRecentMove;     //Tracks the most recent move made in the game


    //Network play ID Tag
    private static final long serialVersionUID = 7552321013488624386L;

    /**
     * GoGameState
     * Constructor for the GoGameStateClass
     *
     * @author Jude Gabriel
     * @author Natalie Tashchuk
     * @author Mia Anderson
     * @author Brynn Harrington
     */
    public GoGameState() {
        //Initialize the this size and gameBoard array
        boardSize = 9;
        gameBoard = initializeArray();

        //Set isPlayer1 to true so that player 1 starts the game
        isPlayer1 = true;

        //Initialize the float values of the user's click
        userXClick = -1;
        userYClick = -1;

        //Initialize the players scores and total number of moves
        player1Score = 0;
        player2Score = 0;
        totalMoves = 0;

        //Initialize game over to false and number of skips to zero
        gameOver = false;
        numSkips = 0;

        //Initialize the arrays that store former this positions
        stoneCopiesFirst = initializeArray();
        stoneCopiesSecond = initializeArray();

        //Initialize handicap
        p1Handicap = false;
        p2Handicap = false;

        //Initialize forfeits
        player1Forfeit = false;
        player2Forfeit = false;

        //Initialize the time
        time = 0;

        //Initialize the most recent move array
        mostRecentMove = new int[2];
        mostRecentMove[0] = -1;
        mostRecentMove[1] = -1;
    }


    /**
     * GoGameState
     * Copy Constructor for the Go GameState
     * Performs a deep copy of the Go GameState to counter
     * possible cheating in the game.
     *
     * @param gs - the game state to copy
     * @author Jude Gabriel
     * @author Natalie Tashchuk
     * @author Mia Anderson
     * @author Brynn Harrington
     */
    public GoGameState(GoGameState gs) {
        //Initialize the instance variables to the current game state
        this.boardSize = gs.boardSize;
        this.userXClick = gs.userXClick;
        this.userYClick = gs.userYClick;
        this.gameBoard = deepCopyArray(gs.gameBoard);
        this.player1Score = gs.player1Score;
        this.player2Score = gs.player2Score;
        this.gameOver = gs.gameOver;
        this.numSkips = gs.numSkips;
        this.isPlayer1 = gs.isPlayer1;
        this.totalMoves = gs.totalMoves;
        this.p1Handicap = gs.p1Handicap;
        this.p2Handicap = gs.p2Handicap;
        this.player1Forfeit = gs.player1Forfeit;
        this.player2Forfeit = gs.player2Forfeit;
        this.time = gs.time;
        this.mostRecentMove = new int[2];
        this.mostRecentMove[0] = gs.mostRecentMove[0];
        this.mostRecentMove[1] = gs.mostRecentMove[1];
        this.stoneCopiesFirst = deepCopyArray(gs.stoneCopiesFirst);
        this.stoneCopiesSecond = deepCopyArray(gs.stoneCopiesSecond);
        super.currentSetupTurn = gs.currentSetupTurn;
        super.numSetupTurns = gs.numSetupTurns;

    }


    /**
     * initializeArray
     * This method initializes the array of stones
     *
     * @author Jude Gabriel
     * <p>
     * NOTE: This method works as expected
     */
    public Stone[][] initializeArray() {
        //Create a temporary array of stones
        Stone[][] tempBoard = new Stone[boardSize][boardSize];

        //Initialize the stones to a certain color
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tempBoard[i][j] = new Stone((j * 83) + 46, (i * 81) + 48);
            }
        }

        //Return the array of stones
        return tempBoard;
    }


    /**
     * playerMove
     * Calculate the player move and then re-change the color of the stones
     *
     * @param x x location of the user or AI click
     * @param y y location of the user or AI click
     * @return true if it is a valid player move, false otherwise
     * @author Jude Gabriel
     */
    public boolean playerMove(int x, int y) {
        //Find the liberty the user clicked on
        int[] indexVal = findStone(x, y);
        int iIndex = x;
        int jIndex = y;

        //Determine the current player's color
        Stone.StoneColor currStoneColor;
        Stone.StoneColor oppStoneColor;
        if (isPlayer1) {
            currStoneColor = Stone.StoneColor.BLACK;
            oppStoneColor = Stone.StoneColor.WHITE;
        } else {
            currStoneColor = Stone.StoneColor.WHITE;
            oppStoneColor = Stone.StoneColor.BLACK;
        }

        //If total moves is 1, copy to track this from two moves ago
        if (totalMoves == 1) {
            stoneCopiesFirst = deepCopyArray(gameBoard);
        }

        //If total moves is 2, copy to track this from one move ago
        if (totalMoves == 2) {
            stoneCopiesSecond = deepCopyArray(gameBoard);
        }

        //Check if the move is valid
        boolean validMove = isValidLocation(iIndex, jIndex);

        //Place stone if valid
        if (validMove) {
            //Update the liberty's color based on the current player's move
            gameBoard[iIndex][jIndex].setStoneColor(currStoneColor);

            //Determine if capture is possible
            iterateAndCheckCapture(iIndex, jIndex);

            //Capture if possible
            commenceCapture(oppStoneColor);

            //Reset the capture and player
            resetCapture();
            isPlayer1 = !isPlayer1;

            //Reset the score in case of captures and recalculate score
            player1Score = 0;
            player1Score += calculateScore(Stone.StoneColor.BLACK, Stone.StoneColor.WHITE);

            player2Score = 0;
            player2Score += calculateScore(Stone.StoneColor.WHITE, Stone.StoneColor.BLACK);

            //Increment number of moves
            totalMoves++;

            //Reset the number of skipped turns to zero since valid move played
            numSkips = 0;

            //Store the most recent move
            mostRecentMove[0] = x;
            mostRecentMove[1] = y;

            //Return true since valid move was made by player
            return true;
        }

        //Otherwise return false
        return false;
    }


    /**
     * findStone
     * Finds which index the user clicked on in the stones array
     *
     * @param x the x location of the user click
     * @param y the y location of the user click
     * @return an integer array containing the indices
     * @author Jude Gabriel
     */
    public int[] findStone(float x, float y) {
        //Initialize index values as -1 so we can error check
        int iIndex = -1;
        int jIndex = -1;

        //Since the radius of the stone is 25 we wanna check double the surroundings
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if ((x < gameBoard[i][j].getxRight()) && (x > gameBoard[i][j].getxLeft())) {
                    if ((y > gameBoard[i][j].getyTop()) && (y < gameBoard[i][j].getyBottom())) {
                        iIndex = i;
                        jIndex = j;
                    }
                }
            }
        }

        //Create an array to store the index values
        int[] indexArray = new int[2];
        indexArray[0] = iIndex;
        indexArray[1] = jIndex;

        //Return the array with the index values of the stone
        return indexArray;
    }


    /**
     * iterateAndCheckCapture
     * A helper function that iterates through the this and checks
     * whether a capture is possible in the given place on the this.
     *
     * @param x the x-coordinate of where the user clicked
     * @param y the y-coordinate of where the user clicked
     * @author Brynn Harrington
     */
    public void iterateAndCheckCapture(int x, int y) {
        //Initialize a variable to store the stone color of current player
        //and the opponent's stone color
        Stone.StoneColor currStoneColor, oppStoneColor;
        if (isPlayer1) {
            currStoneColor = Stone.StoneColor.BLACK;
            oppStoneColor = Stone.StoneColor.WHITE;
        } else {
            currStoneColor = Stone.StoneColor.WHITE;
            oppStoneColor = Stone.StoneColor.BLACK;
        }

        //Iterate through the this and determine whether they player can capture
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                //Check if current position is empty
                if (gameBoard[i][j].getStoneColor() == Stone.StoneColor.NONE) {
                    //Verify this position has not been checked for a capture already
                    if (gameBoard[i][j].getCheckedStone() == Stone.CheckedStone.FALSE) {
                        //Check if the player can capture from this position
                        checkCapture(i, j, currStoneColor, oppStoneColor);
                    }
                }
            }
        }
    }


    /**
     * isValidLocation
     * This will check if the user places the stone in a valid position
     *
     * @return true if the user places a stone in a valid location
     * @author Jude Gabriel
     * @author Brynn Harrington
     */
    public boolean isValidLocation(int iIndex, int jIndex) {
        //Check if the user clicked on a liberty
        if (iIndex == -1 || jIndex == -1) return false;

        //Check if a stone is already there
        if (gameBoard[iIndex][jIndex].getStoneColor() != Stone.StoneColor.NONE)
            return false;

        //Initialize a variable to track if able to capture
        boolean capCheck = false;

        //Initialize a variable to track if repeated position
        boolean repeated = false;

        //Initialize a variable to track current player's stone color
        //and the opponent's stone color
        Stone.StoneColor currStoneColor;
        Stone.StoneColor oppStoneColor;

        //Determine the current player's stone color and
        //opponent's stone color
        if (isPlayer1) {
            //If player one, current player's color is black
            currStoneColor = Stone.StoneColor.BLACK;
            //If player one, opponent's color is white
            oppStoneColor = Stone.StoneColor.WHITE;
        } else {
            //If player one, current player's color is black
            currStoneColor = Stone.StoneColor.WHITE;
            //If player one, opponent's color is white
            oppStoneColor = Stone.StoneColor.BLACK;
        }

        //Create a deep copy of the current game this
        Stone[][] copyArr = deepCopyArray(gameBoard);

        //Set the stone color to the current player
        gameBoard[iIndex][jIndex].setStoneColor(currStoneColor);

        //Verify the player will not capture themselves
        capCheck = selfCapture(iIndex, jIndex, oppStoneColor, currStoneColor);

        //Verify the this is not a repeated position
        if(totalMoves >= 2) {
            repeated = checkRepeatedPosition(iIndex, jIndex);
        }

        //Set the game this to the deep copy with new position
        gameBoard = deepCopyArray(copyArr);

        //If self capture, return false
        if (capCheck) return false;

        //If it is repeated position return false
        if (repeated) return false;

        //Reset the capture
        resetCapture();

        //If all above cases do not return false then it is a valid move
        //and return true
        return true;
    }


    /**
     * selfCapture
     * Checks if a stone can capture itself
     *
     * @param x        i index of stone
     * @param y        j index of stone
     * @param checkCol Capturing stone color
     * @param capCol   Captured stone color
     * @return true if the stone captures itself
     * @author Jude Gabriel
     * @author Brynn Harrington
     */
    public boolean selfCapture(int x, int y, Stone.StoneColor checkCol, Stone.StoneColor capCol) {
        //Iterate through the this
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                //Verify the stone is empty
                if (gameBoard[i][j].getStoneColor() == Stone.StoneColor.NONE) {
                    //Verify the stone cannot be captured
                    if (gameBoard[i][j].getCheckedStone() == Stone.CheckedStone.FALSE) {
                        checkCapture(i, j, capCol, checkCol);
                    }
                }
            }
        }

        //Check if the person placing a stone can capture first
        commenceCapture(checkCol);
        resetCapture();

        //Iterate through the this
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                //Verify the stone is empty
                if (gameBoard[i][j].getStoneColor() == Stone.StoneColor.NONE) {
                    //Verify the stone cannot be captured
                    if (gameBoard[i][j].getCheckedStone() == Stone.CheckedStone.FALSE) {
                        checkCapture(i, j, checkCol, capCol);
                    }
                }
            }
        }

        //See if the opposing player can capture the person making the move
        commenceCapture(capCol);
        resetCapture();

        //Return whether or not the stone was captured
        return gameBoard[x][y].getStoneColor() != capCol;
    }


    /**
     * checkCapture
     * Checks for captured stones
     *
     * @param i        i index of the stone
     * @param j        j index of the stone
     * @param checkCol Color of capturing stones
     * @param capCol   Color of captured stones
     * @author Jude Gabriel
     * @modified Brynn Harrington
     */
    public void checkCapture(int i, int j, Stone.StoneColor checkCol, Stone.StoneColor capCol) {
        //Verify the location is valid, the stone has not been checked. and the stone color
        //is not what is being checked
        if (i < 0 || j < 0
                || i > gameBoard.length - 1 || j > gameBoard.length - 1
                || gameBoard[i][j].getCheckedStone() == Stone.CheckedStone.TRUE
                || gameBoard[i][j].getStoneColor() == checkCol) {
            return;
        }

        //If the stone has not been checked yet, check if capture possible
        if (gameBoard[i][j].getCheckedStone() == Stone.CheckedStone.FALSE) {
            //Set the checked to true
            gameBoard[i][j].setCheckedStone(Stone.CheckedStone.TRUE);

            //Check around to determine if able to capture
            checkCapture(i + 1, j, checkCol, capCol);
            checkCapture(i - 1, j, checkCol, capCol);
            checkCapture(i, j + 1, checkCol, capCol);
            checkCapture(i, j - 1, checkCol, capCol);
        }
    }


    /**
     * commenceCapture
     * This method finds which stones can be captured and captures them
     *
     * @author Jude Gabriel
     */
    public void commenceCapture(Stone.StoneColor capCol) {
        //Iterate through the this
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                //If the stone hasn't been checked and the color is the capture color,
                //capture the stone
                if (gameBoard[i][j].getCheckedStone() == Stone.CheckedStone.FALSE) {
                    if (gameBoard[i][j].getStoneColor() == capCol) {
                        gameBoard[i][j].setStoneColor(Stone.StoneColor.NONE);
                    }
                }
            }
        }
    }


    /**
     * resetCapture
     * If captures are not possible, reset checked stone values
     *
     * @author Jude Gabriel
     */
    public void resetCapture() {
        //Iterate through the this and reset checked stone to false
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j].setCheckedStone(Stone.CheckedStone.FALSE);
            }
        }
    }


    /**
     * checkRepeatedPosition
     * Checks if the user recreated a previous position on the this
     *
     * @param x - the x-coordinate of where the user clicked
     * @param y - the y-coordinate of where the user clicked
     * @return true if the this position is repeated
     * @author Jude Gabriel
     */
    public boolean checkRepeatedPosition(int x, int y){
        //Set a truth counter to zero
        int count = 0;

        //Create a deep copy of the copy array
        Stone[][] copyArray = deepCopyArray(gameBoard);


        //Determine the current color
        Stone.StoneColor oppStoneColor;
        if (isPlayer1) oppStoneColor = Stone.StoneColor.WHITE;
        else oppStoneColor = Stone.StoneColor.BLACK;

        //Iterate and determine if capture is possible, if so commence capture
        //and reset capture
        iterateAndCheckCapture(x, y);
        commenceCapture(oppStoneColor);
        resetCapture();

        //Check if the boards are equal
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (gameBoard[i][j].getStoneColor() != stoneCopiesFirst[i][j].getStoneColor()) {
                    count++;
                }
            }
        }

        //If they are equal reset the this and return true
        if (count == 0) {
            gameBoard = deepCopyArray(copyArray);
            return true;
        }

        //If they are not equal update the arrays
        //Update the array storing two boards ago to the one this ago
        stoneCopiesFirst = deepCopyArray(stoneCopiesSecond);
        //Update the array storing the past this to the current
        stoneCopiesSecond = deepCopyArray(gameBoard);

        //Update the current this
        gameBoard = deepCopyArray(copyArray);

        //Return false since not repeated position
        return false;
    }


    /**
     * deepCopyArray
     * Creates a copy of the array
     *
     * @param firstArr The array to copy
     * @return the copied array
     * @author Jude Gabriel
     */
    public Stone[][] deepCopyArray(Stone[][] firstArr) {
        //Create a new copy array
        Stone[][] copyArr = new Stone[boardSize][boardSize];

        //Iterate through the current this size and create empty this
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                copyArr[i][j] = new Stone(0, 0);
            }
        }

        //Iterate through the this and perform a deep copy of current this
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (firstArr[i][j] != null) {
                    copyArr[i][j].setRadius(firstArr[i][j].getRadius());
                    copyArr[i][j].setxLeft(firstArr[i][j].getxLeft());
                    copyArr[i][j].setxLocation(firstArr[i][j].getxLocation());
                    copyArr[i][j].setxRight(firstArr[i][j].getxRight());
                    copyArr[i][j].setyBottom(firstArr[i][j].getyBottom());
                    copyArr[i][j].setyTop(firstArr[i][j].getyTop());
                    copyArr[i][j].setyLocation(firstArr[i][j].getyLocation());
                    copyArr[i][j].setStoneColor(firstArr[i][j].getStoneColor());
                    copyArr[i][j].setCheckedStone(firstArr[i][j].getCheckedStone());
                }
            }
        }

        //Return the new array
        return copyArr;
    }


    /**
     * Checks if a winning condition is not possible for a player
     *
     * @return true if a winning condition is not possible
     * @author Jude Gabriel
     */
    public boolean isOver() {
        //Set count to zero
        int count = 0;

        //Iterate through the this and find empty liberties
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if(gameBoard[i][j].getStoneColor() == Stone.StoneColor.NONE){
                    //Check if empty liberties have empty neighbors and return false if so
                    count = isOverRecursive(i, j);
                    if(count >= 2){
                        return false;
                    }

                    //Update count for next liberty
                    count = 0;
                }
            }
        }

        //Return true if there is no set of empty liberties
        return true;
    }

    /**
     * Helper method for isOver Checks surrounding empty liberties
     *
     * @param i x index of liberty
     * @param j y index of liberty
     *
     * @return the number of empty liberties
     * @author Jude Gabriel
     */
    public int isOverRecursive(int i, int j){
        //Set count to 1 since the liberty that called this method is empty
        int count = 1;

        /* Check if there are any empty neighbors */
        if(i > 0){
            if(gameBoard[i - 1][j].getStoneColor() == Stone.StoneColor.NONE){
                count++;
            }
        }
        if(i < gameBoard.length - 1){
            if(gameBoard[i + 1][j].getStoneColor() == Stone.StoneColor.NONE){
                count++;
            }
        }
        if(j > 0){
            if(gameBoard[i][j - 1].getStoneColor() == Stone.StoneColor.NONE){
                count++;
            }
        }
        if(j < gameBoard.length - 1){
            if(gameBoard[i][j + 1].getStoneColor() == Stone.StoneColor.NONE){
                count++;
            }
        }

        //Return the number of empty liberties
        return count;
    }


    /**
     * Getter for boardSize
     *
     * @return size of current this as an int
     * @author Natalie Tashchuk
     */
    public int getBoardSize(){
        return boardSize;
    }


    /**
     * Getter for gameBoard
     *
     * @return current this as an array of Stone Objects
     * @author Natalie Tashchuk
     */
    public Stone[][] getGameBoard(){
        return gameBoard;
    }


    /**
     * Getter for the number of skips
     *
     * @return number of skips made
     * @author Jude Gabriel
     */
    public int getNumSkips(){
        return numSkips;
    }


    /**
     * skipTurn
     * Allows the user to skip a turn by changing the player turn value
     *
     * @return true once the player swap has happened
     * @author Jude Gabriel
     * @author Brynn Harrington
     */
    public boolean skipTurn() {
        //Reset the current player to the opponent
        isPlayer1 = !isPlayer1;

        //Increment number of skips
        numSkips++;

        //If number of skips is two, the game is over
        if (numSkips == 2) gameOver = true;

        //Return true since the player has skipped
        return true;
    }


    /**
     * forfeit
     * Allows the user to forfeit the game
     *
     * @return true if player 1 forfeits and false if player 2 does
     * @author Jude Gabriel
     */
    public boolean forfeit() {
        // set game over to true since forfeited
        gameOver = true;
        if(isPlayer1){
            player1Forfeit = true;
        }
        else{
            player2Forfeit = true;
        }
        return true;
    }


    /**
     * Setter for the total time the game has been played for
     *
     * @param aTime the current game time
     * @author Jude Gabriel
     */
    public void setTime(int aTime){
        time = aTime;
    }


    /**
     * Getter for the total game time
     *
     * @return the game time
     * @author Jude Gabriel
     */
    public int getTime(){
        return time;
    }


    /**
     * calculateScore
     * Calculates the current score
     *
     * @return totalScore - the total score for the player
     * @author Jude Gabriel
     * @author Mia Anderson
     * @author Natalie Taschuck
     * @author Brynn Harrington
     */
    public int calculateScore(Stone.StoneColor colorToScore, Stone.StoneColor otherColor) {
        //Initialize the total score to zero
        int totalScore = 0;

        //Iterate through the this
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                //See if there are empty liberties surrounding
                if (gameBoard[i][j].getStoneColor() == otherColor) {
                    checkCapture(i, j, colorToScore, otherColor);
                }
            }
        }

        //Add to the total score
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                //If empty liberties, add to current score
                if (gameBoard[i][j].getCheckedStone() == Stone.CheckedStone.FALSE) {
                    totalScore++;
                }
            }
        }

        //Reset the capture
        resetCapture();

        //Return the total Score
        return totalScore;
    }


    /** isGameOver
     * Returns the current instance of game over.
     *
     * @author Brynn Harrington
     * @return game over
     */
    public boolean isGameOver() { return gameOver; }


    /** getPlayer
     *
     * Returns the index of the current player
     *
     * @author Brynn Harrington
     * @return current player
     */
    public int getPlayer() {
        // if is player 1, return index 0
        if (isPlayer1) return 0;

        // otherwise return player 2, index 1
        else return 1;
    }

    /** getIsPlayer1
     *
     * Returns whether it is player 1's turn or not
     *
     * @author Brynn Harrington
     * @return isPlayer1
     */
    public boolean getIsPlayer1() { return isPlayer1; }

    /**
     * Getter for player 1's score
     *
     * @return  player 1's score
     *
     * @author Jude Gabriel
     */
    public int getPlayer1Score(){
        return player1Score;
    }


    /**
     * Getter for player 2's turn
     *
     * @return  Player 2's score
     *
     * @author Jude Gabriel
     */
    public int getPlayer2Score(){
        return player2Score;
    }


    /**
     * Setter for gameOver
     *
     * @param isOver
     */
    public void setGameOver(boolean isOver){
        gameOver = isOver;
    }


    /**
     * Getter for player1Forfeit
     *
     * @return  true if the player forfeited
     * @author Jude Gabriel
     */
    public boolean getPlayer1Forfeit(){
        return player1Forfeit;
    }


    /**
     * Getter for the player2Forfeit
     *
     * @return true if the player forfeited
     * @auhor Jude Gabriel
     */
    public boolean getPlayer2Forfeit(){
        return player2Forfeit;
    }


    /**
     * Getter for the total moves in the game
     *
     * @return the total number of moves
     * @author Jude Gabriel
     */
    public int getTotalMoves(){
        return totalMoves;
    }


    /**
     * Getter for the coordinates of the most recent move
     *
     * @return an array with the most recent moves coordinates
     * @author Jude Gabriel
     */
    public int[] getMostRecentMove(){
        return mostRecentMove;
    }


    /**
     * Getter for the player 1 handicap value
     *
     * @return true if player 1 agrees to a handicap
     * @author Jude Gabriel
     */
    public boolean getP1Handicap(){
        return p1Handicap;
    }


    /**
     * Getter for the player 2 handicap value
     *
     * @return true if player 2 agrees to a handicap
     * @author Jude Gabriel
     */
    public boolean getP2Handicap(){
        return p2Handicap;
    }

    /**
     * setHandicap
     * Checks if both users agree on a handicap and places player 1's handicap
     * if so.
     *
     * @author Jude Gabriel
     * @author Brynn Harrington
     */
    public boolean setHandicap(){
        //Case 1: It is player 1's turn and the game hasn't started
        if((isPlayer1) && (totalMoves) == 0){
            //Set player 1 handicap to true and change the turn
            p1Handicap = true;
            isPlayer1 = !isPlayer1;
            if(p2Handicap == false){
                return true;
            }
        }

        //Case 2: It is player 2's turn and the game hasn't started
        if((!isPlayer1) && (totalMoves == 0)){
            //Set player 2 handicap to true and change the turn
            p2Handicap = true;
            isPlayer1 = !isPlayer1;
            if(p1Handicap == false){
                return true;
            }
        }

        //Case 3: Both players agree to a handicap
        if(p1Handicap && p2Handicap && totalMoves == 0){
            //Place the stones on the this and set both handicap's to false
            gameBoard[2][2].setStoneColor(Stone.StoneColor.WHITE);
            gameBoard[6][6].setStoneColor(Stone.StoneColor.WHITE);
            p1Handicap = false;
            p2Handicap = false;
        }
        return true;
    }
/////////////////////////////////////////////////////////////////////////////////
    /**
	 * Attempts to play a move for the player who is next
	 * @param move				
	 * @throws MoveException	throws if move is invalid for this players
	 */
	public void addMove(Move move) throws MoveException{
		Player currentPlayer = players.poll();
		Set<Stone> captured;
		if (move.getType().equals(MoveType.PASS)){
			captured = new HashSet<>();
		} else {
			captured = this.makeMove(move, currentPlayer.getColor());
		}
		players.add(currentPlayer);
		moveHistory.push(new MoveResult(move, captured, ++turn));
	}
	
	/**
	 * Returns a set of moves for the requested player
	 * @param player 	the requested player
	 * @return			the set of possible moves
	 */
	public Set<Move> getPossibleMoves(PlayerColor player) {
		Set<Move> moves = new HashSet<Move>();
		for (int i=0; i<this.getBoardSize(); i++){
			for (int j=0; j<this.getBoardSize(); j++){
				Move move = Move.getMoveInstance(MoveType.NORMAL, i, j);
				if (isLegalMove(move, player)){
					moves.add(move);
				}
			}
		}
		return moves;
	}
	
	/**
	 * @return	the last move that was played
	 */
	public Move getLastMove(){
		return getLastMove(getLastMoved().getColor());
	}
	
	/**
	 * Returns the last move that was played by a player
	 * @param color		the color of the requested player
	 * @return			the last move that was played
	 */
	public Move getLastMove(PlayerColor color){
		Move move = null;
		if (!moveHistory.isEmpty()){
			move = moveHistory.peekFirst().move;
			if (players.peekLast().getColor().equals(color)){
				return move;
			} else {
				move = null;
				MoveResult temp = moveHistory.poll();
				if (!moveHistory.isEmpty()){
					move = moveHistory.peekFirst().move;
				}
				moveHistory.push(temp);
			}
		}
		return move;
	}
	
	/**
	 * Returns whether or not the move is valid, includes ko logic
	 * @param move		the move in question
	 * @param color		the player who is playing the move
	 * @return			true if move is valid, false otherwise
	 */
	public boolean isLegalMove(Move move, PlayerColor color){
		if (move == null){
			return false;
		} else if (move.equals(Move.getMoveInstance(MoveType.PASS, 0, 0))){
			return true;
		}
		Boolean boardLegal = this.isLegalMove(move, color);
		MoveResult last = moveHistory.peekFirst();
		Boolean retakingKo = last != null && last.captured.size() == 1 && 
				this.getStoneGroupLibertiesAtLocation(last.move.getX(), last.move.getY()) == 1 &&
				move.getX().equals(last.captured.iterator().next().x_location) &&
				move.getY().equals(last.captured.iterator().next().y_location);
		
		return boardLegal && !retakingKo;
	}

    /**
	 * Returns a set of moves for the requested player
	 * @param player 	the requested player
	 * @return			the set of possible moves
	 */
	public Set<Move> getPossibleMoves(Stone.StoneColor player) {
		Set<Move> moves = new HashSet<Move>();
		for (int i = 0; i < boardSize; i++){
			for (int j = 0; j < boardSize; j++){
				Move move = Move.getMoveInstance(MoveType.PLACE, i, j);
				if (isLegalMove(move, player)){
					moves.add(move);
				}
			}
		}
		return moves;
	}

	/**
	 * Returns whether the location defined by the move is an eye for the requested player
	 * @param move		the move representing the x,y location of the eye
	 * @param color		the player who "owns" the eye
	 * @return			returns true if this is an eye for this player, false otherwise
	 */
	public boolean isEye(Move move, Stone.StoneColor color){
		return this.isEye(move, color);
	}

	/**
	 * @return	get the last player moved
	 */
	public Player getLastMoved() {
		return players.peekLast();
	}
	
	/**
	 * @return	get the player whose turn it is
	 */
	public Player getNextToMove() {
		return players.peek();
	}
	
	/**
	 * @return	returns the score
	 */
	public Map<Stone.StoneColor, Integer> getScore(){
		return this.getScore();
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder("MoveHistory: " + moveHistory.toString());
		b.append(this.toString());
		return b.toString();
	}

	public void captureDeadGroups() {
		//do Bensen's algorithm in the future
		this.captureDeadGroups();
	}

	public Set<StoneGroup> getGroupsInAtari() {
		return this.getGroupsInAtari();
	}

	public StoneGroup getStoneGroupAt(Move move) {
		return this.getStoneGroupAt(move);
	}

    /********* HELPER METHODS FOR TESTING ***********/

    /**
     * Checks if two boards are equal
     *
     * @param object the gamestate to compare to
     *
     * @return true if the gamestate's are equal
     * @author Jude Gabriel
     */
    public boolean equals(Object object) {
        //Check if the object is an instance of the gamestate
        if (!(object instanceof GoGameState)) {
            return false;
        }

        //Get the gamestate
        GoGameState goGameState = (GoGameState) object;

        //Compare if the gamestates have the same color at each location
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(this.gameBoard[i][j].getStoneColor() !=
                        goGameState.gameBoard[i][j].getStoneColor()){

                    //Boards were not equal
                    return false;
                }
            }
        }

        //Boards were equal
        return true;
    }


    /**
     * Compares if two constructors are equal
     *
     * @param object the gamestate to compare to
     *
     * @return true if they are equal
     * @author Jude Gabriel
     */
    public boolean testCopyConstructor(Object object){
        //Test all non-this instance variables
        if(!(object instanceof GoGameState)){
            return false;
        }
        if(this.userXClick != ((GoGameState) object).userXClick){
            return false;
        }
        if(this.userYClick != ((GoGameState) object).userYClick){
            return false;
        }
        if(this.boardSize != ((GoGameState) object).boardSize){
            return false;
        }
        if(this.isPlayer1 != ((GoGameState) object).isPlayer1){
            return false;
        }
        if(this.player1Score != ((GoGameState) object).player1Score){
            return false;
        }
        if(this.player2Score != ((GoGameState) object).player2Score){
            return false;
        }
        if(this.gameOver != ((GoGameState) object).gameOver){
            return false;
        }
        if(this.totalMoves != ((GoGameState) object).totalMoves){
            return false;
        }
        if(this.numSkips != ((GoGameState) object).numSkips){
            return false;
        }
        if(this.p1Handicap != ((GoGameState) object).getP1Handicap()){
            return false;
        }
        if(this.p2Handicap != ((GoGameState) object).getP2Handicap()){
            return false;
        }
        if(this.player1Forfeit != ((GoGameState) object).getPlayer1Forfeit()){
            return false;
        }
        if(this.player2Forfeit != ((GoGameState) object).getPlayer2Forfeit()){
            return false;
        }
        if (this.time != ((GoGameState) object).getTime()){
            return false;
        }
        if(this.mostRecentMove != getMostRecentMove()){
            return false;
        }

        //Check gameboard values
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(gameBoard[i][j].getStoneColor() !=
                        ((GoGameState) object).gameBoard[i][j].getStoneColor()){
                    return false;
                }
                if(gameBoard[i][j].getCheckedStone() !=
                        ((GoGameState) object).gameBoard[i][j].getCheckedStone()){
                    return false;
                }
                if(gameBoard[i][j].getxLeft() !=
                        ((GoGameState) object).gameBoard[i][j].getxLeft()){
                    return false;
                }
                if(gameBoard[i][j].getxRight() !=
                        ((GoGameState) object).gameBoard[i][j].getxRight()){
                    return false;
                }
                if(gameBoard[i][j].getxLocation() !=
                        ((GoGameState) object).gameBoard[i][j].getxLocation()){
                    return false;
                }
                if(gameBoard[i][j].getyBottom() !=
                        ((GoGameState) object).gameBoard[i][j].getyBottom()){
                    return false;
                }
                if(gameBoard[i][j].getyTop() !=
                        ((GoGameState) object).gameBoard[i][j].getyTop()){
                    return false;
                }
                if(gameBoard[i][j].getyLocation() !=
                        ((GoGameState) object).gameBoard[i][j].getyLocation()){
                    return false;
                }
                if(gameBoard[i][j].getRadius() !=
                        ((GoGameState) object).gameBoard[i][j].getRadius()){
                    return false;
                }
            }
        }

        //Check stone copies first values
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(stoneCopiesFirst[i][j].getStoneColor() !=
                        ((GoGameState) object).stoneCopiesFirst[i][j].getStoneColor()){
                    return false;
                }
                if(stoneCopiesFirst[i][j].getCheckedStone() !=
                        ((GoGameState) object).stoneCopiesFirst[i][j].getCheckedStone()){
                    return false;
                }
                if(stoneCopiesFirst[i][j].getxLeft() !=
                        ((GoGameState) object).stoneCopiesFirst[i][j].getxLeft()){
                    return false;
                }
                if(stoneCopiesFirst[i][j].getxRight() !=
                        ((GoGameState) object).stoneCopiesFirst[i][j].getxRight()){
                    return false;
                }
                if(stoneCopiesFirst[i][j].getxLocation() !=
                        ((GoGameState) object).stoneCopiesFirst[i][j].getxLocation()){
                    return false;
                }
                if(stoneCopiesFirst[i][j].getyBottom() !=
                        ((GoGameState) object).stoneCopiesFirst[i][j].getyBottom()){
                    return false;
                }
                if(stoneCopiesFirst[i][j].getyTop() !=
                        ((GoGameState) object).stoneCopiesFirst[i][j].getyTop()){
                    return false;
                }
                if(stoneCopiesFirst[i][j].getyLocation() !=
                        ((GoGameState) object).stoneCopiesFirst[i][j].getyLocation()){
                    return false;
                }
                if(stoneCopiesFirst[i][j].getRadius() !=
                        ((GoGameState) object).stoneCopiesFirst[i][j].getRadius()){
                    return false;
                }
            }
        }

        //Check stone copies second values
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(stoneCopiesSecond[i][j].getStoneColor() !=
                        ((GoGameState) object).stoneCopiesSecond[i][j].getStoneColor()){
                    return false;
                }
                if(stoneCopiesSecond[i][j].getCheckedStone() !=
                        ((GoGameState) object).stoneCopiesSecond[i][j].getCheckedStone()){
                    return false;
                }
                if(stoneCopiesSecond[i][j].getxLeft() !=
                        ((GoGameState) object).stoneCopiesSecond[i][j].getxLeft()){
                    return false;
                }
                if(stoneCopiesSecond[i][j].getxRight() !=
                        ((GoGameState) object).stoneCopiesSecond[i][j].getxRight()){
                    return false;
                }
                if(stoneCopiesSecond[i][j].getxLocation() !=
                        ((GoGameState) object).stoneCopiesSecond[i][j].getxLocation()){
                    return false;
                }
                if(stoneCopiesSecond[i][j].getyBottom() !=
                        ((GoGameState) object).stoneCopiesSecond[i][j].getyBottom()){
                    return false;
                }
                if(stoneCopiesSecond[i][j].getyTop() !=
                        ((GoGameState) object).stoneCopiesSecond[i][j].getyTop()){
                    return false;
                }
                if(stoneCopiesSecond[i][j].getyLocation() !=
                        ((GoGameState) object).stoneCopiesSecond[i][j].getyLocation()){
                    return false;
                }
                if(stoneCopiesSecond[i][j].getRadius() !=
                        ((GoGameState) object).stoneCopiesSecond[i][j].getRadius()){
                    return false;
                }
            }
        }

        //If nothing is false, gamestates are equal
        return true;
    }


    /** testCaptures
     * Used to test if captures works
     *
     * @author Jude Gabriel
     */
    public void testCapture() {
        //Populate the this with stones
        gameBoard[0][0].setStoneColor(Stone.StoneColor.BLACK);
        gameBoard[0][1].setStoneColor(Stone.StoneColor.BLACK);
        gameBoard[0][2].setStoneColor(Stone.StoneColor.BLACK);
        gameBoard[1][0].setStoneColor(Stone.StoneColor.BLACK);
        gameBoard[1][1].setStoneColor(Stone.StoneColor.WHITE);
        gameBoard[1][2].setStoneColor(Stone.StoneColor.BLACK);
        gameBoard[2][0].setStoneColor(Stone.StoneColor.BLACK);
        gameBoard[2][1].setStoneColor(Stone.StoneColor.BLACK);
    }


    /**
     * testRepeatedPositions
     * Used to test a player does not repeat the past position
     *
     * @author Brynn Harrington
     * @author Jude Gabriel
     */
    public void testRepeatedPosition() {
        //Set up the triangle of black stones
        gameBoard[0][1].setStoneColor(Stone.StoneColor.BLACK);
        gameBoard[1][0].setStoneColor(Stone.StoneColor.BLACK);

        //Set up the triangle of whiteStones
        gameBoard[1][1].setStoneColor(Stone.StoneColor.WHITE);
        gameBoard[2][2].setStoneColor(Stone.StoneColor.WHITE);
        gameBoard[1][3].setStoneColor(Stone.StoneColor.WHITE);
    }


    /**
     * testForfeit
     * Used to test if forfeit works
     *
     * @author Brynn Harrington
     * @author Jude Gabriel
     */
    public void testForfeit() {
        gameOver = false;

        // dummy values for the this
        gameBoard[0][3].setStoneColor(Stone.StoneColor.BLACK);
        gameBoard[2][0].setStoneColor(Stone.StoneColor.BLACK);
        gameBoard[1][3].setStoneColor(Stone.StoneColor.WHITE);
        gameBoard[0][0].setStoneColor(Stone.StoneColor.WHITE);

        // forfeit
        this.forfeit();
    }


    /**
     * Used to reset the this in testing
     *
     * @author Jude Gabriel
     */
    public void resetStones(){
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                gameBoard[i][j].setStoneColor(Stone.StoneColor.NONE);
            }
        }
    }

    /**
	 * addStoneNoGUI
	 *
	 * adds a stone to the this without displaying it onto the GUI
	 *
	 * @param this - the this to make the moves on
	 * @param row - the row to be placed
	 * @param col - the column to be placed
     *
     * @author Brynn Harrington
	 */
	public void addStoneNoGUI(Stone[][] this, int row, int col) {
		this[row][col].setStoneColor(isPlayer1 ? Stone.StoneColor.BLACK : Stone.StoneColor.WHITE);
	}
}




