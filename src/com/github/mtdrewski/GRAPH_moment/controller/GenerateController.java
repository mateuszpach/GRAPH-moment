package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.generators.IntervalConstrainedGenerator;
import com.github.mtdrewski.GRAPH_moment.model.generators.KComponentGenerator;
import com.github.mtdrewski.GRAPH_moment.model.generators.StandardGraphGenerator;
import com.github.mtdrewski.GRAPH_moment.model.generators.TreeGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class GenerateController {

    private static GraphDrawerController graphDrawerController;

    private Map<String, IntervalConstrainedGenerator.type> typeConversion;

    public void initGraphTypes() {
        typeConversion.put("Standard", IntervalConstrainedGenerator.type.STANDARD);
        typeConversion.put("Multicomponent", IntervalConstrainedGenerator.type.MULTICOMPONENT);
        typeConversion.put("Tree", IntervalConstrainedGenerator.type.TREE);
    }

    @FXML
    private BorderPane root;

    @FXML
    private ChoiceBox<String> graphTypeSelector;

    @FXML
    private CheckBox isDirected; //TODO

    @FXML
    private TextArea minVertices;
    @FXML
    private TextArea maxVertices;
    @FXML
    private TextArea minEdges;
    @FXML
    private TextArea maxEdges;
    @FXML
    private TextArea numOfComponents;

    public void initialize() {
        initGraphTypes();
        graphTypeSelector.getItems().addAll("Standard", "Multicomponent", "Tree");
        graphTypeSelector.setOnAction(e -> {
            IntervalConstrainedGenerator.type type = typeConversion.get(graphTypeSelector.getValue());
            switch (type) {
                case STANDARD:
                    minEdges.setEditable(true);
                    maxEdges.setEditable(true);
                    numOfComponents.setText("");
                    numOfComponents.setEditable(false);
                    break;
                case MULTICOMPONENT:
                    minEdges.setEditable(true);
                    maxEdges.setEditable(true);
                    numOfComponents.setEditable(false);
                    break;
                case TREE:
                    minEdges.setText("");
                    minEdges.setEditable(false);
                    maxEdges.setText("");
                    maxEdges.setEditable(false);
                    numOfComponents.setText("");
                    numOfComponents.setEditable(false);
            }
        });
    }

    public void importAndGenerate() {

        IntervalConstrainedGenerator.type type = typeConversion.get(graphTypeSelector.getValue());

        switch (type) {
            case STANDARD:
                generateStandard();
                break;
            case MULTICOMPONENT:
                generateMulticomponent();
                break;
            case TREE:
                generateTree();
                break;
            default:
                alert("Specify the type of graph.");
                break;
        }
    }

    private void generateStandard() {
        try {
            String minV, maxV, minE, maxE;

            minV = minVertices.getText();
            maxV = maxVertices.getText();
            minE = minEdges.getText();
            maxE = maxEdges.getText();

            StandardGraphGenerator generator = null;

            if (minE.equals("") && maxE.equals("")) {
                generator = new StandardGraphGenerator(Integer.parseInt(minV), Integer.parseInt(maxV));
            }
            else if (!minE.equals("") && !maxE.equals("")) {
                generator = new StandardGraphGenerator(Integer.parseInt(minV), Integer.parseInt(maxV),
                                                       Integer.parseInt(minE), Integer.parseInt(maxE));
            }

            if (generator == null)
                throw new IllegalArgumentException("Some field should or shouldn't be empty and isn't as it should");

            graphDrawerController.drawNewGraph(generator.generate());
        }
        catch (IllegalArgumentException e) {
            alert("Wrong input");
        }
    }

    private void generateMulticomponent() {
        try {
            String minV, maxV, minE, maxE, cNum;

            minV = minVertices.getText();
            maxV = maxVertices.getText();
            minE = minEdges.getText();
            maxE = maxEdges.getText();
            cNum = numOfComponents.getText();

            KComponentGenerator generator = null;

            if (minE.equals("") && maxE.equals("")) {
                if (cNum.equals(""))
                    generator = new KComponentGenerator(Integer.parseInt(minV), Integer.parseInt(maxV));
                else
                    generator = new KComponentGenerator(Integer.parseInt(minV), Integer.parseInt(maxV), Integer.parseInt(cNum));
            }
            else if (!minE.equals("") && !maxE.equals("")) {
                if (cNum.equals(""))
                    generator = new KComponentGenerator(Integer.parseInt(minV), Integer.parseInt(maxV),
                                                        Integer.parseInt(minE), Integer.parseInt(maxE));
                else
                    generator = new KComponentGenerator(Integer.parseInt(minV), Integer.parseInt(maxV),
                                                        Integer.parseInt(minE), Integer.parseInt(maxE), Integer.parseInt(cNum));
            }

            if (generator == null)
                throw new IllegalArgumentException("Some field should or shouldn't be empty and isn't as it should");

            graphDrawerController.drawNewGraph(generator.generate());
        }
        catch (IllegalArgumentException e) {
            alert("Wrong input");
        }
    }

    private void generateTree() {
        try {
            String minV, maxV;

            minV = minVertices.getText();
            maxV = maxVertices.getText();

            TreeGenerator generator = new TreeGenerator(Integer.parseInt(minV), Integer.parseInt(maxV));
            graphDrawerController.drawNewGraph(generator.generate());
        }
        catch (IllegalArgumentException e) {
            alert("Wrong input");
        }
    }

    private void alert(String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) root.getScene().getWindow());
        dialog.getIcons().add(new Image("icon.png"));
        dialog.setTitle("Error");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/error_alert.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((ErrorAlertController) fxmlLoader.getController()).setMessage(message);

        dialog.setScene(new Scene(root, 600, 400));
        dialog.setMinWidth(400);
        dialog.setMinHeight(600);
        dialog.show();
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }
}