package elichayo.shorte_route_supermarket.pages;

import com.vaadin.flow.component.textfield.TextArea;

import java.util.Arrays;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import elichayo.shorte_route_supermarket.services.ServiceGetAdjacencyMatrix;
import elichayo.shorte_route_supermarket.services.ServiceGetAdjacencyMatrix.InvertMatrixListener;

@Route("build-supermarket")
public class PageBuildSupermarket extends VerticalLayout {
    private int[][] matrix;

    private ServiceGetAdjacencyMatrix service;

    private Button[][] buttons;

    private TextArea graphArea;
    private H3 statusGraph;

    private int nodeNum = 1;

    public PageBuildSupermarket(ServiceGetAdjacencyMatrix service) {
        this.service = service;

        // דף מימין לשמאל
        // UI.getCurrent().getElement().setAttribute("dir", "rtl");
        getStyle().setPadding("70px");

        H1 title = new H1("Build supermarket:");

        TextArea superName = new TextArea("Supermarket name");

        TextArea sizeOfGrid = new TextArea("Size of the grid");

        graphArea = new TextArea("Adjacency matrix");
        graphArea.setWidthFull();

        statusGraph = new H3("");

        ComboBox comboBox = new ComboBox<>("Select graph");
        comboBox.setItems("Graph 1", "Graph 2");
        comboBox.addValueChangeListener(event -> {
            // System.out.println("choice = " + event.getValue());
            setGraphOngrid(event.getValue());
        });

        Button send = new Button("send");
        send.addClickListener(e -> {
            // System.out.println("super name:" + superName.getValue());
            printMatrix();
            invertMatrix(matrix);
        });

        matrix = new int[10][10];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = 100; // ערך במשבצת ריקה
            }
        }
        VerticalLayout grid = buttonGrid(10, 10, 500);
        // grid.getStyle().setBackgroundColor("yellow");

        // Div space = new Div();
        // space.setWidth("200px");

        // buildGrid(n , n, 500);
        VerticalLayout input = new VerticalLayout(title, superName, sizeOfGrid, send);
        HorizontalLayout map = new HorizontalLayout(input, graphArea,
                new VerticalLayout(new HorizontalLayout(comboBox, statusGraph), grid));
        add(map);

        // input.getStyle().setBackgroundColor("yellow");
        // map.getStyle().setBackgroundColor("green");
        // getStyle().setBackgroundColor("pink");

        map.setWidthFull();

        // int n = 1;
        // try
        // {
        // n = Integer.parseInt(sizeOfGrid.getValue());
        // } catch (NumberFormatException e)
        // {
        // System.out.println("its not number!.");
        // }
        // buildGrid(n , n, 500);

    }

    private VerticalLayout buttonGrid(int numberOfRows, int numberOfColums, double widthAndHeight) {
        VerticalLayout grid = new VerticalLayout();
        grid.setSpacing(false);

        buttons = new Button[numberOfRows][numberOfColums];

        int btnHeight = (int) widthAndHeight / numberOfRows;
        int btnWidth = (int) widthAndHeight / numberOfColums;
        int i, j;
        for (i = 0; i < numberOfRows; i++) {
            HorizontalLayout rowInGrid = new HorizontalLayout();
            rowInGrid.setSpacing(false);

            for (j = 0; j < numberOfColums; j++) {
                // int btnNum = (i+1)*(j+1);
                // Button button = new Button(btnNum + "");
                buttons[i][j] = new Button();
                buttons[i][j].getStyle().setBorderRadius("0px");
                buttons[i][j].getStyle().set("border", "1px solid gray");
                buttons[i][j].setMinWidth(btnWidth, Unit.PIXELS);
                buttons[i][j].setMinHeight(btnHeight, Unit.PIXELS);
                int row = i;
                int col = j;
                buttons[i][j].addClickListener(e -> {
                    // System.out.println(btnNum + " ");
                    updateButton(buttons[row][col], row, col);
                });
                rowInGrid.add(buttons[i][j]);
            }
            grid.add(rowInGrid);
        }
        return grid;
    }

    private void updateButton(Button btn, int row, int col) {
        String color = btn.getStyle().get("background-color");
        if (color == null) {
            btn.getStyle().set("background-color", "red"); // אדום - צבע דרך
            matrix[row][col] = 101;
        } else if (color.equals("red")) {
            btn.getStyle().set("background-color", "black"); // שחור - צבע צומת
            btn.setText(nodeNum + "");
            matrix[row][col] = nodeNum++;
        } else if (color.equals("black")) {
            btn.getStyle().set("background-color", "green"); // ירוק - צבע נקודת כניסה ויציאה
            btn.setText("0");
            matrix[row][col] = 0;
            nodeNum--;
        } else {
            btn.getStyle().set("background-color", null);
            matrix[row][col] = 100;
            btn.setText("");
        }
        invertMatrix(matrix);
        // printMatrix();
    }

    private void printMatrix() {
        int i, j;
        System.out.println("Matrix:");
        for (i = 0; i < matrix.length; i++) {
            for (j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + ", ");
            }
            System.out.println();
        }
    }

    // private void printGraph(int[][] graph) {
    // System.out.println("new Graph:");
    // int i, j;
    // for (i = 0; i < graph.length; i++) {
    // for (j = 0; j < graph.length; j++) {
    // System.out.print(graph[i][j] + ", ");
    // }
    // System.out.println();
    // }
    // }

    private void setGraphOngrid(Object value) // value = איזה גרף אתה רוצה להציג
    {
        int[][] graph1 = { { 100, 100, 100, 100, 100, 101, 101, 101, 100, 100 },
                { 101, 101, 101, 101, 101, 101, 4, 101, 100, 100 },
                { 101, 3, 101, 101, 101, 101, 101, 101, 101, 101 },
                { 101, 101, 101, 101, 101, 100, 100, 101, 2, 101 },
                { 100, 101, 101, 101, 101, 101, 101, 101, 101, 101 },
                { 100, 101, 101, 101, 1, 101, 101, 101, 101, 101 },
                { 100, 101, 101, 101, 101, 101, 101, 5, 101, 101 },
                { 101, 101, 101, 101, 101, 101, 101, 101, 101, 101 },
                { 101, 101, 101, 101, 101, 100, 100, 100, 100, 100 },
                { 0, 101, 101, 101, 101, 100, 100, 100, 100, 100 } };

        int[][] graph2 = { { 100, 100, 100, 100, 100, 101, 101, 101, 100, 100 },
                { 101, 101, 101, 101, 101, 101, 4, 101, 100, 100 },
                { 101, 3, 101, 101, 101, 101, 101, 101, 101, 101 },
                { 101, 101, 101, 101, 101, 100, 100, 101, 2, 101 },
                { 100, 101, 101, 101, 101, 101, 101, 101, 101, 101 },
                { 100, 101, 101, 101, 1, 101, 101, 101, 101, 101 },
                { 100, 101, 101, 101, 101, 101, 101, 5, 101, 101 },
                { 101, 101, 101, 101, 101, 101, 101, 101, 101, 101 },
                { 101, 101, 101, 101, 101, 100, 100, 100, 100, 100 },
                { 0, 101, 101, 101, 101, 100, 100, 100, 100, 100 } };

        int[][] graph = graph1;
        switch ((String) value) {
            case "Graph 1":
                graph = graph1;
                break;
            case "Graph 2":
                graph = graph2;
                break;
        }
        matrix = graph;
        int i, j;
        for (i = 0; i < graph.length; i++) {
            for (j = 0; j < graph.length; j++) {
                setButtonColor(buttons[i][j], graph[i][j]);
            }
        }
        invertMatrix(graph);
    }

    private void setButtonColor(Button btn, int color) {
        if (color == 100) {
            btn.getStyle().setBackgroundColor(null);
        } else if (color == 101) {
            btn.getStyle().setBackgroundColor("red");
        } else if (color == 0) {
            btn.getStyle().setBackgroundColor("green");
            btn.setText("0");
        } else if (color > 0 && color < 100) {
            btn.getStyle().setBackgroundColor("black");
            btn.setText(color + "");
        } else {
            System.out.println("------- Error setButtonColor ------");
        }
    }

    private void invertMatrix(int[][] matrix) {
        this.service = new ServiceGetAdjacencyMatrix(matrix);
        this.service.addInvertMatrixLisener(new InvertMatrixListener() {
            @Override
            public void onGetAdjacencyMatrix(int[][] graph) {
                // printGraph(graph);
                UI ui = getUI().orElseThrow();
                ui.access(() -> {
                    graphArea.setValue(Arrays.deepToString(graph).replace("], ", "],\n ")
                            .replace(Integer.MAX_VALUE + "", "∞"));
                });
            }

            @Override
            public void onError(boolean error) {
                if (error) {
                    // System.out.println("חסרה צומת התחלה");
                    UI ui = getUI().orElseThrow();
                    ui.access(() -> {
                        statusGraph.removeAll();
                        statusGraph.add("Invalid map");
                        statusGraph.getStyle().setColor("red");
                    });
                } else {
                    UI ui = getUI().orElseThrow();
                    ui.access(() -> {
                        statusGraph.removeAll();
                        statusGraph.add("good map");
                        statusGraph.getStyle().setColor("green");
                    });
                }
            }
        });
        this.service.startInvertMatrix();
    }
}
