package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.processors.DataProcessor;
import com.github.mtdrewski.GRAPH_moment.model.processors.FileIOProcessor;
import com.jfoenix.controls.JFXButton;
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
    private FileIOProcessor fileIOProcessor = new FileIOProcessor();

    private enum State {
        BEFORE_EXPORT, AFTER_EXPORT;
    }

    State state = State.BEFORE_EXPORT;

    //TODO: avoid code duplication
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

    public void exportGraphDataToTextArea() {
        switch (state) {
            case BEFORE_EXPORT:
                try {
                    Graph graph = graphDrawerController.getGraph();
                    //TODO: implement makeOutputFromGraph and specify exception
                    String text = dataProcessor.makeOutputFromGraph(graph, graphType);
                    textArea.setText(text);
                } catch (Exception e /*DataProcessor.IncorrectFormatException e*/) {
                    alert("Graph export failed");
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
                ((Stage) root.getScene().getWindow()).close();
                break;
        }
    }

    public void browseAndSave() {
        Stage rootStage = (Stage) root.getScene().getWindow();
        boolean isSaved = false;
        try {
            isSaved = FileIOProcessor.saveAs(rootStage, textArea.getText());
        } catch (IOException e) {
            alert("Save failed");
        }
        if (isSaved) rootStage.close();
    }

    public void setGraphType() {
        JFXRadioButton selectedRadioButton = (JFXRadioButton) typeGroup.getSelectedToggle();
        String toogleGroupValue = selectedRadioButton.getText();
        if (toogleGroupValue.equals("Adjacency matrix"))
            graphType = DataProcessor.Type.ADJACENCY_MATRIX;
        else if (toogleGroupValue.equals("Incidence matrix"))
            graphType = DataProcessor.Type.INCIDENCE_MATRIX;
        else //toogleGroupValue.equals("Edges list")
            graphType = DataProcessor.Type.EDGE_LIST;
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }

}
