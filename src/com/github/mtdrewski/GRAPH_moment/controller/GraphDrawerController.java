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

    private boolean cursorOverVertex = false;
    private boolean isUnsaved = false;

    private enum Mode {EDGE, SELECT, STANDARD}
    protected boolean isDirected = false;

    Mode mode;
    private EdgeLine currentEdge;

    private VertexCircle sourceVertex = null;
    private ArrayList<VertexCircle> selectedVertices;

    private Graph graph;

    public boolean isUnsaved() {
        return isUnsaved;
    }

    public void setUnsaved(boolean b) {
        isUnsaved = b;
    }

    public void setDirected(boolean value) { isDirected = value; }
    public boolean getDirected() { return isDirected; }

    public void setCursorOverVertex(boolean value) {
        cursorOverVertex = value;
    }

    public boolean isInEdgeMode() {
        return mode == Mode.EDGE;
    }

    public boolean isInSelectMode() {
        return mode == Mode.SELECT;
    }

    public boolean isInStandardMode() {
        return mode == Mode.STANDARD;
    }

    protected EdgeLine getCurrentEdge() {
        return currentEdge;
    }
    protected VertexCircle getSourceVertex() {
        return sourceVertex;
    }

    protected AnchorPane getRoot() { return root; }

    private Supplier<VertexCircle> vertexShapeFactory = () -> {
        VertexCircle vertex = new VertexCircle(this);
        vertex.prepareLooks();
        vertex.createBehaviour();
        return vertex;
    };

    public Supplier<EdgeLine> edgeLineFactory = () -> {
        EdgeLine edge = new EdgeLine(this);
        edge.prepareLooks();
        edge.setStartVertex(sourceVertex);
        return edge;
    };

    public Supplier<DirectedEdgeLine> directedEdgeLineFactory = () -> {
        DirectedEdgeLine edge = new DirectedEdgeLine(this);
        edge.prepareLooks();
        edge.setStartVertex(sourceVertex);
        return edge;
    };

    public void initialize() {
        //TODO: Maybe GraphDrawer could remember own instance in some static field
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

        root.setOnMousePressed(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {

                if (mode == Mode.EDGE && !cursorOverVertex) {
                    exitEdgeMode(false);
                }

                if (mode == Mode.SELECT && !cursorOverVertex) {
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
            }
            if (e.getCode() == KeyCode.DELETE) {
                deleteAll();
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
        EdgeLine edge;

        if (!isDirected)
            edge = edgeLineFactory.get();
        else
            edge = directedEdgeLineFactory.get();

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

    protected void selectVertex(VertexCircle vertex) {
        int index = root.getChildren().indexOf(vertex);
        root.getChildren().add(index, vertex.getShadow());
        selectedVertices.add(vertex);
    }

    protected void deselectAll() {
        selectedVertices.stream().forEach(v -> {
            v.hideShadow();
            v.deselect();
        });
        selectedVertices.clear();
    }

    protected void deselect(VertexCircle vertex) {
        vertex.hideShadow();
        selectedVertices.remove(vertex);
    }

    private void deleteAll() {
        isUnsaved = true;

        selectedVertices.stream().forEach(v -> {
            v.getOutcomingEdges().stream().forEach(EdgeLine::disappearFromScene);
            v.disappearFromScene();
            graph.removeVertex(v.id());
        });

        root.getChildren().stream().forEach(n -> {
            if (n.getClass().equals(VertexCircle.class)) {
                ((VertexCircle) n).refresh();
            }
        });

        selectedVertices.clear();
    }

    public void drawNewGraph(Graph newGraph) {
        isUnsaved = true;

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
        //TODO: after adding directed graphs title should look eg. MyGraph [directed/undirected] - GRAPH Moment
        ((Stage) root.getScene().getWindow()).setTitle(file.getName() + " [directed] - GRAPH Moment");
        this.file = file;
    }

    public Pair<Double, Double> getRandomCoords() {
        Random random = new Random();
        return new Pair<>(
                random.nextDouble() * (root.getWidth()- 40.0) + 20.0, random.nextDouble() * (root.getHeight()- 40.0) + 20.0
        );
    }
}
