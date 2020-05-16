package com.github.mtdrewski.GRAPH_moment.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UnsavedAlertController {

    @FXML
    BorderPane root;

    public void save() {
        ((Stage) root.getScene().getWindow()).close();
    }

    //TODO: (IMPORTANT) Implement this method
    public void discard() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
