package com.example.gogame.GoGame.players;

import com.example.gogame.GoGame.infoMessage.GoGameState;
import com.example.gogame.GoGame.infoMessage.Stone;
import com.example.gogame.GoGame.infoMessage.moveData.Move;

public abstract class Player {

	Stone.StoneColor color;
	public Player(Stone.StoneColor color) { this.color = color; }
	public abstract Move getMove(GoGameState goGS);
	public Stone.StoneColor getColor() { return color; }
	public void setColor(Stone.StoneColor color) { this.color = color; }
	
}
