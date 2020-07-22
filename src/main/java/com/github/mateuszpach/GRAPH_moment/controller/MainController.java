package com.github.mateuszpach.GRAPH_moment.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {

    private static GraphDrawerController graphDrawerController;

    @FXML
    private BorderPane root;

    public void newProjectOnClick() {
        Stage rootStage = (Stage) root.getScene().getWindow();
        ReaderSaver.assureIsSaved(rootStage);
        if (ReaderSaver.checkIfAssuranceFailed()) return;

        Stage newProjectStage = Stager.stageFactory.get();
        Stager.initializeStage(
                (Stage) root.getScene().getWindow(),
                newProjectStage,
                "/view/new_project_options.fxml",
                "New Graph Options",
                600,
                200);
    }

    public void openProjectOnClick() {
        ReaderSaver.openProject((Stage) root.getScene().getWindow());
    }

    public void saveProjectOnClick() {
        ReaderSaver.saveProject((Stage) root.getScene().getWindow());
    }

    public void saveProjectAsOnClick() {
        ReaderSaver.saveProjectAs((Stage) root.getScene().getWindow());
    }

    public void generateOnClick() {
        Stage stage = Stager.stageFactory.get();
        Stager.initializeStage(
                (Stage) root.getScene().getWindow(),
                stage,
                "/view/generate.fxml",
                "Generate Options",
                400,
                750);
    }

    public void importOnClick() {
        Stage stage = Stager.stageFactory.get();
        Stager.initializeStage(
                (Stage) root.getScene().getWindow(),
                stage,
                "/view/import.fxml",
                "Import Options",
                800,
                600);
    }

    public void exportOnClick() {
        Stage stage = Stager.stageFactory.get();
        Stager.initializeStage(
                (Stage) root.getScene().getWindow(),
                stage,
                "/view/export.fxml",
                "Export Options",
                800,
                600);
    }

    public void cheatsheetOnClick() {
        Stage stage = Stager.stageFactory.get();
        Stager.initializeStage(
                (Stage) root.getScene().getWindow(),
                stage,
                "/view/cheatsheet.fxml",
                "Cheatsheet",
                700,
                500);
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }
}