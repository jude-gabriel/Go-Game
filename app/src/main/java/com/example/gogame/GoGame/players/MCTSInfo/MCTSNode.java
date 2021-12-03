package com.example.gogame.GoGame.players.MCTSInfo;

import androidx.annotation.NonNull;

import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;
import com.example.gogame.GoGame.infoMessage.moveData.Move;
import com.example.gogame.GoGame.infoMessage.moveData.MoveType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * MCTSNode
 * <p>
 * an object representation of a node for the Monte-Carlo tree search
 *
 * @author Brynn Harrington
 */
class MCTSNode {
    private static final double epsilon = 1e-6;
    private static final Random random = new Random();
    private final Set<Move> untriedMoves;
    private Move move;
    private List<MCTSNode> children;
    private MCTSNode parent;
    private double wins;
    private double visits;
    private Stone.StoneColor playerJustMoved;
    private final GoGameState goGS;


    public MCTSNode(Move move, MCTSNode parent, GoGameState goGS) {
        this.move = move;
        this.parent = parent;
        wins = 0;
        visits = 0;
        int[] mostRecentMove = goGS.getMostRecentMove();
        int x = mostRecentMove[0];
        int y = mostRecentMove[1];
        Stone[][] board = goGS.getGameBoard();
        playerJustMoved = board[x][y].getStoneColor();
        untriedMoves = goGS.getPossibleMoves(Stone.StoneColor.BLACK.equals(playerJustMoved) ? Stone.StoneColor.WHITE : Stone.StoneColor.BLACK);
        children = new ArrayList<>();
        this.goGS = goGS;
    }

    /**
     * Select the best child based on Upper Confidence Bound. This balances exploration (nodes not
     * traveled very often) and exploitation (nodes that are known to have a high win ratio)
     *
     * @return The optimal node to playout
     */
    public MCTSNode UCTSelectChild() {
        MCTSNode selected = null;
        double best = -1;
        for (MCTSNode node : children) {
            if (node.getMove().getType().equals(MoveType.SKIP) && selected == null) selected = node;
            else {
                //disincentivize playing on the edges
                double ratio;
                if (node.getMove().getRow() == 0 ||
                        node.getMove().getCol() == 0 ||
                        node.getMove().getRow() == goGS.getBoardSize() - 1 ||
                        node.getMove().getCol() == goGS.getBoardSize() - 1)
                    ratio = Math.max(0, (node.wins - 6.1) / (epsilon + node.visits));
                else ratio = node.wins / (epsilon + node.visits);
                double V = Math.max(.001, ratio * (1 - ratio));
                V = 1;
                double uctValue =
                        ratio + Math.sqrt(V * Math.log(this.visits + 1) / (epsilon + node.visits)) + random.nextDouble() * epsilon;
                if (uctValue > best) {
                    selected = node;
                    best = uctValue;
                }
            }
        }
        return selected;
    }

    /**
     * Transfer the node representing the move [that was just played] from the untried bucket to the tried bucket
     *
     * @param move  the move that was just played
     * @param state the current state of the game (with move just played)
     * @return the child node
     */
    public MCTSNode addChild(Move move) {
        untriedMoves.remove(move);
        MCTSNode node = new MCTSNode(move, this, goGS);
        children.add(node);
        return node;
    }

    public void update(Boolean wonResult) {
        visits += 1;
        wins += wonResult ? 1 : 0;
    }

    public Move getMove() { return move; }
    public void setMove(Move move) { this.move = move; }
    public List<MCTSNode> getChildren() { return children; }
    public void setChildren(List<MCTSNode> children) { this.children = children; }
    public MCTSNode getParent() { return parent; }
    public void setParent(MCTSNode parent) { this.parent = parent; }
    public double getWins() { return wins; }
    public void setWins(double wins) { this.wins = wins; }
    public Double getVisits() { return visits; }
    public void setVisits(double visits) { this.visits = visits; }
    public Stone.StoneColor getPlayerJustMoved() { return playerJustMoved; }
    public void setPlayerJustMoved(Stone.StoneColor playerJustMoved) { this.playerJustMoved = playerJustMoved; }
    public Set<Move> getUntriedMoves() { return untriedMoves;}

    @NonNull
    @Override
    public String toString() {
        return "MCTSNode [move=" + move + ", wins=" + wins + ", visits="
                + visits + "]";
    }

}//MCTSNode
