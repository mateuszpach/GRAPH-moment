package com.github.mtdrewski.GRAPH_moment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/main.fxml"));
        Parent root1 = fxmlLoader.load();
        primaryStage.setTitle("GRAPH Moment");
        primaryStage.setScene(new Scene(root1,1200,800));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}