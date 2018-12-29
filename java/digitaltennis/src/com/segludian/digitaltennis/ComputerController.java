package com.segludian.digitaltennis;

public class ComputerController extends Controller {

	@Override
	/**
	 * Return true if this is a human playing, false if a computer.
	 */
	public boolean isHuman() {
		return false;
	}

	/**
	 * update the players position.
	 * @param player - The player object we are operating on.
	 * @param ball - The ball object we are tracking.
	 * @param dt - Time in milliseconds since the previous frame.
	 */
	@Override
	public void update(Player player, Ball ball, double dt) {
		// If the top of the paddle is lower than the ball center move up.
		// If it is higher than the ball center, move down.
		int direction = 0;
		if (player.getY() > ball.getY()) {
			direction = -1;
		}
		else if (player.getY() + Player.getHeight() < ball.getY()) {
			direction = 1;
		}
		double deltaY;
		deltaY = (player.getSpeed() * direction * dt) / 1000.0;
		player.setY(player.getY() + deltaY);
	}
	
}
