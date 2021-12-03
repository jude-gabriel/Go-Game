package com.example.gogame.GoGame.players.MCTSInfo;

import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;
import com.example.gogame.GoGame.infoMessage.StoneGroup;
import com.example.gogame.GoGame.infoMessage.moveData.Intersection;
import com.example.gogame.GoGame.infoMessage.moveData.Move;
import com.example.gogame.GoGame.infoMessage.moveData.MoveType;
import com.example.gogame.GoGame.players.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Go Player using non-parallel Monte Carlo Tree Search with a default of 10k moves
 *
 * @author calebj
 */
public class MCTS extends Player {

    private static int ITERATIONS_PER_MOVE = 10000;
    private final Random random;

    //Other person's move
    private MCTSNode root;

    public MCTS(Stone.StoneColor color) {
        super(color);
        random = new Random();
    }

    public MCTS(Stone.StoneColor color, Integer iterations) {
        this(color);
        ITERATIONS_PER_MOVE = iterations;
    }

    @Override
    public Move getMove(GoGameState goGS) {

        if (goGS.getLastMoved().equals(color)) {
            //we shouldn't have been called, it's not our turn
            return Move.getMoveInstance(MoveType.SKIP, 0, 0);
        }

        Move lastMove = goGS.getLastMove();
        if (root != null && lastMove != null && !lastMove.getType().equals(MoveType.SKIP)) {
            pruneTree(lastMove, goGS);
        } else {
            root = new MCTSNode(null, null, goGS);
        }


        try {
            Move move = UCT(goGS, ITERATIONS_PER_MOVE);
            pruneTree(move, goGS);
            return move;
        } catch (MoveException e) {
            return Move.getMoveInstance(MoveType.SKIP, 0, 0);
        }
    }

    //performs basic MCTS with Upper Confidence Bound for Trees
    private Move UCT(GoGameState rootstate, int iterationsPerMove) throws MoveException {
        for (int i = 0; i < iterationsPerMove; i++) {
            MCTSNode node = root;
            GoGameState goGS = new GoGameState(rootstate);

            //Select
            while (node.getUntriedMoves().isEmpty() && !node.getChildren().isEmpty()) {
                node = node.UCTSelectChild();
                goGS.addMove(node.getMove());
            }

            //Expand
            if (node.getUntriedMoves() != null) {
                Set<Move> untried = node.getUntriedMoves();
                stripBadMoves(untried, goGS);
                Move m;
                if (untried.isEmpty()) {
                    m = Move.getMoveInstance(MoveType.SKIP, 0, 0);
                } else {
                    List<Move> possibleMoveList = new ArrayList<>(untried);
                    m = possibleMoveList.get(random.nextInt(untried.size()));
                }
                goGS.addMove(m);
                node = node.addChild(m, goGS);
            }

            //Rollout
            Set<Move> possibleMoves = goGS.getPossibleMoves(goGS.getNextToMove().getColor());
            Map<Stone.StoneColor, Boolean> justPassed = new HashMap<Stone.StoneColor, Boolean>();
            justPassed.put(Stone.StoneColor.BLACK, false);
            justPassed.put(Stone.StoneColor.WHITE, false);
            while (!possibleMoves.isEmpty()) {
                Move m = getBestMove(possibleMoves, goGS);
                if (m.equals(Move.getMoveInstance(MoveType.SKIP, 0, 0))) {
                    if (!justPassed.get(goGS.getNextToMove().getColor())) {
                        justPassed.put(goGS.getNextToMove().getColor(), Boolean.TRUE);
                    } else {
                        break;
                    }
                } else {
                    justPassed.put(goGS.getNextToMove().getColor(), Boolean.FALSE);
                }
                goGS.addMove(m);
                possibleMoves = goGS.getPossibleMoves(goGS.getNextToMove().getColor());

            }

            goGS.captureDeadGroups();


            //Backpropogate
            while (node != null) {
                Map<Stone.StoneColor, Integer> score = goGS.getScore();
                Integer myScore = 0;
                Integer enemyScore = 0;
                for (Entry<Stone.StoneColor, Integer> entry : score.entrySet()) {
                    if (entry.getKey().equals(node.getPlayerJustMoved())) {
                        myScore = entry.getValue();
                    } else {
                        enemyScore = entry.getValue();
                    }
                }
                node.update(myScore > enemyScore || (myScore == enemyScore && node.getPlayerJustMoved().equals(Stone.StoneColor.WHITE)));
                node = node.getParent();
            }
        }

        return Collections.max(root.getChildren(), new Comparator<MCTSNode>() {
            @Override
            public int compare(MCTSNode o1, MCTSNode o2) {
                return o1.getVisits().compareTo(o2.getVisits());
            }
        }).getMove();
    }

