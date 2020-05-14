package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.processors.DataProcessor;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ImportController {

    private static GraphDrawerController graphDrawerController;

    @FXML
    private TextArea textArea;

    @FXML
    ToggleGroup typeGroup;

    DataProcessor.Type graphType;

    private DataProcessor dataProcessor = new DataProcessor();

    private void alert(String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) textArea.getScene().getWindow());
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

    public void importGraphDataFromTextArea() throws IOException {
        String textInput = textArea.getText();
        Graph graph;
        try {
            //TODO: let user have spaces before endl
            graph = dataProcessor.makeGraphFromInput(textInput, graphType);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            alert("Incorrect input format");
            return;
        }
        ((Stage) textArea.getScene().getWindow()).close();
        graphDrawerController.drawNewGraph(graph);
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

    //TODO: implement merge response

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }


}