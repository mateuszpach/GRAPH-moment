package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.Main;
import com.github.mtdrewski.GRAPH_moment.model.generators.IntervalConstrainedGenerator;
import com.github.mtdrewski.GRAPH_moment.model.graphs.DirectedGraph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;
import com.github.mtdrewski.GRAPH_moment.model.processors.DataProcessor;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class GraphDrawerController {

    @FXML
    private AnchorPane root;

    private File file = null;

    protected boolean cursorOverVertex = false;
    protected boolean cursorOverEdge = false;
    private boolean isUnsaved = true;

    private enum Mode {EDGE, SELECT, STANDARD, TYPING}

    private boolean isDirected = false;

    private Mode mode;
    private EdgeLine currentEdge;

    private VertexCircle sourceVertex = null;
    protected ArrayList<VertexCircle> selectedVertices;
    protected ArrayList<EdgeLine> selectedEdges;
    protected ArrayList<VertexCircle> verticesOnScene;
    protected ArrayList<EdgeLine> edgesOnScene;

    private Graph graph;
    private double maxX, maxY;
    public double getMaxX() { return maxX; }
    public double getMaxY() { return maxY; }

    public boolean isUnsaved() {
        return isUnsaved;
    }
    public void setUnsaved(boolean b) {
        isUnsaved = b;
    }

    public boolean isDirected() {
        return isDirected;
    }
    public void setDirected(boolean value) {
        isDirected = value;
    }

    public boolean isInEdgeMode() { return mode == Mode.EDGE; }
    public boolean isInSelectMode() {
        return mode == Mode.SELECT;
    }
    public boolean isInStandardMode() {
        return mode == Mode.STANDARD;
    }


    protected EdgeLine getCurrentEdge() { return currentEdge; }
    protected VertexCircle getSourceVertex() {
        return sourceVertex;
    }
    protected AnchorPane getRoot() {
        return root;
    }

    private final Supplier<VertexCircle> vertexShapeFactory = () -> {
        VertexCircle vertex = new VertexCircle(this);
        vertex.prepareLooks();
        return vertex;
    };

    private final Supplier<EdgeLine> edgeLineFactory = () -> {
        EdgeLine edge;
        if (!isDirected)
            edge = new EdgeLine(this);
        else
            edge = new DirectedEdgeLine(this);
        edge.setStartVertex(sourceVertex);
        edge.prepareLooks();
        return edge;
    };

    public void initialize() {
        Main.setGraphDrawerController(this);
        MainController.setGraphDrawerController(this);
        NewProjectOptionsController.setGraphDrawerController(this);
        ReaderSaver.setGraphDrawerController(this);
        GenerateController.setGraphDrawerController(this);
        ImportController.setGraphDrawerController(this);
        ExportController.setGraphDrawerController(this);
        DataProcessor.setGraphDrawerController(this);
        IntervalConstrainedGenerator.setGraphDrawerController(this);

        graph = new Graph();
        mode = Mode.STANDARD;
        selectedVertices = new ArrayList<>();
        selectedEdges = new ArrayList<>();
        verticesOnScene = new ArrayList<>();
        edgesOnScene = new ArrayList<>();

        root.setOnMousePressed(e -> {
            maxX = root.getWidth() - 25.0;
            maxY = root.getHeight() - 25.0;

            if (e.getButton().equals(MouseButton.PRIMARY)) {

                if (mode == Mode.EDGE && !cursorOverVertex) {
                    exitEdgeMode(false);
                }

                if (mode == Mode.SELECT && !cursorOverVertex && !cursorOverEdge) {
                    deselectAll();
                }

                if (mode == Mode.STANDARD && e.getClickCount() == 2 && !cursorOverVertex) {
                    VertexCircle vertexShape = vertexShapeFactory.get();
                    vertexShape.setPosition(e);
                    vertexShape.appearOnScene();
                }
            }
        });
        root.setOnMouseMoved(e -> {
            if (currentEdge != null && mode == Mode.EDGE)
                currentEdge.followCursor(e);
        });
        root.setOnMouseDragged(e -> {
            if (currentEdge != null && mode == Mode.EDGE)
                currentEdge.followCursor(e);
        });
        root.setFocusTraversable(true);
        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.CONTROL && mode == Mode.STANDARD) {
                mode = Mode.SELECT;
            } else if (e.getCode() == KeyCode.DELETE) {
                deleteAll();
            } else if (e.getCode() == KeyCode.ALT && mode == Mode.STANDARD) {
                mode = Mode.TYPING;
                for (EdgeLine edge : edgesOnScene) {
                    edge.editLabel(true);
                    edge.moveLabel();
                }
            } else if (e.getCode() == KeyCode.ALT && mode == Mode.TYPING) {
                mode = Mode.STANDARD;
                for (EdgeLine edge : edgesOnScene) {
                    edge.editLabel(false);
                    edge.underlyingEdge.setLabel(edge.label.getText());
                }
            }
        });
        root.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.CONTROL && mode == Mode.SELECT) {
                mode = Mode.STANDARD;
            }
        });
    }

    protected void enterEdgeMode(VertexCircle vertex, MouseEvent event) {
        sourceVertex = vertex;
        EdgeLine edge = edgeLineFactory.get();
        sourceVertex.addOutcomingEdge(edge);
        edge.followCursor(event);
        edge.appearOnScene();
        currentEdge = edge;
        mode = Mode.EDGE;
    }

    protected void exitEdgeMode(boolean success) {
        if (!success) {
            sourceVertex.removeOutcomingEdge(currentEdge);
            currentEdge.disappearFromScene();
        }

        currentEdge = null;
        sourceVertex = null;
        mode = Mode.STANDARD;
    }


    protected void deselectAll() {
        ArrayList<VertexCircle> toDeselectV = new ArrayList<>(selectedVertices);
        toDeselectV.stream().forEach(VertexCircle::deselect);
        ArrayList<EdgeLine> toDeselectE = new ArrayList<>(selectedEdges);
        toDeselectE.stream().forEach(EdgeLine::deselect);
    }

    private void deleteAll() {
        isUnsaved = true;

        selectedEdges.forEach(e -> {
            graph.removeEdge(e.startVertex.id(), e.endVertex.id());
            e.startVertex.getOutcomingEdges().remove(e);
            e.endVertex.getOutcomingEdges().remove(e);
            e.disappearFromScene();
        });

        selectedVertices.forEach(v -> {
            v.getOutcomingEdges().forEach(EdgeLine::disappearFromScene);
            v.disappearFromScene();
            graph.removeVertex(v.id());
        });

        verticesOnScene.forEach(VertexCircle::refresh);

        selectedVertices.clear();
        selectedEdges.clear();
    }

    public void drawNewGraph(Graph newGraph) {
        isUnsaved = true;
        edgesOnScene.clear();
        verticesOnScene.clear();

        root.getChildren().clear();
        if (isDirected)
            graph = new DirectedGraph();
        else
            graph = new Graph();

        ArrayList<VertexCircle> vertexCircles = new ArrayList<>();

        for (Vertex graphVertex : newGraph.getVertices()) {
            VertexCircle vertexShape = vertexShapeFactory.get();
            vertexShape.setPosition(graphVertex.xPos(), graphVertex.yPos());
            vertexCircles.add(vertexShape);
            vertexShape.appearOnScene();
        }

        for (Edge graphEdge : newGraph.getEdges()) {

            EdgeLine edgeLine;
            if (isDirected)
                edgeLine = new DirectedEdgeLine(this);
            else
                edgeLine = new EdgeLine(this);

            edgeLine.prepareLooks();
            edgeLine.setStartVertex(vertexCircles.get(graphEdge.vert1().id() - 1));
            vertexCircles.get(graphEdge.vert1().id() - 1).addOutcomingEdge(edgeLine);

            edgeLine.appearOnScene();

            edgeLine.setEndVertex(vertexCircles.get(graphEdge.vert2().id() - 1));
            vertexCircles.get(graphEdge.vert2().id() - 1).addOutcomingEdge(edgeLine);
        }

        currentEdge = null;
        sourceVertex = null;
    }

    public Graph getGraph() {
        return graph;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        StringBuilder title = new StringBuilder();

        if (file == null) {
            title.append("Untitled");
        } else {
            title.append(file.getName());
        }

        if (isDirected) {
            title.append(" [directed] - GRAPH Moment");
        } else {
            title.append(" [undirected] - GRAPH Moment");
        }

        ((Stage) root.getScene().getWindow()).setTitle(title.toString());

        this.file = file;
    }

    public Pair<Double, Double> getRandomCoords() {
        Random random = new Random();
        return new Pair<>(
                random.nextDouble() * (root.getWidth() - 50) + 25, random.nextDouble() * (root.getHeight() - 50) + 25
        );
    }
}
