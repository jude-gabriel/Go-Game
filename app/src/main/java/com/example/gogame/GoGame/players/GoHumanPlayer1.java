package com.example.gogame.GoGame.players;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gogame.GameFramework.GameMainActivity;
import com.example.gogame.GameFramework.actionMessage.EndTurnAction;
import com.example.gogame.GameFramework.actionMessage.TimerAction;
import com.example.gogame.GameFramework.infoMessage.BindGameInfo;
import com.example.gogame.GameFramework.infoMessage.GameInfo;
import com.example.gogame.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.gogame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gogame.GameFramework.infoMessage.TimerInfo;
import com.example.gogame.GameFramework.players.GameHumanPlayer;
import com.example.gogame.GameFramework.utilities.GameTimer;
import com.example.gogame.GameFramework.utilities.Logger;
import com.example.gogame.GoGame.goActionMessage.GoDumbAIAction;
import com.example.gogame.GoGame.goActionMessage.GoForfeitAction;
import com.example.gogame.GoGame.goActionMessage.GoHandicapAction;
import com.example.gogame.GoGame.goActionMessage.GoMoveAction;
import com.example.gogame.GoGame.goActionMessage.GoNetworkPlayAction;
import com.example.gogame.GoGame.goActionMessage.GoQuitGameAction;
import com.example.gogame.GoGame.goActionMessage.GoSkipTurnAction;
import com.example.gogame.GoGame.goActionMessage.GoSmartAIAction;
import com.example.gogame.GoGame.goActionMessage.GoTwoPlayerAction;
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
    private Button skipButton           = null;
    private Button handicapButton       = null;
    private Button forfeitButton        = null;
    private Button twoPlayerButton      = null;
    private Button dumbAIButton         = null;
    private Button smartAIButton        = null;
    private Button networkPlay          = null;
    private Button quitGameButton       = null;

    //Tag for logging
    private static final String TAG = "GoHumanPlayer1";

    //Surface view
    private GoSurfaceView goSurfaceView;

    //ID for the layout to use
    private int layoutId;


    /**
     * Constructor for GoHumanPlayer1
     *
     * @param name      the name of the player
     * @param layoutID  id for the layout
     *
     * @author Jude Gabriel
     */
    public GoHumanPlayer1(String name, int layoutID){
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
        int p1Score;
        int p2Score;
        int elapsedMin;
        int elapsedSec;
        int playerTurn;

        Logger.log(TAG, "Hit recieveInfo : " + info.getClass());



        /** Update the view objects?? **/
        if(info instanceof GoGameState){
            p1Score = ((GoGameState) info).getPlayer1Score();
            p2Score = ((GoGameState) info).getPlayer2Score();
            playerTurn = ((GoGameState) info).getPlayer();

            if(player1ScoreText != null && playerTurnText != null && timerText != null) {
                player1ScoreText.setText("Player 1 Score: " + p1Score);
                player2ScoreText.setText("Player 2 Score: " + p2Score);
                playerTurnText.setText(allPlayerNames[playerTurn] + "'s Turn!");
                handicapButton.setText("HANDICAP");
                timerText.setText("TIMER");
                timerText.setText("Timer " + ((GoGameState) info).getTime());
                if(((GoGameState) info).getTotalMoves() > 0){
                    handicapButton.setVisibility(View.GONE);
                }
                Logger.log(TAG, "TIMER");
                goSurfaceView.invalidate();

            }

            //What should be done for the timer????
        }

        //Error check if the surface view exists
        if(goSurfaceView == null){
            return;
        }

        //Check if the move was valid. If it wasn't produce an error message.
        //if it is set the current state and then call invalidate
        if(info instanceof IllegalMoveInfo){
            goSurfaceView.flash(Color.RED, 1000);
            validMoveText.setText("INVALID MOVE");
            validMoveText.setBackgroundColor(Color.RED);
            Logger.log(TAG, "Invalid move hit");
        }

        //Check the human tried to move out of turn, update the valid move text
        else if(info instanceof NotYourTurnInfo){
            validMoveText.setText("NOT YOUR TURN");
            validMoveText.setBackgroundColor(Color.RED);
        }

        //Check if it was a timer action, if so update the timer
        else if(info instanceof TimerInfo){
//            GameTimer timer = ((TimerInfo) info).getTimer();
//            elapsedSec = timer.getTicks();
//            elapsedMin = elapsedSec / 60;
//            elapsedSec = elapsedSec % 60;
//            timerText.setText("Elapsed Time: " + elapsedMin + ":" + elapsedSec);
//            Logger.log(TAG, "TIMER");

        }

        //If we hit here and do not have a gamestate action, exit
        else if(!(info instanceof GoGameState)){
            return;
        }

        //If it is, then update the surface view
        else{
            goSurfaceView.setState((GoGameState)info);  //NEED setState FROM NATALIE
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
            this.timerText = (TextView) activity.findViewById(R.id.elapsedTimeText);
            this.handicapButton = (Button) activity.findViewById(R.id.handicapButton);
            this.skipButton = (Button) activity.findViewById(R.id.skipTurnButton);
            this.forfeitButton = (Button) activity.findViewById(R.id.forfeitButton);
            this.twoPlayerButton = (Button) activity.findViewById(R.id.twoPlayerButton);
            this.dumbAIButton = (Button) activity.findViewById(R.id.dumbAIButton);
            this.smartAIButton = (Button) activity.findViewById(R.id.smartAIButton);
            this.networkPlay = (Button) activity.findViewById(R.id.networkButton);
            this.quitGameButton = (Button) activity.findViewById(R.id.quitGameButton);
        }

        //Initialize the surface view
        goSurfaceView = (GoSurfaceView)myActivity.findViewById(R.id.goSurfaceView);

        //Set the listener for all buttons
        goSurfaceView.setOnTouchListener(this);
        skipButton.setOnClickListener(this);
        handicapButton.setOnClickListener(this);
        this.forfeitButton.setOnClickListener(this);
        this.twoPlayerButton.setOnClickListener(this);
        this.dumbAIButton.setOnClickListener(this);
        this.smartAIButton.setOnClickListener(this);
        this.networkPlay.setOnClickListener(this);
        this.quitGameButton.setOnClickListener(this);
    }


    /**
     * Getter for the GUI's top view
     *
     * @return the GUI's top view
     *
     * @author Jude Gabriel
     *
     * TODO: Requires Testing
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
     * TODO: Requires Testing
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
     * TODO: Requires Testing
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
            validMoveText.setText("VALID MOVE");
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

            //Case 7: It was the 2-player button, send a 2-player action
            case R.id.twoPlayerButton:
                game.sendAction(new GoTwoPlayerAction(this));
                break;

            //Case 8: It was the network play button, send a network play action
            case R.id.networkButton:
                game.sendAction(new GoNetworkPlayAction(this));
                break;

            //If this case is hit, it was not one of the buttons that was hit. Exit method
            default:
                return;
        }
        goSurfaceView.invalidate();

        //Error if we hit here
        return;
    }
}
