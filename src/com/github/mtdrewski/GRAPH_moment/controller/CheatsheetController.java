package com.github.mtdrewski.GRAPH_moment.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CheatsheetController {

    @FXML
    BorderPane root;

    public void okOnClick() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
