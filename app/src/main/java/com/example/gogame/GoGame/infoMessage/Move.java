package com.example.gogame.GoGame.infoMessage;

/**
 * Move
 *
 * a valid Go move
 *
 * @author Brynn Harrington
 */
class Move
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
	public static getMoveInstance(MoveType moveType, int xCord, int yCord)
	{

	}
}
