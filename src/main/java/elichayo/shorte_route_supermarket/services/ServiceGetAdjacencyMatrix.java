package elichayo.shorte_route_supermarket.services;

import org.springframework.stereotype.Service;

import elichayo.shorte_route_supermarket.entities.Point;
import elichayo.shorte_route_supermarket.entities.Points;

@Service
public class ServiceGetAdjacencyMatrix {
    public interface InvertMatrixListener {
        public void onGetAdjacencyMatrix(int[][] graph);

        public void onError(boolean error);
    }

    private int[][] matrix;
    private InvertMatrixListener listener;

    final static int START_NODE = 0;
    final static int NODE = 1; // 1<=NODE<=99
    final static int EMPTY = 100;
    final static int ROUTE = 101;

    public ServiceGetAdjacencyMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void addInvertMatrixLisener(InvertMatrixListener listener) {
        this.listener = listener;
    }

    public void startInvertMatrix() {
        new Thread(() -> {
            int sumNodes = sumNodes();
            Points nodes = null;
            try {
                nodes = findNodes(sumNodes);
                int[][] adjacencyMatrix = createAdjacencyMatrix(nodes, sumNodes);
                listener.onGetAdjacencyMatrix(adjacencyMatrix);
                if (sumNodes != 0)
                    listener.onError(false);
            } catch (Exception e) {
                listener.onError(true);
            }
        }).start();
    }

    private int sumNodes() {
        int i, j;
        int sum = 0;
        for (i = 0; i < matrix.length; i++) {
            for (j = 0; j < matrix.length; j++) {
                if (matrix[i][j] >= 0 && matrix[i][j] < 100)
                    sum++;
            }
        }
        // System.out.println("sum = " + sum);
        return sum;
    }

    private Points findNodes(int sumNodes) throws Exception {
        Points indexsNodes = new Points(sumNodes);
        int index = 0;
        int i, j;
        while (index < sumNodes) {
            for (i = 0; i < matrix.length; i++) {
                for (j = 0; j < matrix.length; j++) {
                    if (matrix[i][j] == index)
                        indexsNodes.addPoint(new Point(i, j), index++);
                }
            }
        }
        return indexsNodes;
    }

    private int[][] createAdjacencyMatrix(Points nodes, int sumNodes) throws Exception {
        int[][] adjacencyMatrix = new int[sumNodes][sumNodes];
        int i, j;
        Point point;
        for (i = 0; i < sumNodes; i++) {
            point = nodes.getPoint(i);
            for (j = 0; j < sumNodes; j++) {
                if (i == j)
                    adjacencyMatrix[i][j] = Integer.MAX_VALUE;
                else
                    adjacencyMatrix[i][j] = distance(point, nodes.getPoint(j));
            }
        }
        return adjacencyMatrix;
    }

    private int distance(Point point1, Point point2) throws Exception {
        int distance = Math.abs(point1.getRow() - point2.getRow()); // abs = ערך מוחלט
        distance += Math.abs(point1.getCol() - point2.getCol());
        return distance;
    }
}
