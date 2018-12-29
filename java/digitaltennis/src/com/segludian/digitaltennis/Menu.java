package com.segludian.digitaltennis;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Menu extends JPanel implements ActionListener {

	private static final long serialVersionUID = -4969365043883597481L;
	private final int padHeight = 16;
	Main parent;

	/**
	 * Constructor.
	 * @param parent - The component housing this panel, a Main object.
	 */
	public Menu(Main parent) {
		this.parent = parent;
		setupGUI();		
	}
	
	/**
	 * Set up the menu GUI system.
	 */
	public void setupGUI() {
		this.setBackground(Color.BLACK);
		JLabel title = new JLabel("Digital Tennis");
		Font titleFont = new Font("SansSerif", Font.BOLD, 36);
		title.setFont(titleFont);
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton playerVsComputer = new JButton("Player versus Computer");
		playerVsComputer.setMnemonic(KeyEvent.VK_P);
		playerVsComputer.setSelected(true);
		playerVsComputer.setActionCommand("playerVsComputer");
		playerVsComputer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton playerVsPlayer = new JButton("Player versus Player");
		playerVsPlayer.setMnemonic(KeyEvent.VK_L);
		playerVsPlayer.setActionCommand("playerVsPlayer");
		playerVsPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton computerVsComputer = new JButton("Computer versus Computer");
		computerVsComputer.setMnemonic(KeyEvent.VK_C);
		computerVsComputer.setActionCommand("computerVsComputer");;
		computerVsComputer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton help = new JButton("Help");
		help.setMnemonic(KeyEvent.VK_H);		
		help.setActionCommand("help");
		help.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton exit = new JButton("Exit");
		exit.setMnemonic(KeyEvent.VK_X);		
		exit.setActionCommand("exit");
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel buttons = new JPanel();
		buttons.setBackground(Color.BLACK);
		BoxLayout buttonLayout = new BoxLayout(buttons, BoxLayout.Y_AXIS);
		
		buttons.setLayout(buttonLayout);

		buttons.add(playerVsComputer);
		buttons.add(Box.createVerticalStrut(padHeight));
		buttons.add(playerVsPlayer);
		buttons.add(Box.createVerticalStrut(padHeight));
		buttons.add(computerVsComputer);
		buttons.add(Box.createVerticalStrut(padHeight));
		buttons.add(help);
		buttons.add(Box.createVerticalStrut(padHeight));
		buttons.add(exit);
	
		this.setLayout(new GridLayout(2, 0));
		this.add(title);
		this.add(buttons);
		
		
		playerVsComputer.addActionListener(this);
		playerVsPlayer.addActionListener(this);
		computerVsComputer.addActionListener(this);
		help.addActionListener(this);
		exit.addActionListener(this);				
	}
	
	/**
	 * Respond to an event, simply passing the action command back to the main object.
	 * @param e - ActionEvent object with which button was clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		this.parent.menuEvent(e.getActionCommand());
	}
}
