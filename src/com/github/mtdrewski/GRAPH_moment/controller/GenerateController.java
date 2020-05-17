package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.generators.IntervalConstrainedGenerator;
import com.github.mtdrewski.GRAPH_moment.model.generators.KComponentGenerator;
import com.github.mtdrewski.GRAPH_moment.model.generators.StandardGraphGenerator;
import com.github.mtdrewski.GRAPH_moment.model.generators.TreeGenerator;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.utils.GraphMerger;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

//TODO: max components
public class GenerateController {

    private static GraphDrawerController graphDrawerController;

    private Map<String, IntervalConstrainedGenerator.type> typeConversion = new HashMap<>();
    private GraphMerger.Type mergeType;

    public void initGraphTypes() {
        typeConversion.put("Standard", IntervalConstrainedGenerator.type.STANDARD);
        typeConversion.put("Multicomponent", IntervalConstrainedGenerator.type.MULTICOMPONENT);
        typeConversion.put("Tree", IntervalConstrainedGenerator.type.TREE);
    }

    @FXML
    private BorderPane root;

    @FXML
    private JFXComboBox graphTypeSelector;

//    @FXML
//    private CheckBox isDirected; //TODO

    @FXML
    private JFXTextField minVertices;
    @FXML
    private JFXTextField maxVertices;
    @FXML
    private JFXTextField minEdges;
    @FXML
    private JFXTextField maxEdges;
    @FXML
    private JFXTextField minComponents;
    @FXML
    private JFXTextField maxComponents;
    @FXML
    private Label minVerticesLabel;
    @FXML
    private Label maxVerticesLabel;
    @FXML
    private Label minEdgesLabel;
    @FXML
    private Label maxEdgesLabel;
    @FXML
    private Label minComponentsLabel;
    @FXML
    private Label maxComponentsLabel;
    @FXML
    private ToggleGroup mergeGroup;

    public void initialize() {
        initGraphTypes();
        graphTypeSelector.getItems().addAll("Standard", "Multicomponent", "Tree");

        //TODO: make it looks better
        minEdges.setDisable(false);
        minEdgesLabel.setDisable(false);
        maxEdges.setDisable(false);
        maxEdgesLabel.setDisable(false);
        minComponents.setText("");
        minComponents.setDisable(true);
        minComponentsLabel.setDisable(true);
        maxComponents.setText("");
        maxComponents.setDisable(true);
        maxComponentsLabel.setDisable(true);

        graphTypeSelector.setOnAction(e -> {
            IntervalConstrainedGenerator.type type = typeConversion.get(graphTypeSelector.getValue());
            switch (type) {
                case STANDARD:
                    minEdges.setDisable(false);
                    minEdgesLabel.setDisable(false);
                    maxEdges.setDisable(false);
                    maxEdgesLabel.setDisable(false);
                    minComponents.setText("");
                    minComponents.setDisable(true);
                    minComponentsLabel.setDisable(true);
                    maxComponents.setText("");
                    maxComponents.setDisable(true);
                    maxComponentsLabel.setDisable(true);
                    break;
                case MULTICOMPONENT:
                    minEdges.setDisable(false);
                    minEdgesLabel.setDisable(false);
                    maxEdges.setDisable(false);
                    maxEdgesLabel.setDisable(false);
                    minComponents.setDisable(false);
                    minComponentsLabel.setDisable(false);
                    maxComponents.setDisable(false);
                    maxComponentsLabel.setDisable(false);
                    break;
                case TREE:
                    minEdges.setText("");
                    minEdges.setDisable(true);
                    minEdgesLabel.setDisable(true);
                    maxEdges.setText("");
                    maxEdges.setDisable(true);
                    maxEdgesLabel.setDisable(true);
                    minComponents.setText("");
                    minComponents.setDisable(true);
                    minComponentsLabel.setDisable(true);
                    maxComponents.setText("");
                    maxComponents.setDisable(true);
                    maxComponentsLabel.setDisable(true);
            }
        });
    }

