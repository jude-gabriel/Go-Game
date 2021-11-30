package com.example.gogame.GoGame.players;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gogame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gogame.GameFramework.players.GameHumanPlayer;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GoGame.goActionMessage.GoDumbAIAction;
import com.example.gogame.GoGame.goActionMessage.GoForfeitAction;
import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoQuitGameAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.goActionMessage.GoSmartAIAction;
import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.views.GoSurfaceView;
import com.example.gogame.R;

public class GoHumanPlayer1 extends GameHumanPlayer implements View.OnTouchListener, View.OnClickListener {

    //Variables used to reference widgets that will be modified during play
    private TextView player1ScoreText   = null;
    private TextView player2ScoreText   = null;
    private TextView playerTurnText     = null;
    private TextView validMoveText      = null;
    private TextView timerText          = null;
    private TextView opponentMoveText   = null;
    private TextView helpButtonText1 = null;
    private TextView helpButtonText2 = null;
    private TextView helpButtonText3 = null;
    private TextView helpButtonText4 = null;

    private Button skipButton           = null;
    private Button handicapButton       = null;
    private Button forfeitButton        = null;
    private Button dumbAIButton         = null;
    private Button smartAIButton        = null;
    private Button quitGameButton       = null;
    private Button helpButton           = null;

    //Tag for logging
    private static final String TAG = "GoHumanPlayer1";

    //Surface view
    private GoSurfaceView goSurfaceView;

    //ID for the layout to use
    private int layoutId;

    //boolean to flip back and forth between displaying help messages when help button clicked
    private boolean displayHelpMessages = false;


    /**
     * Constructor for GoHumanPlayer1
     *
     * @param name      the name of the player
     * @param layoutID  id for the layout
     *
     * @author Jude Gabriel
     */
    public GoHumanPlayer1(String name, int layoutID){
        //Initialize instance variables
        super(name);
        this.layoutId = layoutID;
    }


    /**
     * Has player receive the current game info
     * @param info the current game info
     *
     * @author Jude Gabriel
     *
     * TODO: Add support for timer and test
     */
    @Override
    public void receiveInfo(GameInfo info) {
        //Create variables for the gamestate info
        int p1Score;
        int p2Score;
        int elapsedMin;
        int elapsedSec;
        int playerTurn;

        //Error check if the surface view exists
        if(goSurfaceView == null){
            return;
        }


        /** Update the view objects?? **/
        if(info instanceof GoGameState){
            p1Score = ((GoGameState) info).getPlayer1Score();
            p2Score = ((GoGameState) info).getPlayer2Score();
            playerTurn = ((GoGameState) info).getPlayer();

            //Check that all GUI objects exist
            if(player1ScoreText != null && playerTurnText != null && timerText != null) {
                //Update the scores
                player1ScoreText.setText(allPlayerNames[0] +"'s Score: " + p1Score);
                player2ScoreText.setText(allPlayerNames[1] + "'s Score: " + p2Score);

                //Update who's turn it is
                playerTurnText.setText(allPlayerNames[playerTurn] + "'s Turn!");

                if(playerTurn == 1){
                    playerTurn = 0;
                }
                else{
                    playerTurn = 1;
                }

                //Check if the game has started and set text boxes to visible
                if(((GoGameState) info).getTotalMoves() != 0) {
                    validMoveText.setVisibility(View.VISIBLE);
                    opponentMoveText.setVisibility(View.VISIBLE);

                    if (((GoGameState) info).getNumSkips() != 0) {

                        //Find who's turn it is
                        int otherPlayer;
                        if(playerTurn == 0){
                            otherPlayer = 1;
                        }
                        else{
                            otherPlayer = 0;
                        }

                        //Update the move text box based on the kind of move
                        opponentMoveText.setText(allPlayerNames[playerTurn] + " skipped their turn. " +
                                allPlayerNames[otherPlayer] + ", press skip turn to end the game.");
                    }
                    else if (((GoGameState) info).getP1Handicap() == true) {
                        opponentMoveText.setText(allPlayerNames[0] + " agrees to a handicap.");
                    }
                    else if (((GoGameState) info).getP2Handicap() == true) {
                        opponentMoveText.setText(allPlayerNames[1] + " agrees to a handicap.");
                    }
                    else {
                        int[] lastMove = ((GoGameState) info).getMostRecentMove();
                        int x = lastMove[0] + 1;
                        int y = lastMove[1] + 1;
                        opponentMoveText.setText(allPlayerNames[playerTurn] + " placed a stone at: " +
                                x + ", " + y);
                    }
                }
                else{
                    //If this case is hit set visibility to invisible since game hasn't started
                    opponentMoveText.setVisibility(View.INVISIBLE);
                }

                //Calculate the time and display
                elapsedSec = ((GoGameState) info).getTime();
                elapsedMin = elapsedSec / 60;
                elapsedSec = elapsedSec % 60;
                if(elapsedSec < 10){
                    timerText.setText("Elapsed Time: " + elapsedMin + ":0" + elapsedSec);
                }
                else {
                    timerText.setText("Elapsed Time: " + elapsedMin + ":" + elapsedSec);
                }

                //Remove handicap button after the first move
                if(((GoGameState) info).getTotalMoves() > 0){
                    handicapButton.setVisibility(View.GONE);
                }

                //Update the surface view
                goSurfaceView.invalidate();
            }
        }

        //Check if the move was valid. If it wasn't produce an error message.
        //if it is set the current state and then call invalidate
        if(info instanceof IllegalMoveInfo){
            validMoveText.setVisibility(View.VISIBLE);
            goSurfaceView.flash(Color.RED, 1000);
            validMoveText.setText("INVALID MOVE");
            validMoveText.setBackgroundColor(Color.RED);
            Logger.log(TAG, "Invalid move hit");
        }

        //Check the human tried to move out of turn, update the valid move text
        else if(info instanceof NotYourTurnInfo){
            validMoveText.setVisibility(View.VISIBLE);
            validMoveText.setText("NOT YOUR TURN");
            validMoveText.setBackgroundColor(Color.RED);
        }

        //If we hit here and do not have a gamestate action, exit
        else if(!(info instanceof GoGameState)){
            return;
        }

        //If it is, then update the surface view
        else{
            goSurfaceView.setState((GoGameState)info);
            goSurfaceView.invalidate();
        }

    }


