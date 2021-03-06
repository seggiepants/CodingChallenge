package com.segludian.digitaltennis;

public class Controller {

	/**
	 * Constructor
	 */
	Controller() {
		// Let the derived classes set something up.
	}
	
	/**
	 * Update the position of the given player's racket. 
	 * @param player: Player object to update.
	 * @param ball: Ball object to track.
	 * @param dt: Milliseconds passes since the previous frame.
	 */
	public void update(Player player, Ball ball, double dt) {
		// To be overridden by the derived classes.
	}
	
	/**
	 * Allows you to check if the player object is being run by a computer or by a human.
	 * @return True if human controlled, false if computer controlled.
	 */
	public boolean isHuman() {
		// To be overridden by the derived classes.
		return false;
	}
}
