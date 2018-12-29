package com.segludian.digitaltennis;

public class HumanController extends Controller {
	
	private int direction;
	public int DIRECTION_UP = -1;
	public int DIRECTION_DOWN = 1;
	public int DIRECTION_NONE = 0;

	/**
	 * Constructor
	 */
	public HumanController() {
		this.direction = DIRECTION_NONE;
	}
	
	/**
	 * Allows you to check and see if a player is human or computer controlled.
	 * @return: True if human which for this class is always true.
	 */
	@Override
	public boolean isHuman() {
		return true;
	}
	
	/**
	 * Event when an UP key is pressed.
	 */
	public void Up_Pressed() {
		this.direction = DIRECTION_UP;
	}
	
	/**
	 * Event when an UP key is released.
	 */	
	public void Up_Released() {
		this.direction = DIRECTION_NONE;
	}
	
	/** 
	 * Event when a DOWN key is pressed.
	 */
	public void Down_Pressed() { 
		this.direction = DIRECTION_DOWN;
	}
	
	/**
	 * Event when a DOWN key is released.
	 */
	public void Down_Released() { 
		this.direction = DIRECTION_NONE;
	}
	
	/**
	 * Update the location of the paddle in response to user input.
	 * @param player - Player object we are updating.
	 * @param ball - ball object to track.
	 * @param dt - Milliseconds since the last frame.
	 */
	@Override
	public void update(Player player, Ball ball, double dt) {
		if (direction != DIRECTION_NONE)
		{
			double deltaY;
			deltaY = (player.getSpeed() * direction * dt) / 1000.0;
			player.setY(player.getY() + deltaY);
		}
	}
	

}
