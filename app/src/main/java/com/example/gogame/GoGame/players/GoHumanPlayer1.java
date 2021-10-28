package com.example.gogame.GoGame.players;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
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
     * TODO: This is where we will change all the view objects and update them
     * to reflect the stuff we need to show. i.e. timer, score etc. Check
     * PigHumanplayer for reference
     */
    @Override
    public void receiveInfo(GameInfo info) {
        int p1Score;
        int p2Score;
        int elapsedMin;
        int elapsedSec;
        int playerTurn;


        /** Update the view objects?? **/
        if(info instanceof GoGameState){
            p1Score = ((GoGameState) info).getPlayer1Score();
            p2Score = ((GoGameState) info).getPlayer2Score();
            playerTurn = ((GoGameState) info).getPlayer();

            player1ScoreText.setText("Player 1 Score: " + p1Score);
            player1ScoreText.setText("Player 2 Score: " + p2Score);
            playerTurnText.setText(allPlayerNames[playerTurn] + "'s Turn!");

            //What should be done for the timer????
        }


        //Error check if the surface view exists
        if(goSurfaceView == null){
            return;
        }

        /* Check if the move was valid. If it wasn't produce an error message.
            if it is set the current state and then call invalidate.
         */
        if(info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo){
            goSurfaceView.flash(Color.RED, 1000);
        }
        else if(!(info instanceof GoGameState)){
            return;
        }
        else{
            //goSurfaceView.setState((GoGameState)info);  //NEED setState FROM NATALIE
            goSurfaceView.invalidate();
            Logger.log(TAG, "receiving");
        }

    }

    /**
     * Updates the activity GUI to be the player
     *
     * @param activity the current activity
     *
     * @author Jude Gabriel
     *
     * TODO: Gather all buttons and texts here
     */
    @Override
    public void setAsGui(GameMainActivity activity) {

        //initialize the widget reference members
        this.player1ScoreText = (TextView)activity.findViewById(R.id.player1ScoreText);
        this.player2ScoreText = (TextView)activity.findViewById(R.id.player2ScoreText);
        this.playerTurnText = (TextView)activity.findViewById(R.id.playerTurnText);
        this.validMoveText = (TextView)activity.findViewById(R.id.validMovetext);
        this.timerText = (TextView)activity.findViewById(R.id.elapsedTimeText);

        activity.setContentView(layoutId);

        //SURFACE VIEW DOES NOT EXIST YET
        goSurfaceView = (GoSurfaceView)myActivity.findViewById(R.id.goSurfaceView);

        Logger.log("set listener", "onTouch");
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
        //DOESN'T EXIST YET
        return myActivity.findViewById(R.id.top_gui_layout);
    }


    /**
     * Performs all initialization that needs to be done after all starting
     * info is received
     *
     * @author Jude Gabriel
     */
    public void initAfterReady(){
        myActivity.setTitle("Go: " + allPlayerNames[0] + " vs " + allPlayerNames[1]);
    }


    /**
     * Detects the users move and calls the appropriate methods to alter the game
     * board
     * @param v         the current view
     * @param event     the touch event we are handling
     * @return          true after the event has been handled
     *
     * @author Jude Gabriel
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int[] xyLoc = null;

        //Check if the action has ended
        if(event.getAction() != MotionEvent.ACTION_UP){
            return true;
        }

        //Get the coordinates of a press-location and map it to a liberty
        int x = (int) event.getX();
        int y = (int) event.getY();
        xyLoc = goSurfaceView.findStone(x, y);  //NEED FIND STONE FROM NATALIE

        //Check if the location was not valid and flash screen if so
        if(xyLoc == null || xyLoc[0] == -1 || xyLoc[1] == -1){
            validMoveText.setText("INVALID MOVE");
            validMoveText.setBackgroundColor(Color.RED);
            goSurfaceView.flash(Color.RED, 1000);
        }
        else{
            //Create a new action and send it to the game
            GoMoveAction action = new GoMoveAction(this, xyLoc[0], xyLoc[1]);
            Logger.log("onTouch", "Human player sending Go Move Action");
            game.sendAction(action);
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

        //Error if we hit here
        return;
    }
}
