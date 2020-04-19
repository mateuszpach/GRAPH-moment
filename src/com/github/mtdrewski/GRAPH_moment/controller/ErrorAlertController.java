package com.github.mtdrewski.GRAPH_moment.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class ErrorAlertController {

    @FXML
    Label errorLabel;

    public void okOnClick(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public void setMessage(String message) {
        errorLabel.setText(message);
    }

}
