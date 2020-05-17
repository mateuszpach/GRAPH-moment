package com.github.mtdrewski.GRAPH_moment.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {

    private static GraphDrawerController graphDrawerController;

    @FXML
    private BorderPane root;

    public void newProjectOnClick() {


        if (graphDrawerController.isUnsaved()) {
            Stage unsavedAlert = Stager.stageFactory.get();
            FXMLLoader fxmlLoader = Stager.initializeStage(
                    (Stage) root.getScene().getWindow(),
                    unsavedAlert,
                    "../view/unsaved_alert.fxml",
                    "Unsaved Changes",
                    600,
                    200);
            //TODO: save to file class
        }


        Stage newProjectStage = Stager.stageFactory.get();
        Stager.initializeStage(
                (Stage) root.getScene().getWindow(),
                newProjectStage,
                "../view/new_project_options.fxml",
                "New Graph Options",
                600,
                200);
    }

    public void generateOnClick() {
        Stage stage = Stager.stageFactory.get();
        Stager.initializeStage(
                (Stage) root.getScene().getWindow(),
                stage,
                "../view/generate.fxml",
                "Generate Options",
                400,
                720);
    }

    public void importOnClick() {
        Stage stage = Stager.stageFactory.get();
        Stager.initializeStage(
                (Stage) root.getScene().getWindow(),
                stage,
                "../view/import.fxml",
                "Import Options",
                800,
                600);
    }

    public void exportOnClick() {
        Stage stage = Stager.stageFactory.get();
        Stager.initializeStage(
                (Stage) root.getScene().getWindow(),
                stage,
                "../view/export.fxml",
                "Export Options",
                800,
                600);
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }
}