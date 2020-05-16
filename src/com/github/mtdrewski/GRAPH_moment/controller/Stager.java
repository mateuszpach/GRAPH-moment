package com.github.mtdrewski.GRAPH_moment.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Supplier;

public class Stager {

    protected static Supplier<Stage> stageFactory = () -> {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getIcons().add(new Image("icon.png"));
        return dialog;
    };

    protected static FXMLLoader initializeStage(Stage owner, Stage stage, String resource, String title, int width, int height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Stager.class.getResource(resource));
            Parent root = fxmlLoader.load();
            stage.initOwner(owner);
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.setMinWidth(width);
            stage.setMinHeight(height);
            stage.showAndWait();
            return fxmlLoader;
        } catch (IOException e) {
            System.err.println("ERROR: Could not initialize stage.");
        }
        return null;
    }

    protected static void alert(Stage owner, String message) {
        Stage stage = stageFactory.get();
        FXMLLoader fxmlLoader = initializeStage(owner, stage, "../view/error_alert.fxml", "Error", 400, 200);
        ((ErrorAlertController) fxmlLoader.getController()).setMessage(message);
    }
}
