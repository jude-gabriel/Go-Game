package com.example.gogame.GoGame.players;

import com.example.gogame.GoGame.infoMessage.Stone;

import java.util.ArrayList;

public class MonteCarlo {
     /**
     * Move
     *
     * this class represents a move by the smart AI from the minimax alpha-beta pruning algorithm
     *
     */
    class Move
    {
        /* INSTANCE VARIABLES FOR SMART AI MOVE */
        int row;
        int col;

        /**
         * constructor
         *
         * constructs a move object
         *
         */
        public Move(int r, int c)
        {
            row = r;
            col = c;
        }//SmartAIMove

        /**
         * getters and setters for point
         */
        public int getRow() { return row; }
        public int getCol() { return col; }
        public void setRow(int row) { this.row = row; }
        public void setCol(int col) { this.col = col; }
    }//SmartAIMove

    /* INSTANCE VARIABLES */
    static int boardSize = 9;   // size of board
    static int boardLineCount = boardSize * 2 - 1; // since 9 x 9 - don't double count cell you start with
    int[][] testInt = new int[boardLineCount][boardLineCount]; //
    int[][] testStones = new int[boardLineCount][boardLineCount]; // save each coordinate of each step
    int step = 0; // current number of steps
    int[][] winningLiberty = new int[boardLineCount][boardLineCount];
    int[][] pointInt = new int[boardLineCount][boardLineCount];
    int round = 1;
    boolean pass = false; // true means one color pass
    boolean isKo = false; // true is Ko, false is not

