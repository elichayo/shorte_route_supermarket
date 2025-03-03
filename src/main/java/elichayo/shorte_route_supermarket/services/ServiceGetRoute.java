package elichayo.shorte_route_supermarket.services;

import org.springframework.stereotype.Service;

import elichayo.shorte_route_supermarket.entities.Point;
import elichayo.shorte_route_supermarket.entities.Points;

@Service
public class ServiceGetRoute {

    public interface getRouteListener {
        public void onRouteReady(int[] route);
    }

    public final static int INF = Integer.MAX_VALUE;
    public final static int[][] GRAPH1 = { { INF, 1, 7, 3, 10, 6, 5, 1, 7, 5, 4, 5, 2, 10, 20, 3, 4, 5, 3, 1 },
            { 4, INF, 2, 3, 6, 7, 4, 1, 4, 3, 4, 5, 6, 7, 2, 1, 3, 5, 5, 6 },
            { 7, 6, INF, 3, 3, 3, 7, 4, 7, 6, 4, 5, 6, 2, 4, 6, 7, 4, 1, 1 },
            { 3, 3, 7, INF, 4, 6, 5, 1, 7, 5, 4, 5, 2, 10, 20, 3, 4, 5, 3, 1 },
            { 10, 6, 3, 10, INF, 1, 7, 3, 10, 6, 4, 5, 6, 7, 2, 1, 3, 5, 5, 6 },
            { 6, 7, 3, 5, 4, INF, 1, 3, 6, 7, 4, 5, 6, 2, 4, 6, 7, 4, 1, 1 },
            { 5, 4, 7, 5, 7, 6, INF, 5, 3, 3, 4, 5, 6, 7, 2, 1, 3, 5, 5, 6 },
            { 1, 1, 4, 1, 3, 3, 7, INF, 6, 3, 4, 5, 6, 7, 2, 1, 3, 5, 5, 6 },
            { 7, 4, 1, 7, 10, 6, 3, 4, INF, 1, 4, 5, 6, 7, 2, 1, 3, 5, 5, 6 },
            { 5, 3, 6, 5, 6, 7, 3, 3, 5, INF, 7, 5, 6, 7, 2, 1, 3, 5, 5, 6 },
            { 4, 5, 6, 4, 5, 6, 4, 5, 6, 4, INF, 11, 2, 1, 3, 5, 5, 6, 7, 4 },
            { 5, 6, 7, 5, 6, 7, 5, 6, 7, 5, 7, INF, 1, 5, 5, 6, 7, 2, 1, 3 },
            { 2, 6, 2, 10, 6, 2, 6, 7, 2, 6, 2, 3, INF, 11, 5, 6, 7, 2, 1, 3 },
            { 10, 7, 4, 20, 7, 4, 7, 2, 1, 7, 1, 5, 4, INF, 11, 7, 2, 1, 3, 5 },
            { 20, 2, 6, 3, 2, 6, 2, 1, 3, 2, 3, 5, 5, 6, INF, 31, 5, 6, 7, 2 },
            { 3, 1, 6, 4, 1, 6, 1, 3, 5, 1, 5, 6, 6, 7, 4, INF, 41, 7, 2, 1 },
            { 4, 3, 7, 4, 3, 7, 3, 5, 5, 3, 5, 7, 7, 2, 5, 6, INF, 3, 5, 6 },
            { 5, 5, 4, 5, 5, 4, 5, 5, 5, 5, 6, 2, 2, 1, 6, 7, 4, INF, 1, 7 },
            { 3, 5, 4, 3, 5, 1, 5, 5, 5, 5, 7, 1, 1, 3, 7, 2, 5, 6, INF, 1 },
            { 1, 6, 1, 1, 6, 1, 6, 6, 6, 6, 4, 3, 3, 5, 2, 1, 6, 7, 4, INF } };

    public final static int[][] GRAPH2 = { { INF, 1, 3, 4, 5, 6 },
            { 2, INF, 1, 4, 5, 6 },
            { 3, 1, INF, 1, 5, 6 },
            { 4, 1, 2, INF, 1, 6 },
            { 5, 1, 2, 3, INF, 1 },
            { 13, 1, 2, 3, 4, INF } };

    public final static int[][] GRAPH3 = { { INF, 4, 7, 3 },
            { 4, INF, 6, 3 },
            { 7, 6, INF, 7 },
            { 3, 3, 7, INF } };

    private int[][] graph;
    private int[] route; // כאשר במיקום הראשון זאת הנקודה מאחד לאיזה מיקום וכן האלה
    private Thread taskThread;
    private getRouteListener listener;

    private static Points points;

    // גרף המתקבל על ידי פונקציה אחרת שתקבל מפה, ורשימת קניות ותחזיר גרף
    public ServiceGetRoute(int[][] graph) {
        setup(graph);
    }

    public void setup(int[][] graph) {

        this.graph = graph;
        this.route = new int[graph.length];
    }

    public int[][] getGraph() {
        return graph;
    }

