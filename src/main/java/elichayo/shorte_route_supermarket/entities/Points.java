package elichayo.shorte_route_supermarket.entities;

public class Points {

    private Point[] points;
    private int size;
    private int index;

    public Points() {
        this.points = new Point[10];
        this.size = 10;
        this.index = 0;
    }

    public Points(int size) {
        this.points = new Point[size];
        this.size = size;
        this.index = 0;
    }

    public int getIndex() {
        return this.index;
    }

    public int getSize() {
        return this.size;
    }

    public void addPoint(Point point) {
        if (this.index < this.size) {
            points[this.index] = point;
            this.index++;
        }
    }

    public void addPoint(Point point, int index) throws Exception{
        if (index < this.size) {
            points[index] = point;
            if (this.index < index) {
                this.index = index;
                // System.out.println("new index = " + index);
            }
        } else{
            //System.out.println("------ Error add point ----");
            throw new Exception("------ Error add point ----");
        }
    }

    public Point getPoint(int index) {
        if (index <= this.index && index >= 0)
            return points[index];
        //System.out.println("--------Error index!.----- " + index);
        return null;
    }

    public Point getPoint() {
        if (this.index > 0) {
            this.index--;
            return points[this.index];
        }
        System.out.println("-----Points is empty.----");
        return null;
    }

    public void deleteAllPoints() {
        this.index = 0;
    }

    public void printPoints() {
        System.out.println("points =");
        int i;
        for (i = 0; i < this.size; i++) {
            this.points[i].print();
            System.out.println();
        }
    }
}
