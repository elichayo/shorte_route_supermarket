package elichayo.shorte_route_supermarket.entities;

public class Point {
    private int row;
    private int col;

    public Point() {
        this.row = 0;
        this.col = 0;
    }

    public Point(int x, int y) {
        this.row = x;
        this.col = y;
    }

    public Point(int[] x) {
        this.row = x[0];
        this.col = x[1];
    }

    public Point(Point point) {
        this.row = point.row;
        this.col = point.col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void print() {
        System.out.print("row: " + row + " col: " + col);
    }

}
