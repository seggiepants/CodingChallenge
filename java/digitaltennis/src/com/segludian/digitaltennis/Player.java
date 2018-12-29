package com.segludian.digitaltennis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player {
	// score: The player's current score.
	// x: The X Coordinate of the top left of the player's racket.
	// y: The Y Coordinate of the top left of the player's racket.
	// width: The width of the player's racket.
	// height: The height of the player's racket.
	// Rectangle: Used for collision detection.
	private int score;
	private double x;
	private double y;
	private Rectangle rectangle;
	private Controller controller;
	private final static int width = 8;
	private final static int height = 64;
	private final Color racketColor = Color.white;
	private final double racketSpeed = 200.0; // pixels per second.

	/**
	 * Constructor
	 * @param controller: Controller class that updates the player's racket position.
	 */
	public Player(Controller controller) {
		this.controller = controller;
		this.rectangle = new Rectangle();
		this.rectangle.width = Player.width;	// These don't change so don't bother updating them.
		this.rectangle.height = Player.height;
	}
	
	// Getters and Setters:
	/**
	 * Get the player's score
	 * @return: The current score for this player.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Get a rectangle object with the players postion and size.
	 * @return - Rectangle object with the player's position and size.
	 */
	public Rectangle getRectangle() {
		this.rectangle.x = (int) this.x; // Update with the current position.
		this.rectangle.y = (int) this.y;
		return this.rectangle;
	}
	
	/**
	 * Get the width of a racket object.
	 * @return: racket Width
	 */
	public static int getWidth() {
		return Player.width;
	}
	
	/**
	 * Get the height of a racket object.
	 * @return: racket Height
	 */
	public static int getHeight() {
		return Player.height;
	}
	
	/**
	 * Called when we start a new game.
	 * to initialize the state.
	 * @param x - X-Coordinate to move to at start.
	 * @param y - Y-Coordinate to move to at start.
	 */
	public void newGame(double x, double y)
	{
		this.x = x;
		this.y = y;
		this.score = 0;
	}
	
	/**
	 * Called when the player gets a point.
	 */
	public void addPoint()
	{
		this.score++;
	}
	
	/**
	 * Helper function to reset the players location if they go off screen.
	 * Intended to be called from a resize event.
	 * @param defaultX - The x-coordinate they should use.
	 * @param height - The current height of the window.
	 */
	public void clipXY(double defaultX, int height) {
		
		this.x = defaultX;
		
		if (this.y < 0 || this.y + Player.height >= height) {
			this.y = (height - Player.height) / 2.0;
		}
		
	}
	
	/**
	 * Draw the player's racket.
	 * @param g - a Graphics2D object to draw with.
	 */
	public void draw(Graphics2D g) {
		g.setColor(this.racketColor);
		g.fillRect((int) this.x,  (int)this.y,  Player.width,  Player.height);
		
	}
	
	/**
	 * Update the player's racket position.
	 * @param ball: Ball to keep track of.
	 * @param dt : Number of milliseconds elapsed for the previous frame.
	 */
	public void update(Ball ball, double dt) {
		// Pass it off to the controller to handle.
		this.controller.update(this, ball,  dt);
	}
	
	/**
	 * Return true/false depending on if this player is human or computer controlled.
	 * @return True if human controlled, false if computer controlled.
	 */
	public boolean isHuman() {
		return this.controller.isHuman();
	}
	
	/**
	 * Return the speed the racket moves. The controller needs this to update position (maybe I should refactor).
	 * @return - Racket's movement speed in pixels per second.
	 */
	public double getSpeed() {
		return this.racketSpeed;
	}
	
	/**
	 * Get the racket's x-coordinate for the top left corner.
	 * @return - The racket's x-coordinate for the top left corner.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Set the racket's x-coordinate for the top left corner.
	 * @param x - new x-coordinate.
	 */
	public void setX(double x) { 
		this.x = x;
	}
	
	/**
	 * Get the racket's y-coordinate for the top left corner.
	 * @return - The racket's y-coordinate for the top left corner.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Set the racket's y-coordinate for the top left corner.
	 * @param y - new y-coordinate.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * return a reference to this player's controller object.
	 * @return - The player's controller object.
	 */
	public Controller getController() {
		return this.controller;
	}
}
