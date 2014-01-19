package net.timothyhahn.multipong;

public class State {
	// Event Types
	public static final int FULL = 0;
	public static final int MOVE = 1;
	
	// Public since I'm basically using this as a slightly more organized map
	
	public int lPoints, rPoints, lY, rY, bX, bY, lVY, rVY, bVX, bVY, type;
	
	public State(){
		lPoints = 0;
		rPoints = 0;
		lY = 0;
		rY = 0;
		bX = 0;
		bY = 0;
		lVY = 0;
		rVY = 0;
		bVX = 0;
		bVY = 0;
		type = -1;
	}
	
	public void printState(){
		System.out.println(" lPoints: " + lPoints + " rPoints: " + rPoints + " lY: " + lY + " rY: " + rY + " bX: " + bX + " bY: " + bY);
	}
}
