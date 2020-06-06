package com.github.mtdrewski.GRAPH_moment;

import com.github.mtdrewski.GRAPH_moment.controller.GraphDrawerController;
import com.github.mtdrewski.GRAPH_moment.controller.ReaderSaver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static GraphDrawerController graphDrawerController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));
        primaryStage.setTitle("Untitled [undirected] - GRAPH Moment");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(800);
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            ReaderSaver.assureIsSaved(primaryStage);
            if (ReaderSaver.checkIfAssuranceFailed()) event.consume();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }
}