    /**
     * Updates the activity GUI to be the player
     *
     * @param activity the current activity
     *
     * @author Jude Gabriel
     *
     * TODO: Requires Testing
     */
    @Override
    public void setAsGui(GameMainActivity activity) {
        //Initialize the activity
        activity.setContentView(layoutId);

        //initialize the widget reference members
        if(activity != null) {
            this.player1ScoreText = (TextView) activity.findViewById(R.id.player1ScoreText);
            this.player2ScoreText = (TextView) activity.findViewById(R.id.player2ScoreText);
            this.playerTurnText = (TextView) activity.findViewById(R.id.playerTurnText);
            this.validMoveText = (TextView) activity.findViewById(R.id.validMovetext);
            validMoveText.setVisibility(View.INVISIBLE);
            this.timerText = (TextView) activity.findViewById(R.id.elapsedTimeText);
            this.opponentMoveText = (TextView) activity.findViewById(R.id.opponentMoveText);
            opponentMoveText.setVisibility(View.INVISIBLE);
            this.helpButtonText1 = (TextView) activity.findViewById(R.id.helpButtonText1);
            helpButtonText1.setVisibility(View.INVISIBLE);
            this.helpButtonText2 = (TextView) activity.findViewById(R.id.helpButtonText3);
            helpButtonText2.setVisibility(View.INVISIBLE);
            this.helpButtonText3 = (TextView) activity.findViewById(R.id.helpButtonText3);
            helpButtonText3.setVisibility(View.INVISIBLE);
            this.helpButtonText4 = (TextView) activity.findViewById(R.id.helpButtonText4);
            helpButtonText4.setVisibility(View.INVISIBLE);

            this.handicapButton = (Button) activity.findViewById(R.id.handicapButton);
            this.skipButton = (Button) activity.findViewById(R.id.skipTurnButton);
            this.forfeitButton = (Button) activity.findViewById(R.id.forfeitButton);
            this.dumbAIButton = (Button) activity.findViewById(R.id.dumbAIButton);
            this.smartAIButton = (Button) activity.findViewById(R.id.smartAIButton);
            this.quitGameButton = (Button) activity.findViewById(R.id.quitGameButton);
            this.helpButton = (Button) activity.findViewById(R.id.helpButton);
        }

        //Initialize the surface view
        goSurfaceView = (GoSurfaceView)myActivity.findViewById(R.id.goSurfaceView);

        //Set the listener for all buttons
        goSurfaceView.setOnTouchListener(this);
        skipButton.setOnClickListener(this);
        handicapButton.setOnClickListener(this);
        this.forfeitButton.setOnClickListener(this);
        this.dumbAIButton.setOnClickListener(this);
        this.smartAIButton.setOnClickListener(this);
        this.quitGameButton.setOnClickListener(this);
        this.helpButton.setOnClickListener(this);
    }


