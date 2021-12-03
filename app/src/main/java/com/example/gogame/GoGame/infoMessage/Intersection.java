package com.example.gogame.GoGame.infoMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Intersection
 *
 * determines where intersections exist on the current game board
 * */
public class Intersection {
    /* INSTANCE VARIABLES */
	private Stone occupant; 	// color is none if the intersection is empty
	private List<Intersection> neighbors;   // determine neighboring stones
	public int xCord;      // determine the x coordinate
	public int yCord;


	public Intersection (int x, int y){
		xCord = x;
		yCord = y;
	}

	public Stone getOccupant() {
		return occupant;
	}

	public void setOccupant(Stone occupant) {
		this.occupant = occupant;
	}

	public boolean isOccupied() {
		return this.occupant != null;
	}

	public List<Intersection> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<Intersection> neighbors) {
		this.neighbors = neighbors;
	}

	public void addNeighbor(Intersection neighbor){
		if (neighbors == null){
			neighbors = new ArrayList<>();
		}
		neighbors.add(neighbor);
	}

	@Override
	public String toString() {
		String occupantString = occupant == null ? "empty" : occupant.getOwner().toString();

		return "Intersection [occupant=" + occupantString + ", xCord=" + xCord + ", yCord=" + yCord + "]";
	}


}