package tetrisProject;

import java.awt.Color;

public enum Tetrominoes {
	
    NoShape(new int[][] {  {0,0},  {0,0}, {0,0}, {0,0} }, new Color(0,0,0)),
	ZShape(new int[][] {   {0,-1}, {0,0}, {-1,0},{-1,1} }, new Color(204,102,102)),
	SShape(new int[][] {   {0,-1}, {0,0}, {0,1}, {1,1} }, new Color(102,102,102)),
	LineShape(new int[][]{ {0,-1}, {0,0}, {0,1}, {0,2} }, new Color(102,102,204)),
	TShape(new int[][] {   {-1,0}, {0,0}, {1,0}, {0,1} }, new Color(204,204,102)),
	SquareShape(new int[][]{ {0,0},{1,0}, {0,1}, {1,1} }, new Color(204,102,204)),
	LShape(new int[][] {   {-1,-1},{0,-1},{0,0}, {0,1} }, new Color(102,204,204)),
	JShape(new int[][] {   {1,-1}, {0,-1},{0,0}, {0,1} }, new Color(218,170,0));
	
	
	
	
    private final int[][] coords;
    private final Color color;

    private Tetrominoes(int[][] newCoords, Color newColor) {
        this.coords = newCoords;
        this.color = newColor;
    }

    public int[][] getCoords() {
        return this.coords;
    }

    public Color getColor() {
        return this.color;
    }
}
