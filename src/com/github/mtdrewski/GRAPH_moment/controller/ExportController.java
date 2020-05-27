package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.processors.DataProcessor;
import com.github.mtdrewski.GRAPH_moment.model.processors.FileIOProcessor;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ExportController {

    private static GraphDrawerController graphDrawerController;

    @FXML
    private BorderPane root;
    @FXML
    private TextArea textArea;
    @FXML
    private JFXButton exportButton;
    @FXML
    private JFXButton browseButton;
    @FXML
    private ToggleGroup typeGroup;
    @FXML
    private JFXRadioButton radioButton1;
    @FXML
    private JFXRadioButton radioButton2;
    @FXML
    private JFXRadioButton radioButton3;

    DataProcessor.Type graphType;

    private DataProcessor dataProcessor = new DataProcessor();

    private enum State {
        BEFORE_EXPORT, AFTER_EXPORT;
    }

    State state = State.BEFORE_EXPORT;

    public void exportGraphDataToTextArea() {
        Stage rootStage = (Stage) root.getScene().getWindow();
        switch (state) {
            case BEFORE_EXPORT:
                Graph graph = graphDrawerController.getGraph();

                try {
                    String text = dataProcessor.makeOutputFromGraph(graph, graphType);
                    textArea.setText(text);
                } catch (NullPointerException e) {
                    Stager.alert(rootStage, "Format not specified");
                    return;
                }

                exportButton.getStyleClass().remove("green-button");
                exportButton.getStyleClass().add("red-button");
                exportButton.setText("Close");
                browseButton.setDisable(false);
                radioButton1.setDisable(true);
                radioButton2.setDisable(true);
                radioButton3.setDisable(true);

                state = State.AFTER_EXPORT;
                break;
            case AFTER_EXPORT:
                rootStage.close();
                break;
        }
    }

    public void browseAndSave() {
        Stage rootStage = (Stage) root.getScene().getWindow();
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(rootStage);
            FileIOProcessor.save(textArea.getText(), file);
            rootStage.close();
        } catch (IllegalArgumentException e) {
            Stager.alert(rootStage, "Save failed");
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

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }

}
