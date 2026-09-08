package tetrisProject;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class TetrisGame  extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JLabel statusbar;
	
	public TetrisGame() {
		this.setSize(200, 400);
		this.setTitle("Hakeem's Tetris Game");
		this .setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.statusbar = new JLabel("0");
		this.add(this.statusbar, BorderLayout.SOUTH);
		
		Board board = new Board(this);
		this.add(board);
		board.start();
	}
	
	public JLabel getStatusBar() {
		return this.statusbar;
	}
	
}
