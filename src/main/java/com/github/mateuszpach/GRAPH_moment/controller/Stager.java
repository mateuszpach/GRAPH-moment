package com.github.mateuszpach.GRAPH_moment.controller;

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
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("/img/icon.png"));
        return stage;
    };

    protected static FXMLLoader initializeStage(Stage owner, Stage stage, String resource, String title, int width, int height, boolean wait) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Stager.class.getResource(resource));
            Parent root = fxmlLoader.load();
            stage.initOwner(owner);
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.setMinWidth(width);
            stage.setMinHeight(height);
            if (wait) {
                stage.showAndWait();
            } else {
                stage.show();
            }
            return fxmlLoader;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("ERROR: Could not initialize stage.");
        }
        return null;
    }

    protected static FXMLLoader initializeStage(Stage owner, Stage stage, String resource, String title, int width, int height) {
        return initializeStage(owner, stage, resource, title, width, height, true);
    }

    protected static void alert(Stage owner, String message) {
        Stage stage = stageFactory.get();
        FXMLLoader fxmlLoader = initializeStage(owner, stage, "/view/error_alert.fxml", "Error", 400, 200, false);
        ((ErrorAlertController) fxmlLoader.getController()).setMessage(message);
    }
}
