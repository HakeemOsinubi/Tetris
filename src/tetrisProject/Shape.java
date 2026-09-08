package tetrisProject;


import java.util.*;

public class Shape {

    private Tetrominoes pieceShape;
    private int coords[][];
    private static List<Tetrominoes> bag = new ArrayList<>();
    private static final Random r = new Random();

    public Shape() {
        this.coords = new int[4][2];
        this.setShape(Tetrominoes.NoShape);
    }

    public void setShape(Tetrominoes newShape) {
        int[][] tempCoords = newShape.getCoords();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                this.coords[i][j] = tempCoords[i][j];
            }
        }
        this.pieceShape = newShape;
    }

    public Tetrominoes getShape() {
        return this.pieceShape;
    }

    public void setRandomShape() {
        if (bag.isEmpty()) {
            // refill and shuffle the bag
            bag.add(Tetrominoes.SquareShape);
            bag.add(Tetrominoes.JShape);
            bag.add(Tetrominoes.LShape);
            bag.add(Tetrominoes.LineShape);
            bag.add(Tetrominoes.SShape);
            bag.add(Tetrominoes.TShape);
            bag.add(Tetrominoes.ZShape);
            Collections.shuffle(bag, r);
        }
        this.setShape(bag.remove(0));
    }

	
	public int x(int index) {
		return this.coords[index][0];
	}
	
	public void setX(int index, int x) {
		this.coords[index][0] = x;
	}
	
	public int y(int index) {
		return this.coords[index][1];
	}
	
	public void setY(int index, int y) {
		this.coords[index][1] = y;
	}
	
	public int minX() {
		int m  = this.coords[0][0];
		for(int i = 1; i < 4; i++) {
			m = Math.min(m, this.coords[i][0]);	
		}
		return m;
	}
	
	public int minY() {
		int m  = this.coords[0][1];
		for(int i = 1; i < 4; i++) {
			m = Math.min(m, this.coords[i][1]);
		}
		return m;
	}
	
	public Shape rotateLeft() {
		Shape result = new Shape();
		if(this.pieceShape == Tetrominoes.SquareShape) {
			result = this;
		}
		else {
			result.pieceShape = this.pieceShape;
			for(int i = 0; i < 4; i++) {
				result.setX(i,y(i));
				result.setY(i, -x(i));
			}
		}
		return result;
	}
	
	public Shape rotateRight() {
		Shape result = new Shape();
		if(this.pieceShape == Tetrominoes.SquareShape) {
			result = this;
		}
		else {
			result.pieceShape = this.pieceShape;
			for(int i = 0; i < 4; i++) {
				result.setX(i,-y(i));
				result.setY(i, x(i));
			}
		}
		return result;
	}
	
}
