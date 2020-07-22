package com.github.mateuszpach.GRAPH_moment.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ErrorAlertController {

    @FXML
    BorderPane root;

    @FXML
    Label errorLabel;

    public void okOnClick() {
        ((Stage) root.getScene().getWindow()).close();
    }

    public void setMessage(String message) {
        errorLabel.setText(message);
    }

}
