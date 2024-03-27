package main;
import java.awt.*;

import javax.swing.*;
public class main {
	public static void main(String[] args) {
		//Create frame
		JFrame frame = new JFrame();
		
		//make the layout grids and make the frame black
		frame.setLayout(new GridBagLayout());
		frame.getContentPane().setBackground(Color.BLACK);
		
		//set size and put in middle of screen
		frame.setMinimumSize(new Dimension(1000, 1000));
		frame.setLocationRelativeTo(null);
		
		//create board and add to frame
		Board board = new Board();
		frame.add(board);
		
		//make frame visible
		frame.setVisible(true);
	}

}
