package com.segludian.digitaltennis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class GameWorld extends JPanel implements ComponentListener {
	
	private static final long serialVersionUID = 1L;
	
	private final int STATE_SERVE = 0;
	private final int STATE_PLAY = 1;
	private final int STATE_PAUSE = 2;
	private final int STATE_GAME_OVER = 3;
	
	private final int TARGET_SCORE = 5;
	private final double MESSAGE_SPEED = 75.0; // Speed of message in pixels per second.
	
	private Ball ball;
	private Player playerLeft;
	private Player playerRight;
	private final int fontDistance = 64;
	private final int wallDistance = 32;
	private final int wallSize = 8;
	private Rectangle topWall;
	private Rectangle bottomWall;
	private Rectangle leftOutZone;
	private Rectangle rightOutZone;
	private Color wallColor;
	private Font font;
	private int state = STATE_SERVE;
	private int messageY = 0;
	private String message = "";
	Main parent;
	private boolean running = false;
	private boolean ready = false;
	private boolean showPlayAgain = false;
	private boolean showQuit = true;

	/**
	 * Object to represent the game world.
	 * @param parent - The parent object holding this component. This will be a Main object.
	 * @param left - The controller for the left player object.
	 * @param right - The controller for the right player object.
	 */
	GameWorld(Main parent, Controller left, Controller right) {
		this.ready = false;
		this.parent = parent;
		this.addComponentListener(this);
		
		wallColor = Color.WHITE;
		playerLeft = new Player(left);
		playerRight = new Player(right);
		ball = new Ball(0, 0);
		font = new Font("SansSerif", Font.BOLD, 21);
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, false), PAUSE);
        this.getActionMap().put(PAUSE, pause);
        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), QUIT);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), QUIT);
        this.getActionMap().put(QUIT, quit);
		
        if (left.isHuman()) {
	        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), P1_UP);
	        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), P1_UP_RELEASED);
	        if (!right.isHuman()) {
	        	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), P1_UP);
	        	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), P1_UP_RELEASED);
	        }
	        this.getActionMap().put(P1_UP, p1_up);
	        this.getActionMap().put(P1_UP_RELEASED, p1_up_released);
	
	        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), P1_DOWN);
	        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), P1_DOWN_RELEASED);
	        if (!right.isHuman()) {
	        	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), P1_DOWN);
	        	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), P1_DOWN_RELEASED);
	        }
	        this.getActionMap().put(P1_DOWN, p1_down);
	        this.getActionMap().put(P1_DOWN_RELEASED, p1_down_released);
        }
        
        if (right.isHuman()) {
	        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), P2_UP);
	        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), P2_UP_RELEASED);
	        if (!left.isHuman()) {
	        	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), P2_UP);
	        	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), P2_UP_RELEASED);
	        }
	        this.getActionMap().put(P2_UP, p2_up);
	        this.getActionMap().put(P2_UP_RELEASED, p2_up_released);
	
	        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), P2_DOWN);
	        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), P2_DOWN_RELEASED);
	        if (!left.isHuman()) {
	        	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), P2_DOWN);
	        	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), P2_DOWN_RELEASED);
	        }
	        this.getActionMap().put(P2_DOWN, p2_down);
	        this.getActionMap().put(P2_DOWN_RELEASED, p2_down_released);
        }
		
		this.setVisible(true);
	}
	
	/*
	 * A whole lot of keyboard handling that doesn't work all that well.
	 */
    private static final String P1_UP = "P1_Up";
    private Action p1_up = new AbstractAction(P1_UP) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
            ((HumanController) playerLeft.getController()).Up_Pressed();
        }
    };
    
    private static final String P1_UP_RELEASED = "P1_Up_Released";
    private Action p1_up_released = new AbstractAction(P1_UP_RELEASED) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
			((HumanController) playerLeft.getController()).Up_Released();
        }
    };

    
    private static final String P1_DOWN = "P1_Down";
    private Action p1_down = new AbstractAction(P1_DOWN) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
			((HumanController) playerLeft.getController()).Down_Pressed();
        }
    };

    private static final String P1_DOWN_RELEASED = "P1_Down_Released";
    private Action p1_down_released = new AbstractAction(P1_DOWN_RELEASED) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
			((HumanController) playerLeft.getController()).Down_Released();
        }
    };

    
    private static final String P2_UP = "P2_Up";
    private Action p2_up = new AbstractAction(P2_UP) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
			((HumanController) playerRight.getController()).Up_Pressed();
        }
    };

    private static final String P2_UP_RELEASED = "P2_Up_Released";
    private Action p2_up_released = new AbstractAction(P2_UP_RELEASED) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
			((HumanController) playerRight.getController()).Up_Released();
        }
    };

    
    private static final String P2_DOWN = "P2_Down";
    private Action p2_down = new AbstractAction(P2_DOWN) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
			((HumanController) playerRight.getController()).Down_Pressed();
        }
    };


    private static final String P2_DOWN_RELEASED = "P2_Down_Released";
    private Action p2_down_released = new AbstractAction(P2_DOWN) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
			((HumanController) playerRight.getController()).Down_Released();
        }
    };

    private static final String PAUSE = "Pause";
    private Action pause = new AbstractAction(PAUSE) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
            if ((state == STATE_PLAY) || (state == STATE_PAUSE)) {
            	statePause();
            }
        }
    };

    private static final String QUIT = "Quit";
    private Action quit = new AbstractAction(QUIT) {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
            if (showQuit) {
            	showQuit = false; // Flag so we don't display an arbitrary number of quit prompts.
            	if (JOptionPane.showConfirmDialog(parent, "Exit to main menu?", "Digital Tennis", JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					// Return to the main menu.
            		running = false;
					parent.menuEvent("mainMenu");
				}            	
            	showQuit = true;
            }
        }
    };


    /**
     * Start the game once the control has loaded.
     */
	public void start() {
		this.ready = true;
		resizeWorld();
		newGame();
		runGameLoop();				
	}
	
	/**
	 * Start up a new game. Didn't quite qualify as a state since we just go directly to serve.
	 */
	public void newGame() {		
		playerLeft.newGame(this.getLeftPlayerX(), this.getPlayerY());
		playerRight.newGame(this.getRightPlayerX(), this.getPlayerY());
		stateServe();
	}
	
	/** 
	 * Switch to the game over state.
	 */
	public void stateGameOver() {
		// Don't switch states unless someone won.
		if (playerLeft.getScore() == this.TARGET_SCORE || playerRight.getScore() == this.TARGET_SCORE) {
			state = STATE_GAME_OVER;
			if (playerLeft.isHuman() == playerRight.isHuman())
			{
				if (playerLeft.getScore() == this.TARGET_SCORE) {
					message = "Player 1 Wins!";
				}
				else {
					message = "Player 2 Wins!";
				}
			}
			else {
				if (playerLeft.getScore() == this.TARGET_SCORE) {
					message = "You Win!";
				}
				else {
					message = "Sorry, you lost.";
				}
			}
			this.messageY = 0;
		}
		showPlayAgain = true;
	}
	
	/**
	 * Switch to/from the pause state.
	 */
	public void statePause() {
		if (state == STATE_PAUSE) {
			state = STATE_PLAY;
		}
		else {
			state = STATE_PAUSE;
			messageY = this.getHeight() / 2;
			message = "Paused - Press \"P\" to resume";
		}
	}
	
	/**
	 * Switch to the Play state.
	 */
	public void statePlay() {
		double newAngle;
		int quadrant;

		state = STATE_PLAY;
		// Want a random angle between -30 and + 30 degrees or 150 to 210 degrees, but not exactly 0, or 180 degrees.
		// I will make 4 areas 1-30, 330-359, 150-179, and 180-210
		newAngle = (Math.random() * Math.PI / 6) + .001;
		quadrant = (int)(Math.random() * 4);
		switch(quadrant)
		{
		case 0:
			// No change, already good.
			break;
		case 1:
			newAngle = Math.PI - newAngle;
			break;
		case 2:
			newAngle = Math.PI + newAngle;
			break;
		case 3:
			newAngle = (2.0 * Math.PI) - newAngle;
			break;
		}
		ball.setAngle(newAngle);
		ball.setSpeed(ball.getDefaultSpeed());
	}
	
	/**
	 * Switch to the serve the ball state.
	 */
	public void stateServe() {
		int width = this.getWidth();		
		state = STATE_SERVE;
		ball.setX((double) width / 2);
		ball.setY(0);
		ball.setAngle(Math.PI/2.0); // Down.
		ball.setSpeed(ball.getDefaultSpeed()); // Fall down.
	}

	
	// Update and Draw commands.
	/**
	 * Draw the game world.
	 * @param g: Graphics rendering context.
	 */
	public void draw(Graphics2D g) {
		
		int width = this.getWidth();
		int height = this.getHeight();
		int centerX = width / 2;		
		String temp;
		
		// Clear the window
		g.setColor(Color.BLACK);;
		g.fillRect(0,  0,  width,  height);
				
		
		// Draw the borders.
		g.setColor(wallColor);;
		g.fillRect(topWall.x,  topWall.y,  topWall.width,  topWall.height);
		g.fillRect(bottomWall.x,  bottomWall.y,  bottomWall.width,  bottomWall.height);
		
		// Draw a dashed line down the center. 
		g.setColor(Color.WHITE);
		for(int y = 0; y < height; y+= 16) {
			g.drawLine(centerX, y, centerX, y + 8);
		}
		
		// Draw the score.
		// Left Player
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(String.format("%01d",  playerLeft.getScore()), fontDistance, fontDistance);
		
		// Right Player.
		temp = String.format("%01d",  playerRight.getScore());
		g.drawString(temp,  width - g.getFontMetrics().stringWidth(temp) - fontDistance, fontDistance);
		
		// Draw the active objects
		playerLeft.draw(g);
		playerRight.draw(g);
		ball.draw(g);
		
		if ((this.state == STATE_GAME_OVER) || (this.state == STATE_PAUSE)) {
			int messageX = (width - g.getFontMetrics().stringWidth(message)) / 2;
			g.drawString(this.message, messageX, this.messageY);
		}
	}
	
	/**
	 * Update the game world.
	 * @param dt: Milliseconds elapsed since the previous frame.
	 */
	public void update(double dt) {
		
		Rectangle ballRect;
		boolean hit = false;

		if (state == STATE_PAUSE) {
			// Do Nothing
		}
		else if (state == STATE_PLAY) {
			// Update the game objects.
			playerLeft.update(ball, dt);
			
			// Sanity Checks.
			if (playerLeft.getY() < topWall.y + topWall.height) {
				playerLeft.setY(topWall.y + topWall.height);
			}
			
			if (playerLeft.getY() + Player.getHeight() > bottomWall.y) {
				playerLeft.setY(bottomWall.y - Player.getHeight());
			}
			
			
			playerRight.update(ball, dt);
			// Sanity Checks.
			if (playerRight.getY() < topWall.y + topWall.height) {
				playerRight.setY(topWall.y + topWall.height);
			}
			
			if (playerRight.getY() + Player.getHeight() > bottomWall.y) {
				playerRight.setY(bottomWall.y - Player.getHeight());
			}
			
			ball.update(dt);
			
			ballRect = ball.getRectangle();
			
			if (ballRect.intersects(leftOutZone)) {
				playerRight.addPoint();
				stateServe();
			}
			
			if (ballRect.intersects(rightOutZone)) {
				playerLeft.addPoint();
				stateServe();
			}

			if (ballRect.intersects(topWall)) {
				hit = ball.intersectsRectangle(topWall);
			}
			
			if (!hit) {
				if (ballRect.intersects(bottomWall)) {
					hit = ball.intersectsRectangle(bottomWall);
				}
			}
			
			if (!hit) {
				if (ballRect.intersects(playerLeft.getRectangle())) {
					hit = ball.intersectsRectangle(playerLeft.getRectangle());
				}
			}
			

			if (!hit) {
				if (ballRect.intersects(playerRight.getRectangle())) {
					hit = ball.intersectsRectangle(playerRight.getRectangle());
				}
			}
			
			if ((playerLeft.getScore() >= TARGET_SCORE) || (playerRight.getScore() >= TARGET_SCORE)) {
				stateGameOver();
			}
			
		}
		else if (state == STATE_SERVE) {
			// Move the ball down to the center then switch to play mode.
			double targetY = this.getHeight() / 2;
			if (ball.getY() >= targetY) {
				ball.setY(targetY);
				statePlay();
			}
			else {
				ball.update(dt);
			}
		}
		else if (state == STATE_GAME_OVER) {
			int targetY = (this.getHeight() / 2);
			if (messageY >= targetY) {
				messageY = targetY;
				if (showPlayAgain) {
					showPlayAgain = false;
					if (JOptionPane.showConfirmDialog(parent, "Game Over!\r\nWould you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						// Start a new game.
						newGame();
					}
					else {
						// Return to the main menu.
						this.running = false;
						parent.menuEvent("mainMenu");
					}
				}
			}
			else {
				messageY += Math.max(1, (int)((dt * MESSAGE_SPEED) / 1000.0));
			}
		}
		
	}
	
	/**
	 * Get the default x-coordinate of the left player.
	 * @return - Default x coordinate of the left player.
	 */
	private double getLeftPlayerX()
	{
		return wallDistance; 
	}
	
	/**
	 * Get the default x-coordinate of the right player.
	 * @return - Default x coordinate of the right player.
	 */
	private double getRightPlayerX() {
		return (double) this.getWidth() - wallDistance - Player.getWidth();
	}
	
	/**
	 * Get the default y-coordinate of either player.
	 * @return - Y - coordinate in the center of the screen.
	 */
	private double getPlayerY() {
		return (double) (this.getHeight() - Player.getHeight()) / 2.0; 
	}
	
	/**
	 * Figure out where the walls and out zones should go in case of a resize.
	 * Resize while running is not supported but I do try to clip the paddles and ball to a reasonable position.
	 */
	public void resizeWorld() {
		int width = this.getWidth();
		int height = this.getHeight();
		
		playerLeft.clipXY((double) wallDistance, height);
		playerRight.clipXY((double) width - wallDistance - Player.getWidth(), height);
		if (ball.getX() < 0.0 || ball.getX() >= width) {
			ball.setX((double) width / 2);
		}
		if (ball.getY() < 0.0 || ball.getY() >= height) {
			ball.setY((double) height / 2);
		}
		
		this.topWall = new Rectangle(0, 0, width, this.wallSize);
		this.bottomWall =  new Rectangle(0, height - this.wallSize, width, this.wallSize);
		this.leftOutZone = new Rectangle(0, 0, this.wallSize, height);
		this.rightOutZone = new Rectangle(width - this.wallSize, 0, this.wallSize, height);
		
	}
	
	/**
	 * Resize event callback. Kick off the start command if not ready and we have a non-zero width and height.
	 * Couldn't put this in the constructor, it wasn't ready at that point.
	 */
	public void componentResized(ComponentEvent e) {
		if (this.ready) { 
			resizeWorld();
		}
		else if (this.getWidth() > 0.0 && this.getHeight() > 0.0) {
			start();
		}
    }
	
	/**
	 * Hidden event callback. Needed to get the resize event. Don't do anything with this.
	 */
	public void componentHidden(ComponentEvent e) {
		// Do nothing, only wanted resize
    }

	/**
	 * Hidden event callback. Needed to get the resize event. Don't do anything with this.
	 */
    public void componentMoved(ComponentEvent e) {
    	// Do nothing, only wanted resize
    }

	/**
	 * Hidden event callback. Needed to get the resize event. Don't do anything with this.
	 */
    public void componentShown(ComponentEvent e) {
    	// Do nothing, only wanted resize
    }

    /**
     * Paint the component. Just calls my draw command.
     * @param g: Graphics rendering context.
     */
    @Override
    public void paintComponent(Graphics g) {  
    	this.draw((Graphics2D) g);    	
    }
    
    /**
     * Return the preferred size for the component.
     * @return - Dimension object with preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }
    
    
    /**
     * Start up a new thread and run the game loop. 
     */
    public void runGameLoop()
    {
    	// Starts a new thread and runs the game loop in it.
    	Thread loop = new Thread()
    	{
    		public void run()
    		{
    			gameLoop();
			}
		};
		this.running = true;
		loop.start();
	}
    
    /**
     * The main game loop. 
     * WARNING: Should be run under a different thread!
     */
    private void gameLoop()
    {       
       
       //If we are able to get as high as this FPS, don't render again.
       final double TARGET_FPS = 60;
       final double TARGET_TIME = 1000000000 / TARGET_FPS;
       
       //Store the last time we ran the draw and update loops.
       double currentTime = System.nanoTime() - TARGET_TIME; // Want the first loop to not be zero.
       double previousTime;
       double dt;
       
       while (this.running)
       {
    	  previousTime = currentTime;
          currentTime = System.nanoTime();
          // Compute delta time in milliseconds not nanoseconds.
          // Go no faster than target time
          dt = Math.min(TARGET_TIME * 4, Math.max(TARGET_TIME, (currentTime - previousTime))) / 1000000.0;
          
          this.update(dt);
          this.repaint();
          
          // Yield until it has been at least the target time. This saves the CPU from hogging.          
          while ( currentTime - previousTime < TARGET_TIME) {
        	  Thread.yield();

        	  // This stops the app from consuming all your CPU.
        	  try {Thread.sleep(1);} catch(Exception e) {}
        	  currentTime = System.nanoTime(); 
          }
       }
    }	
}
