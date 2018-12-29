/**
 * 
 */
package com.segludian.digitaltennis;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * A digital tennis game. It may look similar to 'PONG' 
 * @author: seggiepants
 */
public class Main extends JFrame {

	private static final long serialVersionUID = 6954325115332256860L;

	/**
	 * Constructor.
	 */
	public Main() {
		super("Digital Tennis");
		this.setSize(640, 480);
		this.setVisible(true);
		
		this.add(new Menu(this));
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);;
			}
		});
	}
	
	/** 
	 * Handles events from the main menus.
	 * @param command - The type of event to respond to.
	 */
	public void menuEvent(String command) {
		switch(command)
		{
		case "playerVsComputer":			
			this.getContentPane().removeAll();
			this.add(new GameWorld(this, new HumanController(), new ComputerController()));
			this.revalidate();
			break;
		case "playerVsPlayer":
			this.getContentPane().removeAll();
			this.add(new GameWorld(this, new HumanController(), new HumanController()));
			this.revalidate();
			break;
		case "computerVsComputer":
			this.getContentPane().removeAll();
			this.add(new GameWorld(this, new ComputerController(), new ComputerController()));
			this.revalidate();
			break;
		case "help":
			this.getContentPane().removeAll();
			this.add(new Help(this));
			this.revalidate();
			break;
		case "exit":
			this.dispose();
			break;
		case "mainMenu":
			this.getContentPane().removeAll();
			this.add(new Menu(this));
			this.revalidate();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Entry point. Just create a main object and set it running.
	 * @param args : Any arguments passed to the application.
	 */
	public static void main(String[] args) {
		
		Main main = new Main();
		
	}

}
