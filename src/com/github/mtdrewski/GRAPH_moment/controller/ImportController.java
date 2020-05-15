package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.processors.DataProcessor;
import com.github.mtdrewski.GRAPH_moment.model.processors.FileIOProcessor;
import com.github.mtdrewski.GRAPH_moment.model.utils.GraphMerger;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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

    private void alert(String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) root.getScene().getWindow());
        dialog.getIcons().add(new Image("icon.png"));
        dialog.setTitle("Error");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/error_alert.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((ErrorAlertController) fxmlLoader.getController()).setMessage(message);

        dialog.setScene(new Scene(root, 400, 200));
        dialog.setMinWidth(400);
        dialog.setMinHeight(200);
        dialog.show();
    }

    public void importGraphDataFromTextArea() {
        String textInput = textArea.getText();
        Graph newGraph;
        try {
            newGraph = dataProcessor.makeGraphFromInput(textInput, graphType);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            alert("Incorrect input format");
            return;
        }
        ((Stage) root.getScene().getWindow()).close();

        Graph oldGraph = graphDrawerController.getGraph();
        switch (mergeType) {
            case UNION:
                graphDrawerController.drawNewGraph(GraphMerger.union(oldGraph, newGraph));
                break;
            case DISJOINT_UNION:
                graphDrawerController.drawNewGraph(GraphMerger.disjointUnion(oldGraph, newGraph));
                break;
        }
    }

    public void browseAndPull() {
        Stage rootStage = (Stage) root.getScene().getWindow();
        try {
            textArea.setText(FileIOProcessor.pullWithChoice(rootStage));
        } catch (FileIOProcessor.CancelledException e) {
        } catch (IOException e) {
            alert("Pull failed");
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
            case "Renumber new graph":
                mergeType = GraphMerger.Type.DISJOINT_UNION;
                break;
        }
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }

}