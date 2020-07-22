package com.github.mateuszpach.GRAPH_moment.controller;

import com.github.mateuszpach.GRAPH_moment.model.graphs.Graph;
import com.github.mateuszpach.GRAPH_moment.model.processors.DataProcessor;
import com.github.mateuszpach.GRAPH_moment.model.processors.FileIOProcessor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ReaderSaver {

    private static GraphDrawerController graphDrawerController;
    private static DataProcessor dataProcessor = new DataProcessor();
    private static boolean assuranceFailed;

    public static boolean openProject(Stage stage) {
        assureIsSaved(stage);
        if (checkIfAssuranceFailed()) return false;
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file == null) return false;
            String graphInput = FileIOProcessor.read(file);
            Graph graph = dataProcessor.makeFullGraphFromInput(graphInput);
            graphDrawerController.drawNewGraph(graph);
            graphDrawerController.setFile(file);
            graphDrawerController.setUnsaved(false);
            return true;
        } catch (Exception e) {
            Stager.alert(stage, "Read failed");
        }
        return false;
    }

    public static boolean saveProject(Stage stage) {
        File file = graphDrawerController.getFile();
        if (graphDrawerController.getFile() == null) {
            saveProjectAs(stage);
            return true;
        } else {
            try {
                FileIOProcessor.save(dataProcessor.makeFullOutputFromGraph(graphDrawerController.getGraph()), file);
                graphDrawerController.setUnsaved(false);
                return true;
            } catch (IllegalArgumentException e) {
                Stager.alert(stage, "Save failed");
            }
        }
        return false;
    }

    public static boolean saveProjectAs(Stage stage) {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(stage);
            if (file == null) return false;
            FileIOProcessor.save(dataProcessor.makeFullOutputFromGraph(graphDrawerController.getGraph()), file);
            graphDrawerController.setFile(file);
            graphDrawerController.setUnsaved(false);
            return true;
        } catch (IllegalArgumentException e) {
            Stager.alert(stage, "Save failed");
        }
        return false;
    }

    public static void assureIsSaved(Stage stage) {
        assuranceFailed = false;
        if (graphDrawerController.isUnsaved()) {
            Stage unsavedAlert = Stager.stageFactory.get();
            Stager.initializeStage(
                    stage,
                    unsavedAlert,
                    "/view/unsaved_alert.fxml",
                    "Unsaved Changes",
                    600,
                    200);
        }
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }

    public static void assuranceFailed() {
        assuranceFailed = true;
    }

    public static boolean checkIfAssuranceFailed() {
        return assuranceFailed;
    }
}
