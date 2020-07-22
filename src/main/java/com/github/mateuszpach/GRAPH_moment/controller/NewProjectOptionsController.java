package com.github.mateuszpach.GRAPH_moment.controller;

import com.github.mateuszpach.GRAPH_moment.model.graphs.DirectedGraph;
import com.github.mateuszpach.GRAPH_moment.model.graphs.Graph;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NewProjectOptionsController {

    private static GraphDrawerController graphDrawerController;

    @FXML
    private BorderPane root;

    public void undirected() {
        graphDrawerController.setUnsaved(true);
        graphDrawerController.setDirected(false);
        graphDrawerController.setFile(null);
        graphDrawerController.drawNewGraph(new Graph());
        ((Stage) root.getScene().getWindow()).close();
    }

    public void directed() {
        graphDrawerController.setUnsaved(true);
        graphDrawerController.setDirected(true);
        graphDrawerController.setFile(null);
        graphDrawerController.drawNewGraph(new DirectedGraph());
        ((Stage) root.getScene().getWindow()).close();
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }
}
