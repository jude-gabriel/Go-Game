package com.example.gogame.GoGame.infoMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Move
 *
 * a valid Go move
 *
 * @author Brynn Harrington
 */
public class Move
{
	/* INSTANCE VARIABLES */
	private MoveType type;							// instance of type of move
	private int x; 									// the x coordinate of the liberty of the move
	private int y; 									// the y coordinate of the liberty of the move
	public static final int MAX_BOARD_SIZE = 9;		// maximum board size
	public static Map<MoveType, Move[][]> moves;	// map the type of moves to the move

	/**
	 * constructor
	 *
	 * initializes the type and coordinates of the move
	 */
	private Move(MoveType moveType, int xCord, int yCord) {
		type = moveType;
		x = xCord;
		y = yCord;
	}

	/** getMoveInstance
	 *
	 * since making a move is the main action in Go so to optimize running time, all moves are
	 * created initially and passed by reference
	 */
	public static Move getMoveInstance(MoveType moveType, int x, int y)
	{
		if (moves == null){
					moves = new HashMap<>();
					for (MoveType t : MoveType.values()){
						Move[][] moveArray = new Move[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
						for (int i = 0; i<MAX_BOARD_SIZE; i++){
							for (int j = 0; j<MAX_BOARD_SIZE; j++){
								moveArray[i][j] = new Move(t, i, j);
							}
						}
						moves.put(t, moveArray);
					}
				}
				return moves.get(moveType)[x][y];
	}
}