    public void importAndGenerate() {

        IntervalConstrainedGenerator.type type = typeConversion.get(graphTypeSelector.getValue());
        Graph newGraph = null;

        switch (type) {
            case STANDARD:
                newGraph = generateStandard();
                break;
            case MULTICOMPONENT:
                newGraph = generateMulticomponent();
                break;
            case TREE:
                newGraph = generateTree();
                break;
            default:
                Stager.alert((Stage) root.getScene().getWindow(), "Specify the type of graph.");
                break;
        }

        ((Stage) root.getScene().getWindow()).close();

        Graph oldGraph = graphDrawerController.getGraph();
        switch (mergeType) {
            case UNION:
                graphDrawerController.drawNewGraph(GraphMerger.union(oldGraph, newGraph));
                break;
            case DISJOINT_UNION:
                graphDrawerController.drawNewGraph(GraphMerger.disjointUnion(oldGraph, newGraph));
                break;
        }
    }

    private Graph generateStandard() {
        try {
            String minV, maxV, minE, maxE;

            minV = minVertices.getText();
            maxV = maxVertices.getText();
            minE = minEdges.getText();
            maxE = maxEdges.getText();

            StandardGraphGenerator generator = null;

            if (minE.equals("") && maxE.equals("")) {
                generator = new StandardGraphGenerator(Integer.parseInt(minV), Integer.parseInt(maxV));
            } else if (!minE.equals("") && !maxE.equals("")) {
                generator = new StandardGraphGenerator(Integer.parseInt(minV), Integer.parseInt(maxV),
                        Integer.parseInt(minE), Integer.parseInt(maxE));
            }

            if (generator == null)
                throw new IllegalArgumentException("Some field should or shouldn't be empty and isn't as it should");

            return generator.generate();
        } catch (IllegalArgumentException e) {
            Stager.alert((Stage) root.getScene().getWindow(), "Wrong input");
        }
        return null;
    }

    private Graph generateMulticomponent() {
        try {
            String minV, maxV, minE, maxE, minC, maxC;

            minV = minVertices.getText();
            maxV = maxVertices.getText();
            minE = minEdges.getText();
            maxE = maxEdges.getText();
            minC = minComponents.getText();
            maxC = maxComponents.getText();

            KComponentGenerator generator = null;

            if (minE.equals("") && maxE.equals("")) {
                if (minC.equals(""))
                    generator = new KComponentGenerator(Integer.parseInt(minV), Integer.parseInt(maxV));
                else
                    generator = new KComponentGenerator(Integer.parseInt(minV), Integer.parseInt(maxV), Integer.parseInt(minC), Integer.parseInt(maxC));
            } else if (!minE.equals("") && !maxE.equals("")) {
                if (minC.equals(""))
                    generator = new KComponentGenerator(Integer.parseInt(minV), Integer.parseInt(maxV),
                            Integer.parseInt(minE), Integer.parseInt(maxE));
                else
                    generator = new KComponentGenerator(Integer.parseInt(minV), Integer.parseInt(maxV),
                            Integer.parseInt(minE), Integer.parseInt(maxE), Integer.parseInt(minC), Integer.parseInt(maxC));
            }
            if (generator == null)
                throw new IllegalArgumentException("Some field should or shouldn't be empty and isn't as it should");

            return generator.generate();
        } catch (IllegalArgumentException e) {
            Stager.alert((Stage) root.getScene().getWindow(), "Wrong input");
        }
        return null;
    }

    private Graph generateTree() {
        try {
            String minV, maxV;

            minV = minVertices.getText();
            maxV = maxVertices.getText();

            TreeGenerator generator = new TreeGenerator(Integer.parseInt(minV), Integer.parseInt(maxV));
            return generator.generate();
        } catch (IllegalArgumentException e) {
            Stager.alert((Stage) root.getScene().getWindow(), "Wrong input");
        }
        return null;
    }

    public void setMergeType() {
        switch (((JFXRadioButton) mergeGroup.getSelectedToggle()).getText()) {
            case "Union graph":
                mergeType = GraphMerger.Type.UNION;
                break;
            case "Renumber new graph":
                mergeType = GraphMerger.Type.DISJOINT_UNION;
                break;
        }
    }

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }
}