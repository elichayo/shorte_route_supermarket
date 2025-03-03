package elichayo.shorte_route_supermarket.pages;

import java.util.Arrays;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

import elichayo.shorte_route_supermarket.services.ServiceGetRoute;
import elichayo.shorte_route_supermarket.services.ServiceGetRoute.getRouteListener;

import com.vaadin.flow.component.notification.Notification;

@Route("")
public class PageHome extends VerticalLayout {
    final static int INF = Integer.MAX_VALUE;

    private ServiceGetRoute graph;
    private TextArea output;
    private ComboBox<String> comboBox;

    public PageHome(ServiceGetRoute graph) {
        this.graph = graph;

        graph.addGraphListener(new getRouteListener() {
            @Override
            public void onRouteReady(int[] route) {
                UI ui = getUI().orElseThrow();
                ui.access(() -> {
                    log("\nRoute: " + Arrays.toString(route));
                });
            }
        });

        comboBox = new ComboBox<>("Select graph");
        comboBox.setItems("Graph 1", "Graph 2", "Graph 3");

        // TextArea input = new TextArea("Input Map Matrix");
        Button buttonSend = new Button("send");
        // buttonSend.addClickListener(new ComponentEventListener<ClickEvent<Button>>()
        // {
        // @Override
        // public void onComponentEvent(ClickEvent<Button> event) {
        // findSortestPath();
        // }
        // });

        buttonSend.addClickListener(click -> findSortestPath());

        output = new TextArea("Output route");
        output.setSizeFull();
        output.setMaxWidth("600px");
        output.setReadOnly(true);

        add(new H1("Home"));
        // add(input);
        add(comboBox, buttonSend, output);
    }

    private void findSortestPath() {
        clearLog();

        switch (comboBox.getValue()) {
            case "Graph 1":
                graph.setup(ServiceGetRoute.GRAPH1);
                break;
            case "Graph 2":
                graph.setup(ServiceGetRoute.GRAPH2);
                break;
            case "Graph 3":
                graph.setup(ServiceGetRoute.GRAPH3);
                break;
            default:
                Notification.show("Error, please select a graph");
        }

        log("Matrix:\n"
                + Arrays.deepToString(graph.getGraph()).replace("], ", "],\n ").replace(ServiceGetRoute.INF + "", "∞"));

        graph.shortRoute();
    }

    private void log(String str) {
        output.setValue(output.getValue() + "\n" + str);
    }

    private void clearLog() {
        output.setValue("");
    }
}
