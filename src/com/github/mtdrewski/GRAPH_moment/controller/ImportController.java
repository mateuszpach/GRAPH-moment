package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.dataProcessor.DataProcessor;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.util.zip.Adler32;

public class ImportController {

    private static GraphDrawerController graphDrawerController;

    @FXML
    private TextArea textArea;

    @FXML
    ToggleGroup typeGroup;

    DataProcessor.Type graphType;

    private DataProcessor dataProcessor = new DataProcessor();

    public void importGraphDataFromTextArea() throws IOException {
        String textInput = textArea.getText();
        Graph graph;
        try {
            graph = dataProcessor.makeGraphFromInput(textInput, graphType);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            System.out.println(e.getMessage());         //TODO: maybe make this message as pop-up
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