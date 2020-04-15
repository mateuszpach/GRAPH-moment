package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.dataProcessor.DataProcessor;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ImportController {

    @FXML
    TextArea textArea;

    public GraphDrawer graphDrawer;

    DataProcessor dataProcessor = new DataProcessor();

    public void importGraphOnClick() throws IOException {
        String textInput = textArea.getText();
        Graph graph;
        try {
            graph = dataProcessor.makeGraphFromInput(textInput, DataProcessor.Type.EDGE_LIST);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            System.out.println(e.getMessage());         //TODO: maybe make this message as pop-up
            return;
        }
        ((Stage) textArea.getScene().getWindow()).close();
        System.out.println(graph);

    }
    public void canYouHearMe(){
        System.out.println("hello hello hello");
    }

}