    public void test()
    {
        int i, n = 200;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                //Move tp = getComputerMove();
                //TODO -
                /*
                * int i, n=200;
            for(i=0;i<n;i++){
                Move tp = getComputerMove();
                if(tp!=null){
                    putOnTestBoard(tp);
                    userFlag = userFlag*-1;
                }else{
                    if(pass){
                        break;
                    }
                    pass = true;
                    testStones[testSteps][0]=0;
                    testStones[testSteps][1]=0;
                    testSteps++;
                    userFlag = userFlag*-1;
                }
            }
        finish();
    }*/
            }
        }
    }

    public Move getMove()
    {
        int x, y, n = 0, m = 0;
        boolean suicide=false;
            do{
                suicide=false;
                m=0;
                do{
                    x=(int)(Math.random()*9+4);
                    y=(int)(Math.random()*9+4);
                    m++;
                }while(m<50 && (testInt[x][y]!=0 || (!checkAvailable(x,y,userFlag) && checkEat(x,y,userFlag).isEmpty())));
                if (m<50){
                    if(x==4 && y ==4){
                        if(testInt[4][5]+testInt[5][5]+testInt[5][4]==3*userFlag)
                            suicide=true;
                    }else if(x==4 && y==12){
                        if(testInt[4][11]+testInt[5][11]+testInt[5][12]==3*userFlag)
                            suicide=true;
                    }else if(x==12 && y==4){
                        if(testInt[12][5]+testInt[11][5]+testInt[11][4]==3*userFlag)
                            suicide=true;
                    }else if(x==12 && y==12){
                        if(testInt[12][11]+testInt[11][11]+testInt[11][12]==3*userFlag)
                            suicide=true;
                    }else{
                        if(x==4){
                            if(testInt[4][y+1]+testInt[4][y-1]+testInt[5][y+1]+testInt[5][y-1]+testInt[5][y]==5*userFlag)
                                suicide=true;
                        }else if(x==12){
                            if(testInt[12][y+1]+testInt[12][y-1]+testInt[11][y+1]+testInt[11][y-1]+testInt[11][y]==5*userFlag)
                                suicide=true;
                        }else if(y==4){
                            if(testInt[x+1][4]+testInt[x-1][4]+testInt[x+1][5]+testInt[x-1][5]+testInt[x][5]==5*userFlag)
                                suicide=true;
                        }else if(y==12){
                            if(testInt[x+1][12]+testInt[x-1][12]+testInt[x+1][11]+testInt[x-1][11]+testInt[x][11]==5*userFlag)
                                suicide=true;
                        }else{
                            if(testInt[x][y+1]+testInt[x][y-1]+testInt[x+1][y]+testInt[x-1][y]==4*userFlag &&
                                    (testInt[x+1][y+1]+testInt[x+1][y-1]+testInt[x-1][y+1]==3*userFlag ||
                                     testInt[x+1][y+1]+testInt[x+1][y-1]+testInt[x-1][y-1]==3*userFlag ||
                                     testInt[x+1][y+1]+testInt[x-1][y+1]+testInt[x-1][y-1]==3*userFlag ||
                                     testInt[x+1][y-1]+testInt[x-1][y+1]+testInt[x-1][y-1]==3*userFlag))
                                suicide=true;
                        }
                    }
                    if(suicide){
                        n++;
                        testInt[x][y]=0;
                    }
                }else{
                    return null;
                }
                if(n>10){
                    return null;
                }
            }while(suicide);
            return new Move(x, y);
    }

    private ArrayList checkEat(int i, int j, int userFlag) {
        int a=i,b=j;
            ArrayList stonesDead = new ArrayList();
            ArrayList   temp = new ArrayList();
            temp = null;
            int n;
            if(testInt[a+1][b]!=-userFlag && testInt[a][b+1]!=-userFlag && testInt[a-1][b]!=-userFlag && testInt[a][b-1]!=-userFlag)
                return stonesDead;
            a=i+1;
            if(testInt[a][b]==-userFlag)
            {
                temp = findEatPart(a, b, userFlag);
                if (temp != null)
                {
                    for(n=0; n<temp.size();n++)
                    stonesDead.add(temp.get(n));
                }
            }
            a=i-1;
            if(testInt[a][b]==-userFlag && !stonesDead.contains(new   Move(a, b)))
            {
                temp = findEatPart(a, b, userFlag);
                if (temp != null)
                {
                    for(n=0; n<temp.size();n++)
                    stonesDead.add(temp.get(n));
                }
            }
            a=i;b=j+1;
            if(testInt[a][b]==-userFlag && !stonesDead.contains(new   Move(a, b)))
            {
                temp = findEatPart(a, b, userFlag);
                if (temp != null)
                {
                    for(n=0; n<temp.size();n++)
                    stonesDead.add(temp.get(n));
                }
            }
            b=j-1;
            if(testInt[a][b]==-userFlag && !stonesDead.contains(new   Move(a, b)))
            {
                temp = findEatPart(a, b, userFlag);
                if (temp != null)
                {
                    for(n=0; n<temp.size();n++)
                    stonesDead.add(temp.get(n));
                }
            }
            return stonesDead;
    }

    private boolean checkAvailable(int i, int j, int userFlag) {
            ArrayList stonesVisited = new ArrayList();
            int findCount;
            int countLiberty=0;
            int begin=0, end=0;
            stonesVisited.add(new Move(i,j));
            testInt[i][j]=userFlag;
            stoneLiberty();
            do
            {
                findCount=0;
                for(int a = begin; a <= end; a++)
                {
                    Move newMove = (Move) stonesVisited.get(a);
                    int newMoveX = newMove.getX();
                    int newMoveY = newMove.getY;
                    if(testInt[newx+1][newy]==userFlag && !stonesVisited.contains(new   Move(newx+1, newy)))
                    {
                        stonesVisited.add(new Move(newx+1, newy));
                        findCount   +=   1;
                    }
                    if(testInt[newx-1][newy]==userFlag && !stonesVisited.contains(new   Move(newx-1, newy)))
                    {
                        stonesVisited.add(new Move(newx-1, newy));
                        findCount   +=   1;
                    }
                    if(testInt[newx][newy+1]==userFlag && !stonesVisited.contains(new   Move(newx, newy+1)))
                    {
                        stonesVisited.add(new   Move(newx,   newy+1));
                        findCount   +=   1;
                    }
                    if(testInt[newx][newy-1]==userFlag && !stonesVisited.contains(new   Move(newx, newy-1)))
                    {
                        stonesVisited.add(new   Move(newx,   newy-1));
                        findCount   +=   1;
                    }
                }
                begin   =   end   +   1;
                end   =   end   +   findCount;
            }
            while (findCount>0);
            for(int count=0;count<stonesVisited.size();count++)
            {
                Move point= (Move) stonesVisited.get(count);
                countLiberty+=liberty[point.x][point.y];
            }
            if(countLiberty!=0)
                return true;
            else
                testInt[i][j]=0;
                return false;
        }
}

