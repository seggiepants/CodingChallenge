package com.segludian.digitaltennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Ball {
	// x: - x - coordinate of the ball.
	// y: - y - coordinate of the ball.
	// angle: direction the ball is moving
	// speed: speed the ball is moving in pixels per second.
	// radius: radius of the circle used to draw the ball.
	// rectangle: Used for collision detection.
	private final int radius = 8;
	private final double defaultBallSpeed = 200.0;
	private final double maximumBallSpeed = 500.0;
	private double x, y, angle, speed;
	private Rectangle rectangle;
	private Color ballColor;
	
	/**
	 * Ball constructor.
	 * @param x - Initial X-Coordinate of the ball.
	 * @param y - Initial Y-Coordinate of the ball.
	 */
	public Ball(double x, double y) {
		// Initialize ball to default state. The program should pass in the starting position.
		// I assume we are paused so no speed and the direction of travel is straight up.
		this.x = x;
		this.y = y;
		this.angle = Math.PI;
		this.speed = 0.0;
		this.ballColor = Color.WHITE;
		this.rectangle = new Rectangle();
		this.rectangle.width = this.radius * 2;
		this.rectangle.height = this.radius * 2;
	}
	
	// Getters and Setters:	
	/**
	 * 
	 * @return: Current X - Coordinate of the ball.
	 */
	public double getX() {
		return this.x;		
	}
	
	/**
	 * 
	 * @param x : New value for the X - Coordinate of the ball.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * 
	 * @return: Current Y - Coordinate of the ball.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * 
	 * @param y: New value for the Y - Coordinate of the ball.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * 
	 * @return: Current direction of travel for the ball. 
	 */
	public double getAngle() {
		return this.angle;
	}
	
	/**
	 * 
	 * @param speed: New value for speed in pixels per second.
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	/**
	 * 
	 * @return: current ball speed.
	 */
	public double getSpeed() {
		return this.speed;
	}
	
	/**
	 * 
	 * @return: The default speed of the ball when served.
	 */
	public double getDefaultSpeed() {
		return this.defaultBallSpeed;
	}
	
	/**
	 * 
	 * @param angle: New value for the balls direction of travel.
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * Return a rectangle of the balls bounds.
	 * @return - Rectangle with computed ball bounds.
	 */
	public Rectangle getRectangle() {
		this.rectangle.x = (int) this.x - this.radius; // Update with the current position.
		this.rectangle.y = (int) this.y - this.radius; // Width/Height doesn't change.
		return this.rectangle;
	}
	
	// Update and Draw methods. 
	
	/**
	 * 
	 * @param dt: delta time, time elapsed since the last frame in milliseconds.
	 */
	public void update(double dt) {
		this.x += ((this.speed * dt) / 1000.0) * Math.cos(this.angle);
		this.y += ((this.speed * dt) / 1000.0) * Math.sin(this.angle);
	}
	
	/**
	 * 
	 * @param g: Graphics context to draw with.
	 */
	public void draw(Graphics2D g) {
		g.setColor(ballColor);
		g.fillOval((int)this.x - this.radius, (int)this.y - this.radius, (int) this.radius * 2, (int) this.radius * 2);
	}
	
	/**
	 * Give an angle return it normalized to a 0 to 2 PI range.
	 * @param angle angle to normalize.
	 * @return Normalized angle.
	 */
	private double normalizeAngle(double angle) {
		while (angle >= (2.0 * Math.PI)) {
			angle -= (2.0 * Math.PI);
		}
		
		while (angle < 0.0) {
			angle += (2.0 * Math.PI);			
		}		
		return angle;
	}
	
	/**
	 * This code largely lifted from:  https://ericleong.me/research/circle-line/
	 * Check if the ball intersects with a line segment.
	 * @param x1 - start x-coordinate of the line segment.
	 * @param y1 - start y-coordinate of the line segment.
	 * @param x2 - end x-coordinate of the line segment.
	 * @param y2 - end y-coordinate of the line segment.
	 * @return True if on the line or less than radius units away from it.
	 */
	private boolean intersectsLineSegment(double x1, double y1, double x2, double y2) {
		double A1 = y2 - y1;
		double B1 = x1 - x2; 
		double C1 = (y2 - y1)*x1 + (x1 - x2)*y1;
		double C2 = -B1*this.x + A1*this.y; 
		double det = A1*A1 - -B1*B1; 
		double cx = 0; 
		double cy = 0;
		
		if(det != 0){ 
			cx = ((A1*C1 - B1*C2)/det);
			cy = ((A1*C2 - -B1*C1)/det);
			return (((cx - this.x) * (cx - this.x)) + ((cy - this.y) * (cy - this.y))) <= (this.radius * this.radius);
		}
		else { 
			// Center of circle lies on the line.
			return true;
		}  
	}
	
	/**
	 * Check to see if the ball intersects any of the sides of a rectangle and set it to change direction
	 * if it does.
	 * @param r - Rectangle object to check against. Usually a racket's position and dimensions.
	 * @return True if the rectangled intersect, false otherwise.
	 */
	public boolean intersectsRectangle(Rectangle r) {
		final double delta = (11.0 * Math.PI) / 180.0;
		boolean hit = false;
		// If we bounce I want a bit of randomness so things don't get caught in an endless loop.
		double errorTerm = (Math.random() * delta) - (delta / 2.0);
		
		//Check the left.
		if (!hit) {
			if (intersectsLineSegment((double) r.x, (double) r.y, (double) r.x, (double) (r.y + r.height))) {
				hit = true;
				this.angle = normalizeAngle(reflectHorizontal(this.angle)+ errorTerm);
				this.setX(r.x - this.radius - 1);
			}
		}
		
		//Check the right.
		if (!hit) {
			if (intersectsLineSegment((double) (r.x + r.width), (double) r.y, (double) (r.x + r.width), (double) (r.y + r.height))) {
				hit = true;
				this.angle = normalizeAngle(reflectHorizontal(this.angle)+ errorTerm);
				this.setX(r.x + r.width + this.radius + 1);
			}
		}

		//Check the top.
		if (!hit) {
			if (intersectsLineSegment((double) r.x, (double) r.y, (double)(r.x + r.width), (double) r.y)) {
				hit = true;
				this.angle = normalizeAngle(reflectVertical(this.angle)+ errorTerm);
				this.y = r.y - this.radius - 1;
			}
		}
		
		//Check the bottom.
		if (!hit) {
			if (intersectsLineSegment((double) r.x, (double) (r.y + r.height), (double)(r.x + r.width), (double) (r.y + r.height))) {
				hit = true;
				this.angle = normalizeAngle(reflectVertical(this.angle)+ errorTerm);
				this.y = r.y + r.height + this.radius + 1;
			}
		}

		if (hit) {
			// When the ball is hit, make it move faster up to maximum speed.
			this.speed = Math.min(this.speed * 1.1,  this.maximumBallSpeed);
		}
		
		return hit;		
	}
	
	/**
	 * Flip the given angle vertically.
	 * @param angle - angle to flip
	 * @return - Angle rotated to point in the opposite vertical direction.
	 */
	private double reflectVertical(double angle) {
		return ((2.0 * Math.PI)- angle);
	}
	
	/**
	 * Flip the given angle horizontally.
	 * @param angle - angle to flip
	 * @return - Angle rotated to point in the opposite horizontal direction.
	 */
	private double reflectHorizontal(double angle) {
		if (angle >= Math.PI) {
			return ((2.0 * Math.PI)- (angle % Math.PI)); //) + Math.PI; 
		}
		else {
			return (Math.PI - angle);
		}
		
	}
	
}