    /**
     * Getter for the GUI's top view
     *
     * @return the GUI's top view
     *
     * @author Jude Gabriel
     */
    @Override
    public View getTopView() {
        //Return the top gui
        return myActivity.findViewById(R.id.top_gui_layout);
    }


    /**
     * Performs all initialization that needs to be done after all starting
     * info is received
     *
     * @author Jude Gabriel
     *
     */
    public void initAfterReady(){
        //Initialize the title
        myActivity.setTitle("Go: " + allPlayerNames[0] + " vs " + allPlayerNames[1]);
    }


    /**
     * Detects the users move and calls the appropriate methods to alter the game
     * board
     *
     * @param v         the current view
     * @param event     the touch event we are handling
     * @return          true after the event has been handled
     *
     * @author Jude Gabriel
     *
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Initialize the array that stores the clicks
        int[] xyLoc = null;

        //Check if the action has ended
        if(event.getAction() != MotionEvent.ACTION_UP){
            return true;
        }

        //Get the coordinates of a press-location and map it to a liberty
        int x = (int) event.getX();
        int y = (int) event.getY();
        xyLoc = goSurfaceView.findStone(x, y);

        //Check if the location was not valid and update validMove if so
        if(xyLoc == null || xyLoc[0] == -1 || xyLoc[1] == -1){
            validMoveText.setVisibility(View.VISIBLE);
            validMoveText.setText("INVALID MOVE");
            validMoveText.setBackgroundColor(Color.RED);
            goSurfaceView.flash(Color.RED, 1000);
            goSurfaceView.invalidate();
        }
        else{
            //Create a new action and send it to the game
            GoMoveAction action = new GoMoveAction(this, xyLoc[0], xyLoc[1]);
            Logger.log("onTouch", "Human player sending Go Move Action");
            game.sendAction(action);

            //Update the valid move text since the move was valid
            validMoveText.setVisibility(View.VISIBLE);
            validMoveText.setText("VALID MOVE");
            validMoveText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            validMoveText.setBackgroundColor(Color.GREEN);

            //Update the surface view
            goSurfaceView.invalidate();
        }

        //Event finished
        return true;
    }


    /**
     * Handler for click events
     *
     * @param v the current view
     *
     * @author Jude Gabriel
     *
     * TODO: Requires Testing
     */
    @Override
    public void onClick(View v) {
        //Get the ID of the object clicked
        int viewID = v.getId();
        if(game == null) return;
        switch (viewID){

            //Case 1: It was the skip turn button, send a skip turn action
            case R.id.skipTurnButton:
                game.sendAction(new GoSkipTurnAction(this));
                break;

            //Case 2: It was the forfeit button, send a forfeit action
            case R.id.forfeitButton:
                game.sendAction(new GoForfeitAction(this));
                break;

            //Case 3: It was the handicap button, send a handicap action
            case R.id.handicapButton:
                game.sendAction(new GoHandicapAction(this));
                break;

            //Case 4: It was the quit game button, send a quit game action
            case R.id.quitGameButton:
                game.sendAction(new GoQuitGameAction(this));
                break;

            //Case 5: It was the dumbAi button, send a dumbAi action
            case R.id.dumbAIButton:
                game.sendAction(new GoDumbAIAction(this));
                break;

            //Case 6: It was the smartAi button, send a smartAI action
            case R.id.smartAIButton:
                game.sendAction(new GoSmartAIAction(this));
                break;

            //Case 7: It was the help button, display helpButtonText
            case R.id.helpButton:
                // switch between displaying and not displaying help messages
                displayHelpMessages = !displayHelpMessages;

                if(displayHelpMessages) {
                    helpButtonText1.setVisibility(View.VISIBLE);
                    helpButtonText2.setVisibility(View.VISIBLE);
                    helpButtonText3.setVisibility(View.VISIBLE);
                    helpButtonText4.setVisibility(View.VISIBLE);
                }
                else{
                    helpButtonText1.setVisibility(View.INVISIBLE);
                    helpButtonText2.setVisibility(View.INVISIBLE);
                    helpButtonText3.setVisibility(View.INVISIBLE);
                    helpButtonText4.setVisibility(View.INVISIBLE);
                }
                break;


            //If this case is hit, it was not one of the buttons that was hit. Exit method
            default:
                return;
        }

        //Invalidate the surface view
        goSurfaceView.invalidate();

        //Error if we hit here
        return;
    }
}
