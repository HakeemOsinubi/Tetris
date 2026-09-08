package tetrisProject;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.Timer;

public class Board  extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int BOARDWIDTH = 10;
	private static final int BOARDHEIGHT = 22;
	
	private Timer timer;
	private boolean isFallingFinished = false;
	private boolean isStarted = false;
	private boolean isPaused = false;
	private int numLinesRemoved = 0;
	private int curX = 0;
	private int curY =0;
	private JLabel statusbar;
	private Shape curPiece;
	private  Tetrominoes[] board;
	       
	
	
	public Board(TetrisGame parent) {
		this.setFocusable(true);
		this.curPiece = new Shape();
		this.timer = new Timer(400, this);
		this.timer.start();
		this.statusbar = parent.getStatusBar();
		this.board = new Tetrominoes[BOARDWIDTH* BOARDHEIGHT];
		this.addKeyListener(new TAdapter());
		this.clearBoard();
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(this.isFallingFinished) {
			this.isFallingFinished = false;
			this.newPiece();
		}
		else {
			this.oneLineDown();
		}
	}
	
	
	int squareWidth() {
		return (int)this.getSize().getWidth()/ BOARDWIDTH;	
		
	}

	int squareHeight() {
		return (int)this.getSize().getHeight()/ BOARDHEIGHT;	
	}
	
	Tetrominoes shapeAT(int x, int y) {
		return this.board[(y * BOARDWIDTH) + x];
	}
	
	public void start() {
		if(this.isPaused) {
			return;
		}
		
		this.isStarted = true;
		this.isFallingFinished = false;
		this.numLinesRemoved = 0;
		this.clearBoard();
		
		this.newPiece();
		this.timer.start();
	}
	
	@Override
	
	public void paint (Graphics g) {
		super.paint(g);
		
		Dimension size = this.getSize();
		int boardTop = (int) size.getHeight() - BOARDHEIGHT * this.squareHeight();
		
		for (int i = 0; i < BOARDHEIGHT; i++) {
		    for (int j = 0; j < BOARDWIDTH; j++) {
		        Tetrominoes shape = this.shapeAT(j, i);
		        if (shape != Tetrominoes.NoShape) {
		            this.drawSquare(g, j * squareWidth(),
		                boardTop + i * squareHeight(),
		                shape);
		        }
		    }
		    if (this.curPiece.getShape() != Tetrominoes.NoShape) {
		        for (int i1 = 0; i1 < 4; i1++) {
		            int x = this.curX + this.curPiece.x(i1);
		            int y = this.curY + this.curPiece.y(i1);

		            // Calculate draw position
		            int drawY = boardTop + y * this.squareHeight();

		            // If part of piece is above top of board, clamp to top
		            if (drawY < boardTop) {
		                drawY = boardTop;
		            }

		            this.drawSquare(g, x * this.squareWidth(), drawY, this.curPiece.getShape());
		        }
		    }

		}

		
		if(this.curPiece.getShape()!= Tetrominoes.NoShape) {
			for(int i =0; i< 4; i++) {
				int x = this.curX + this.curPiece.x(i);
				int y = this.curY + this.curPiece.y(i);
				if (y >= 0) {
				    this.drawSquare(g, x * this.squareWidth(), boardTop + y * this.squareHeight(), this.curPiece.getShape());
				}
			}
		}
	}
	
	private void clearBoard() {
		for(int i =0; i< (BOARDHEIGHT * BOARDWIDTH ); i++) {
			this.board[i] = Tetrominoes.NoShape;
		}
	}
	
	private boolean tryMove(Shape newPiece, int newX, int newY) {
		for(int i = 0; i<4; i++) {
			int x = newX + newPiece.x(i);
			int y = newY + newPiece.y(i);  
			if ((x < 0) || (x >= BOARDWIDTH) || (y >= BOARDHEIGHT)) {
			    return false;
			}
			if(this.shapeAT(x, y)!= Tetrominoes.NoShape) {
				return false;
			}
		}
		
		this.curPiece = newPiece;
		this.curX = newX;
		this.curY = newY;
		this.repaint();
		return true;
	}
	
	private void removeFullLines() {
	    int numFullLines = 0;

	    for (int row = BOARDHEIGHT - 1; row >= 0; row--) {
	        boolean lineIsFull = true;
	        for (int col = 0; col < BOARDWIDTH; col++) {
	            if (this.shapeAT(col, row) == Tetrominoes.NoShape) {
	                lineIsFull = false;
	                break;
	            }
	        }

	        if (lineIsFull) {
	            numFullLines++;
	            // Move all rows above down
	            for (int r = row; r > 0; r--) {
	                for (int col = 0; col < BOARDWIDTH; col++) {
	                    this.board[(r * BOARDWIDTH) + col] = this.board[((r - 1) * BOARDWIDTH) + col];
	                }
	            }
	            // Clear top row
	            for (int col = 0; col < BOARDWIDTH; col++) {
	                this.board[col] = Tetrominoes.NoShape;
	            }

	            row++; // recheck same row, since rows have moved down
	        }
	    }

	    if (numFullLines > 0) {
	        this.numLinesRemoved += numFullLines;
	        this.statusbar.setText(String.valueOf(this.numLinesRemoved));
	        this.isFallingFinished = true;
	        this.curPiece.setShape(Tetrominoes.NoShape);
	    }
	}

	
	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color color = shape.getColor();
		g.setColor(color);
		g.fillRect(x+1, y+1, this.squareWidth()-2, this.squareHeight() - 2);
		
		g.setColor(color.brighter());
		g.drawLine(x, y+ this.squareHeight()-1, x, y);
		g.drawLine(x, y, x + this.squareHeight()-1, y);
		
		g.setColor(color.darker());
		g.drawLine(x+1, y+ this.squareHeight()-1, x+ this.squareWidth(), y+ this.squareHeight()-1);
		g.drawLine(x+ this.squareWidth()-1, y+ this.squareHeight(), x + this.squareWidth()-1, y+1);
	}
	
	private void newPiece() {
	    this.curPiece.setRandomShape();
	    this.curX = BOARDWIDTH / 2 - 1;
	    this.curY = -this.curPiece.minY() + 1; // Spawn 1 row above the board

	    if (!tryMove(this.curPiece, this.curX, this.curY)) {
	        this.curPiece.setShape(Tetrominoes.NoShape);
	        this.timer.stop();
	        this.isStarted = false;
	        this.statusbar.setText("Game OVER - FINAL score was " + this.numLinesRemoved);
	    }
	}
	
			
	private void pieceDropped() {
		for (int i = 0; i < 4; i++) {
			int x = this.curX + this.curPiece.x(i);
			int y = this.curY + this.curPiece.y(i);
			this.board[(y * BOARDWIDTH) + x] = this.curPiece.getShape();
		}
		this.removeFullLines();
		this.isFallingFinished = true; // Let the timer create the next piece
	}

			
	private void oneLineDown() {
	    if (!this.tryMove(this.curPiece, this.curX, this.curY + 1)) {
	        this.pieceDropped();
	    }
	}

	
	private void dropDown() {
	    int newY = this.curY;
	    while (newY < BOARDHEIGHT - 1) {
	        if (!this.tryMove(this.curPiece, this.curX, newY + 1)) {
	            break;
	        }
	        newY++;
	    }
	    this.pieceDropped();
	}

	
	private void pause() {
		if(! this.isStarted) {
			return;
		}
		
		this.isPaused = !this.isPaused;
		
		if( this.isPaused) {
			this.timer.stop();
			this.statusbar.setText("-- game paused --");
		}
		else {
			this.timer.start();
			this.statusbar.setText(" "+ this.numLinesRemoved);
		}
		this.repaint();
	}
			
	class TAdapter extends KeyAdapter{
		
		public void keyPressed(KeyEvent ke) {
			if(! isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
				return;
			}
			
			int keycode = ke.getKeyCode();
			
			if(keycode == 'p' || (keycode == 'P') ) {
				pause();
				return;
			}
			
			switch(keycode) {
				case KeyEvent.VK_LEFT:
					tryMove(curPiece, curX-1, curY);
					break;
					
				case KeyEvent.VK_RIGHT:
					tryMove(curPiece, curX+1, curY);
					break;
				
				case KeyEvent.VK_DOWN:
					tryMove(curPiece.rotateRight(), curX, curY);
					break;
				
				case KeyEvent.VK_UP:
					tryMove(curPiece.rotateLeft(), curX, curY);
					break;
				
				case KeyEvent.VK_SPACE:
					dropDown();
					break;
				case 'd':
				case 'D':
					oneLineDown();
					break;
				
			}
			
		}
		
	}					
}
