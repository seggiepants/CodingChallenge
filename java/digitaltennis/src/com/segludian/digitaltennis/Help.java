package com.segludian.digitaltennis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Help extends JPanel implements ActionListener {

	private static final long serialVersionUID = -6666370060387630907L;
	private String helpText;
	Main parent;

	/**
	 * Constructor for the help control. The help text is hard coded and would probably be better if 
	 * read from file. But it is short enough it may not matter.
	 * @param parent - Parent control that houses this panel, a Main object.
	 */
	Help(Main parent) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Digital Tennis\r\n");
		sb.append("==============\r\n");
		sb.append("Digital Tennis is a computer recreation of playing tennis. You can play either against\r\n");
		sb.append("the computer or a friend who is sitting next to you. (Sorry, no network play is supported.)\r\n");
		sb.append("\r\n");
		sb.append("Play Versus the Computer:\r\n");
		sb.append("When you choose to play against the computer, you are the player on the left hand side.\r\n");
		sb.append("Move your racket up and down with either the 'W' or 'S' keys or the 'UP' and 'DOWN' arrows.\r\n");
		sb.append("The first player to five points wins the game.\r\n");
		sb.append("\r\n");
		sb.append("Play Versus another Player:\r\n");
		sb.append("When you choose to play against another player you share the keyboard with them.\r\n");
		sb.append("Player one uses the 'W' and 'S' keys to move the left racket up and down. While the second\r\n");
		sb.append("player uses the 'UP' and 'DOWN' arrows to move the racket on the right. The first player\r\n");
		sb.append("to score five points is the winner.\r\n");
		sb.append("\r\n");
		sb.append("Computer Versus Computer:\r\n");
		sb.append("When you choose this option, the computer will play against itself.\r\n");
		sb.append("\r\n");
		sb.append("Help:\r\n");
		sb.append("The help command will show you this help screen.\r\n");
		sb.append("\r\n");
		sb.append("Exit:\r\n");
		sb.append("Click this button to close the application.");
		sb.append("\r\n");
		sb.append("Note:\r\n");		
		sb.append("While playing you can use the P key to pause and unpause, and the Escape or Q button to\r\n");
		sb.append("return to the title screen.\r\n");
		sb.append("\r\n");
		
		this.helpText = sb.toString();
		
		this.parent = parent;
		this.setupGUI();
	}

	/**
	 * Set up the panel GUI.
	 */
	void setupGUI() {
		JLabel title = new JLabel("Help");
		Font titleFont = new Font("SansSerif", Font.BOLD, 18);
		title.setFont(titleFont);
		title.setForeground(Color.WHITE);
		this.setBackground(Color.BLACK);
		this.setLayout(new BorderLayout());
		
		this.add(title, BorderLayout.NORTH);
		
		JButton ok = new JButton("OK");
		JPanel buttons = new JPanel();
		FlowLayout buttonsLayout = new FlowLayout();
		buttonsLayout.setAlignment(FlowLayout.RIGHT);
		buttons.setLayout(buttonsLayout);
		buttons.add(ok);
		this.add(buttons, BorderLayout.SOUTH);
		ok.addActionListener(this);
		
		JTextArea helpText = new JTextArea();
		helpText.setText(this.helpText);
		JScrollPane helpTextScroll = new JScrollPane(helpText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(helpTextScroll);
	}
	
	/**
	 * Return to the main menu when an action happens, which should really just be the OK button being clicked.  
	 */
	public void actionPerformed(ActionEvent e) {
		this.parent.menuEvent("mainMenu");
	}

}
