package com.github.mtdrewski.GRAPH_moment.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Supplier;

public class Menu {
    @FXML
    private VBox menu;

    private Supplier<Stage> dialogFactory = () -> {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) menu.getScene().getWindow());
        dialog.getIcons().add(new Image("icon.png"));
        return dialog;
    };


    //TODO: consider catching this exception
    //TODO: finish newProject window
    public void newProjectOnClick(ActionEvent event) throws IOException {
        Stage dialog = dialogFactory.get();

        Parent root = FXMLLoader.load(getClass().getResource("../view/newProject.fxml"));
        dialog.setTitle("GRAPH Moment");
        dialog.setScene(new Scene(root, 1200, 800));
        dialog.setMinWidth(800);
        dialog.setMinHeight(500);

        dialog.show();
    }

    public void importOnClick(ActionEvent event) throws IOException {
        Stage dialog = dialogFactory.get();
        Parent root = FXMLLoader.load(getClass().getResource("../view/import.fxml"));
        dialog.setTitle("Import Options");
        dialog.setScene(new Scene(root, 800, 500));
        dialog.setMinWidth(800);
        dialog.setMinHeight(500);

        dialog.show();
    }
}
