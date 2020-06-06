package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.processors.DataProcessor;
import com.github.mtdrewski.GRAPH_moment.model.processors.FileIOProcessor;
import com.github.mtdrewski.GRAPH_moment.model.utils.GraphMerger;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ImportController {

    private static GraphDrawerController graphDrawerController;

    @FXML
    private BorderPane root;
    @FXML
    private TextArea textArea;
    @FXML
    ToggleGroup typeGroup;
    @FXML
    ToggleGroup mergeGroup;

    DataProcessor.Type graphType;
    GraphMerger.Type mergeType;

    private DataProcessor dataProcessor = new DataProcessor();

    public void importGraphDataFromTextArea() {
        Stage rootStage = (Stage) root.getScene().getWindow();
        String textInput = textArea.getText();
        Graph newGraph;
        try {
            newGraph = dataProcessor.makeGraphFromInput(textInput, graphType);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            Stager.alert(rootStage, "Incorrect input format");
            return;
        }
        rootStage.close();

        Graph oldGraph = graphDrawerController.getGraph();
        switch (mergeType) {
            case UNION:
                graphDrawerController.drawNewGraph(GraphMerger.union(oldGraph, newGraph, graphDrawerController.getDirected()), true);
                break;
            case DISJOINT_UNION:
                graphDrawerController.drawNewGraph(GraphMerger.disjointUnion(oldGraph, newGraph, graphDrawerController.getDirected()), true);
                break;
            case REPLACE:
                graphDrawerController.drawNewGraph(newGraph, true);
                break;
            default:
                break;
        }
    }

    public void browseAndPull() {
        Stage rootStage = (Stage) root.getScene().getWindow();
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(rootStage);
            if (file == null) return;
            textArea.setText(FileIOProcessor.read(file));
        } catch (IllegalArgumentException e) {
            Stager.alert(rootStage, "Pull failed");
        }
    }

    public void setGraphType() {
        switch (((JFXRadioButton) typeGroup.getSelectedToggle()).getText()) {
            case "Adjacency matrix":
                graphType = DataProcessor.Type.ADJACENCY_MATRIX;
                break;
            case "Incidence matrix":
                graphType = DataProcessor.Type.INCIDENCE_MATRIX;
                break;
            case "Edges list":
                graphType = DataProcessor.Type.EDGE_LIST;
                break;
        }
    }

    public void setMergeType() {
        switch (((JFXRadioButton) mergeGroup.getSelectedToggle()).getText()) {
            case "Union graph":
                mergeType = GraphMerger.Type.UNION;
                break;
            case "Renumber graph":
                mergeType = GraphMerger.Type.DISJOINT_UNION;
                break;
            case "Replace with graph":
                mergeType = GraphMerger.Type.REPLACE;
                break;
        }
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }

}