    private Move getBestMove(Set<Move> possibleMoves, GoGameState goGS) {
        /**
         * This method determines the best move to try based on the following logic
         * (some portions not yet implemented, marked as such (NYI):

         if the last move is an atari, then
         Save the stones which are in atari.
         (NYI)else if there is an empty location among the 8 locations around the last move which matches a pattern then
         (NYI)	Play randomly uniformly in one of these locations.
         (NYI)else if there is a move which captures stones then
         Capture stones.
         else if there is a legal move then
         Play randomly a legal move that doesn't kill your own eyespace
         else
         Return pass.
         end if
         */
        Player currentPlayer = goGS.getNextToMove();
        stripBadMoves(possibleMoves, goGS);
        if (possibleMoves.isEmpty()) {
            return Move.getMoveInstance(MoveType.SKIP, 0, 0);
        }

        //save stones that are in atari
        Move move = goGS.getLastMove(currentPlayer.getColor());
        StoneGroup group = goGS.getStoneGroupAt(move);
        if (group != null && group.getRemainingLiberties() == 1) {
            Intersection i = group.getLiberties().iterator().next();
            move = Move.getMoveInstance(MoveType.PLACE, i.xCord, i.yCord);
            if (possibleMoves.contains(move)) {
                return move;
            }
        }

        //implement pattern matching here at a later time


        //try to kill enemy groups
        Set<StoneGroup> groups = goGS.getGroupsInAtari();
        Set<StoneGroup> enemyGroups = new TreeSet<>(new Comparator<StoneGroup>() {
            @Override
            public int compare(StoneGroup o1, StoneGroup o2) {
                Integer o2Size = o2.getStones().size();
                Integer o1Size = o1.getStones().size();
                return o2Size.compareTo(o1Size);
            }
        });
        for (StoneGroup atariGroup : groups) {
            if (!atariGroup.getOwner().equals(currentPlayer.getColor())) {
                enemyGroups.add(atariGroup);
            }
        }
        int asi = 0;
        for (StoneGroup enemyGroup : enemyGroups) {
            Intersection i = enemyGroup.getLiberties().iterator().next();
            Move m = Move.getMoveInstance(MoveType.PLACE, i.x_location, i.y_location);
            if (possibleMoves.contains(m)) {
                return m;
            }
        }

        //play randomly
        List<Move> moveList = new ArrayList<>(possibleMoves);
        return moveList.get(random.nextInt(possibleMoves.size()));
    }


    //don't try moves that reduce your own eye space
    private void stripBadMoves(Set<Move> possibleMoves, GoGameState goGS) {
        for (Iterator<Move> it = possibleMoves.iterator(); it.hasNext(); ) {
            Move move = it.next();
            if (goGS.isEye(move, goGS.getNextToMove().getColor())) {
                it.remove();
            }
        }
    }

    //lets help the GC out
    private void pruneTree(Move lastMove, GoGameState goGS) {
        MCTSNode newNode = new MCTSNode(lastMove, null, goGS);
        for (MCTSNode node : root.getChildren()) {
            if (node.getMove().equals(lastMove)) {
                newNode = node;
            }
            node.setParent(null);
        }
        if (root != null) {
            root.getChildren().clear();
        }
        root = newNode;
    }

}