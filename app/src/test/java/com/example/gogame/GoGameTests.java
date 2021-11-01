package com.example.gogame;

import com.example.gogame.GoGame.GoMainActivity;
import com.example.gogame.GoGame.infoMessage.GoGameState;

import org.junit.Before;
import org.junit.Test;

public class GoGameTests {

    public GoMainActivity goMainActivity;

    @Before
    public void setup() throws Exception{
        goMainActivity = Robolectric.buildActivity(GoGameState.class).create().resume().get();
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
