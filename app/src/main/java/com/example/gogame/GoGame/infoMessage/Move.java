package com.example.gogame.GoGame.infoMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
	private int row; 									// the x coordinate of the liberty of the move
	private int col; 									// the y coordinate of the liberty of the move
	public static final int MAX_BOARD_SIZE = 9;		// maximum board size
	public static Map<MoveType, Move[][]> moves;	// map the type of moves to the move

	/**
	 * constructor
	 *
	 * initializes the type and coordinates of the move
	 */
	private Move(MoveType moveType, int r, int c) {
		type = moveType;
		row = r;
		col = c;
	}

	/** getMoveInstance
	 *
	 * since making a move is the main action in Go so to optimize running time, all moves are
	 * created initially and passed by reference
	 */
	public static Move getMoveInstance(MoveType moveType, int xCord, int yCord)
	{
		// determine if current set of moves are null
		if (moves == null)
		{
			// set the moves to a new hash map
			moves = new HashMap<>();

			// iterate through the moves' types
			for (MoveType t : MoveType.values())
			{
				// initialize a new array to store all moves
				Move[][] possibleMoves = new Move[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
				
				// iterate through the board an create a new move at the position
				for (int r = 0; r < MAX_BOARD_SIZE; r++)
				{
					for (int c = 0; c < MAX_BOARD_SIZE; c++) {
						possibleMoves[r][c] = new Move(t, r, c);
					}
				}

				// store the moves and types in the hash map
				moves.put(t, possibleMoves);
			}
		}
		// return the current, non-null move
		return Objects.requireNonNull(moves.get(moveType))[xCord][yCord];
	}

	/**
	 * hashCode
	 *
	 * overrides the hash code for the current game board parameter
	 */
	@Override
	public int hashCode() {return (row * MAX_BOARD_SIZE) + col;}

	/**
	 * equals
	 *
	 * overrides the equality operator
	 */
	@Override
	public boolean equals(Object obj) {return this == obj;}

	/**
	 * toString
	 *
	 * overrides the hash code for the current game board parameter
	 */
	@Override
	public String toString() {return String.format("(" + row + "," + col + ")");}

	/** getters for instance variables **/
	public MoveType getType() { return type; }
	public int getRow() { return row; }
	public int getCol() { return col; }
}//Move
