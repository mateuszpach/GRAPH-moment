package com.github.mtdrewski.GRAPH_moment.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UnsavedAlertController {

    @FXML
    BorderPane root;

    public void save() {
        Stage rootStage = (Stage) root.getScene().getWindow();
        boolean result = ReaderSaver.saveProject(rootStage);
        if (!result) ReaderSaver.assuranceFailed();
        rootStage.close();
    }

    public void discard() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