    public void setGraph(int[][] graph) {
        this.graph = graph;
    }

    public void addGraphListener(getRouteListener listener) {
        this.listener = listener;
    }

    public void shortRoute() {
        // System.out.println("First graph:");
        // printGraph(this.graph);
        taskThread = new Thread(() -> {
            int[][] newGraph;
            int i;
            Point point = new Point();
            points = new Points(this.graph.length);

            do {
                // System.out.println("First graph:");
                // printGraph(this.graph);
                newGraph = createNewGraph();

                points.deleteAllPoints();

                for (i = 0; i < newGraph.length; i++) {

                    matrixSubtractionRow(newGraph);

                    matrixSubtractionCol(newGraph);

                    point = calculationMaxZerroSuffix(newGraph);
                    if (point.getRow() == -1 && point.getCol() == -1) {
                        i = 0; // סיום הלולאה
                        points.deleteAllPoints();
                        newGraph = createNewGraph();

                        // System.out.println("First graph:");
                        // printGraph(newGraph);
                    } else {
                        // point.print();
                        addPointToRoute(point);
                        points.addPoint(point);

                        DeletingRowsAndColumns(newGraph, point);

                        // System.out.println("graph after:");
                        // printGraph(newGraph);
                    }
                }
                orderRoute();
                // System.out.println("route :");
                // printRoute();

                // price
                // printPriceOfRoute(route);
            } while (!checkRouteAndRepair());

            listener.onRouteReady(route);

        });
        taskThread.start();
    }

