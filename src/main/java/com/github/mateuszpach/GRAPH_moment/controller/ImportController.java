package com.github.mateuszpach.GRAPH_moment.controller;

import com.github.mateuszpach.GRAPH_moment.model.graphs.Graph;
import com.github.mateuszpach.GRAPH_moment.model.processors.DataProcessor;
import com.github.mateuszpach.GRAPH_moment.model.processors.FileIOProcessor;
import com.github.mateuszpach.GRAPH_moment.model.utils.GraphEmbedder;
import com.github.mateuszpach.GRAPH_moment.model.utils.GraphMerger;
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
        GraphEmbedder.fruchtermanReingoldLayout(newGraph, graphDrawerController.getRoot());
        GraphMerger.drawAccordingToMergeType(graphDrawerController, mergeType, oldGraph, newGraph);
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
            case "Edges list":
                graphType = DataProcessor.Type.EDGE_LIST;
                break;
        }
    }

    public void setMergeType() {
        mergeType = GraphMerger.recognizeMergeType(((JFXRadioButton) mergeGroup.getSelectedToggle()).getText());
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }

}