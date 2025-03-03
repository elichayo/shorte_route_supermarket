package elichayo.shorte_route_supermarket.services;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

public class SimplePathFinder { // מקבל מטריצה של תיאור המסלול ואת הנקודות שצריך למצוא אליהם מסלול ומחזיר את המסלול המלא.

    private static final int BLOCKED = 100; // תאים חסומים

    private int[][] matrix; // מטריצת הסופר
    private int[] route; // המסלול
    private List<List<int[]>> allPaths; // המסלול המלא.

    // בנאי שמקבל מטריצה ונקודות ציון
    public SimplePathFinder(int[][] matrix, int[] route) {
        this.matrix = matrix;
        this.route = route;
        this.allPaths = new ArrayList<>();
    }

    // פונקציה ראשית שמחשבת את כל המסלולים
    public void calculatePaths() {
        Graph<String, DefaultWeightedEdge> graph = buildGraph();
        Map<Integer, String> nodeMap = createNodeMap();

        // חישוב מסלול בין כל זוג נקודות ציון
        for (int i = 0; i < route.length - 1; i++) {
            String start = nodeMap.get(route[i]);
            String end = nodeMap.get(route[i + 1]);
            List<int[]> path = bfs(graph, start, end);
            if (path.isEmpty()) {
                throw new IllegalStateException("אין מסלול מ-" + start + " ל-" + end);
            }
            allPaths.add(path);
        }
        printSeparatePaths();
        printFullPath();
    }

    // הדפסת כל המקטעים בנפרד
    private void printSeparatePaths() {
        if (allPaths.isEmpty()) {
            System.out.println("עדיין לא חושבו מסלולים. קרא ל-calculatePaths קודם.");
            return;
        }

        System.out.println("מסלולים נפרדים:");
        for (int i = 0; i < allPaths.size(); i++) {
            System.out.println("מקטע " + i + " (מ-" + route[i] + " ל-" + route[i + 1] + "):");
            for (int[] point : allPaths.get(i)) {
                System.out.println("  " + Arrays.toString(point));
            }
        }
    }

    // הדפסת המסלול המלא
    private void printFullPath() {
        if (allPaths.isEmpty()) {
            System.out.println("עדיין לא חושבו מסלולים. קרא ל-calculatePaths קודם.");
            return;
        }

        System.out.println("\nהמסלול המלא:");
        Set<String> visited = new HashSet<>(); // למנוע כפילויות
        for (List<int[]> path : allPaths) {
            int startIdx = visited.isEmpty() ? 0 : 1; // כולל התחלה רק במקטע הראשון
            for (int i = startIdx; i < path.size(); i++) {
                int[] point = path.get(i);
                String pointStr = Arrays.toString(point);
                if (visited.add(pointStr)) {
                    System.out.println(pointStr);
                }
            }
        }
    }

    // יצירת הגרף מהמטריצה
    private Graph<String, DefaultWeightedEdge> buildGraph() {
        Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        int rows = matrix.length, cols = matrix[0].length;

        // הוספת צמתים
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] != BLOCKED) {
                    graph.addVertex(i + "," + j);
                }
            }
        }

        // חיבור תאים סמוכים
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] != BLOCKED) {
                    String node = i + "," + j;
                    for (int[] dir : directions) {
                        int ni = i + dir[0], nj = j + dir[1];
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && matrix[ni][nj] != BLOCKED) {
                            String neighbor = ni + "," + nj;
                            if (!graph.containsEdge(node, neighbor)) {
                                graph.addEdge(node, neighbor);
                            }
                        }
                    }
                }
            }
        }
        return graph;
    }

    // יצירת מיפוי בין ערכים למיקומים
    private Map<Integer, String> createNodeMap() {
        Map<Integer, String> nodeMap = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != BLOCKED) {
                    nodeMap.put(matrix[i][j], i + "," + j);
                }
            }
        }
        return nodeMap;
    }

    // BFS שמוצא מסלול בין שתי נקודות
    private List<int[]> bfs(Graph<String, DefaultWeightedEdge> graph, String start, String end) {
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) {
                List<int[]> path = new ArrayList<>();
                String node = end;
                while (node != null) {
                    path.add(parseNode(node));
                    node = parent.get(node);
                }
                Collections.reverse(path);
                return path;
            }

            for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(current)) {
                String neighbor = graph.getEdgeTarget(edge).equals(current) ? graph.getEdgeSource(edge)
                        : graph.getEdgeTarget(edge);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }
        return Collections.emptyList(); // אם אין מסלול
    }

    // המרת מחרוזת לנקודה
    private int[] parseNode(String node) {
        String[] parts = node.split(",");
        return new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
    }

    // דוגמה לשימוש במחלקה
    public static void main(String[] args) {
        int[][] matrix = {
                { 100, 100, 100, 100, 100, 101, 101, 101, 100, 100 },
                { 101, 101, 101, 101, 101, 101, 4, 101, 100, 100 },
                { 101, 3, 101, 101, 101, 101, 101, 101, 101, 101 },
                { 101, 101, 101, 101, 101, 100, 100, 101, 2, 101 },
                { 100, 101, 101, 101, 101, 101, 101, 101, 101, 101 },
                { 100, 101, 101, 101, 1, 101, 101, 101, 101, 101 },
                { 100, 101, 101, 101, 101, 101, 101, 5, 101, 101 },
                { 101, 101, 101, 101, 101, 101, 101, 101, 101, 101 },
                { 101, 101, 101, 101, 101, 100, 100, 100, 100, 100 },
                { 0, 101, 101, 101, 101, 100, 100, 100, 100, 100 }
        };

        int[] checkpoints = { 0, 1, 2, 3 };

        // יצירת מופע של המחלקה
        SimplePathFinder pathFinder = new SimplePathFinder(matrix, checkpoints);

        // חישוב המסלולים
        pathFinder.calculatePaths();

    }
}