    private void printGraph(int[][] graph) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] != Integer.MAX_VALUE)
                    if (graph[i][j] < 10)
                        System.out.print(graph[i][j] + "  ");
                    else
                        System.out.print(graph[i][j] + " ");
                else
                    System.out.print("∞  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private int[][] createNewGraph() {
        int[][] newGraph = new int[this.graph.length][this.graph.length];
        int i, j;
        for (i = 0; i < this.graph.length; i++) {
            for (j = 0; j < this.graph.length; j++) {
                newGraph[i][j] = this.graph[i][j];
            }
        }
        return newGraph;
    }

    private int[][] matrixSubtractionRow(int[][] graph) {
        int i, j;
        int minNum;
        // מעבר על השורות וחיסור המספר הקטן ביותר מכל אחד מהם
        for (i = 0; i < graph.length; i++) {
            minNum = Integer.MAX_VALUE;
            for (j = 0; j < graph.length; j++) {
                if (graph[i][j] < minNum)
                    minNum = graph[i][j]; // מציאת הערך הכי מנימאלי בשורה
            }
            for (j = 0; j < graph.length; j++) {
                if (graph[i][j] != Integer.MAX_VALUE) // אם הערך הוא מקסימאלי אז אין סןף
                    graph[i][j] -= minNum; // החסרת הערך הכי מנימאלי מכל השורה

            }
        }
        return graph;
    }

    private int[][] matrixSubtractionCol(int[][] graph) {
        int i, j;
        int minNum;
        // מעבר על העמודות וחיסור המספר הקטן ביותר מכל אחד מהם
        for (j = 0; j < graph.length; j++) {
            minNum = Integer.MAX_VALUE;
            for (i = 0; i < graph.length; i++) {
                if (graph[i][j] < minNum)
                    minNum = graph[i][j]; // מציאת הערך הכי מנימאלי בשורה
            }
            for (i = 0; i < graph.length; i++) {
                if (graph[i][j] != Integer.MAX_VALUE) // אם הערך הוא מקסימאלי אז אין סןף
                    graph[i][j] -= minNum; // החסרת הערך הכי מנימאלי מכל השורה
            }
        }
        return graph;
    }

    private Point calculationMaxZerroSuffix(int[][] graph) {
        int i, j;
        double maxZerroSuffix = -1;
        double zerroSuffix = 0;
        Point point = new Point(-1, -1);
        for (i = 0; i < graph.length; i++) {
            for (j = 0; j < graph.length; j++) {
                if (graph[i][j] == 0) {
                    zerroSuffix = zerroSuffix(graph, i, j);
                    if (zerroSuffix > maxZerroSuffix) {
                        maxZerroSuffix = zerroSuffix;
                        point.setRow(i);
                        point.setCol(j);
                    }
                }
            }
        }
        // במידה והתקבל אין סוף יתבצע חסימה על הגרף
        if (point.getRow() == -1 && point.getCol() == -1) {
            // System.out.println("------- Route not found, infinety graph -------\n");
            // System.exit(1);
            repairGraph();
        }
        return point;
    }

    // מחשב את ערכי סיומת האפס ומחזיר את הערך
    private double zerroSuffix(int[][] graph, int row, int col) {
        double zerroSuffix = 0;
        double countZerro = 0;
        int i;
        for (i = 0; i < graph.length; i++)// מעבר על הערכים בכל עמודה
        {
            if (graph[i][col] != Integer.MAX_VALUE && graph[i][col] != 0) // למטה
                zerroSuffix += graph[i][col];
            if (graph[i][col] == 0 && i != row)
                countZerro++;
        }
        for (i = 0; i < graph.length; i++)// מעבר על הערכים בכל שורה
        {
            if (graph[row][i] != Integer.MAX_VALUE && graph[row][i] != 0) // למטה
                zerroSuffix += graph[row][i];
            if (graph[i][row] == 0 && i != col)
                countZerro++;

        }
        countZerro++; // כי צריך להחשיב גם את עצמו
        if (countZerro == 0) // כדי למנוע חלוקה באפס
            return 0.0;
        return (zerroSuffix / countZerro);
    }

    private void DeletingRowsAndColumns(int[][] graph, Point point) {
        int i;
        for (i = 0; i < graph.length; i++) {
            graph[point.getRow()][i] = Integer.MAX_VALUE;
            graph[i][point.getCol()] = Integer.MAX_VALUE;
        }
        // נקודה מקבילה
        graph[point.getCol()][point.getRow()] = Integer.MAX_VALUE;
    }

    // פונקציה שמסדרת את המסלול למסלול המלא 1->2->3->4
    private void orderRoute() {
        int[] orderRoute = new int[this.route.length];
        int i;
        orderRoute[0] = 0;
        for (i = 1; i < this.route.length; i++) {
            orderRoute[i] = this.route[orderRoute[i - 1]];
        }
        route = orderRoute;
    }

    private boolean checkRouteAndRepair() {
        int[] chackIndexs = new int[this.route.length];
        int i;
        for (i = 0; i < this.route.length; i++) {
            if (chackIndexs[this.route[i]] == 0)
                chackIndexs[this.route[i]]++;
            else {
                // חסימת הגישה לאותה נקודה
                // System.out.println("------- Loop in route -------\n");
                // repairRoute(new Point(this.route[i-1], this.route[i]), i-1);
                repairRoute();
                return false;
            }
        }
        return true;
    }

    private void repairRoute() {
        int countInRow, countInCol, i;
        countInRow = countInCol = 0;
        for (i = 0; i < this.graph.length && countInRow <= 1 || countInCol <= 1; i++) {
            if (this.graph[points.getPoint(points.getIndex()).getRow()][i] != Integer.MAX_VALUE)
                countInRow++;
            if (this.graph[i][points.getPoint(points.getIndex()).getCol()] != Integer.MAX_VALUE)
                countInCol++;
        }
        if (countInRow <= 1 || countInCol <= 1) {
            // System.out.println("------- Error in repairRoute -------\n");
            // System.exit(1);
            points.getPoint();
            repairRoute();
        }
        this.graph[points.getPoint(points.getIndex()).getRow()][points.getPoint(points.getIndex())
                .getCol()] = Integer.MAX_VALUE;
    }

    // private void repairRoute(Point point, int index)
    // {
    // int countInRow, countInCol, i;
    // countInRow = countInCol = 0;
    // for (i = 0; i < this.graph.length && countInRow <= 1 || countInCol <= 1; i++)
    // {
    // if (this.graph[point.getRow()][i] != Integer.MAX_VALUE)
    // countInRow++;
    // if (this.graph[i][point.getCol()] != Integer.MAX_VALUE)
    // countInCol++;
    // }
    // if (countInRow <= 1 || countInCol <= 1)
    // {
    // System.out.println("------- Error in repairRoute -------\n");
    // //System.exit(1);
    // point.setRow(route[index]);
    // point.setCol(route[index-1]);
    // index--;
    // repairRoute(point, index);
    // }
    // this.graph[point.getRow()][point.getCol()] = Integer.MAX_VALUE;
    // }

    private void repairGraph() {
        int countInRow, countInCol, i;
        countInRow = countInCol = 0;
        for (i = 0; i < this.graph.length && countInRow <= 1 || countInCol <= 1; i++) {
            if (this.graph[points.getPoint(points.getIndex()).getRow()][i] != Integer.MAX_VALUE)
                countInRow++;
            if (this.graph[i][points.getPoint(points.getIndex()).getCol()] != Integer.MAX_VALUE)
                countInCol++;
        }
        if (countInRow <= 1 || countInCol <= 1) {
            points.getPoint();
            repairGraph();
        }
        this.graph[points.getPoint(points.getIndex()).getRow()][points.getPoint(points.getIndex())
                .getCol()] = Integer.MAX_VALUE;
    }

    private void printPriceOfRoute(int[] route) // בעיה שזה פוגע במסלול...
    {
        int i;
        int price = 0;
        for (i = 1; i < route.length; i++)
            price += this.graph[route[i - 1]][route[i]];
        System.out.println("Price of route: " + price + "\n");
    }

    private void addPointToRoute(Point point) {
        this.route[point.getRow()] = point.getCol();
    }

    private void printRoute() {
        int i;
        for (i = 0; i < this.route.length; i++) {
            System.out.print(this.route[i] + "->");
        }
        System.out.println("\n");
    }
}
