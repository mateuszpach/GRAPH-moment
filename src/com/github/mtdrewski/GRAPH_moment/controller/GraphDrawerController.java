package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.function.Supplier;

public class GraphDrawerController {

    @FXML
    AnchorPane anchorPane;

    private boolean cursorOverVertex = false;
    private boolean inEdgeMode = false;

    private EdgeLine currentEdge;
    private VertexCircle sourceVertex = null;

    private Graph graph;

    public void setCursorOverVertex(boolean value) {
        cursorOverVertex = value;
    }

    public boolean isInEdgeMode() {
        return inEdgeMode;
    }

    protected EdgeLine getCurrentEdge() {
        return currentEdge;
    }

    protected VertexCircle getSourceVertex() {
        return sourceVertex;
    }

    private Supplier<VertexCircle> vertexShapeFactory = () -> {
        VertexCircle vertex = new VertexCircle(this, graph);
        vertex.prepareLooks();
        vertex.createBehaviour();
        return vertex;
    };

    public Supplier<EdgeLine> edgeLineFactory = () -> {
        EdgeLine edge = new EdgeLine(this, graph);
        edge.prepareLooks();
        edge.setStartVertex(sourceVertex);
        return edge;
    };

    public void initialize() {
        ImportController.graphDrawerController = this;
        graph = new Graph();

        anchorPane.setOnMousePressed(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (!inEdgeMode) {
                    if (cursorOverVertex)
                        return;

                    VertexCircle vertexShape = vertexShapeFactory.get();
                    vertexShape.setPosition(e);

                    anchorPane.getChildren().add(vertexShape);
                } else {
                    if (!cursorOverVertex) {
                        exitEdgeMode(false);
                    }
                }
            }
        });

        anchorPane.setOnMouseMoved(e -> {
            if (currentEdge != null && inEdgeMode)
                currentEdge.followCursor(e);
        });
        anchorPane.setOnMouseDragged(e -> {
            if (currentEdge != null && inEdgeMode)
                currentEdge.followCursor(e);
        });
    }

    protected void enterEdgeMode(VertexCircle vertex) {

        sourceVertex = vertex;
        EdgeLine edge = edgeLineFactory.get();
        sourceVertex.addOutcomingEdge(edge);

        currentEdge = edge;
        anchorPane.getChildren().add(0, edge);
        inEdgeMode = true;
    }

    protected void exitEdgeMode(boolean success) {

        if (!success) {
            anchorPane.getChildren().remove(currentEdge);
            sourceVertex.removeOutcomingEdge(currentEdge);
        }

        currentEdge = null;
        sourceVertex = null;
        inEdgeMode = false;
    }

    public void reDrawGraph(Graph newGraph) {

        anchorPane.getChildren().clear();
        graph = new Graph();
        ArrayList<VertexCircle> vertexCircles = new ArrayList<>();

        for (Vertex graphVertex : newGraph.getVertices()) {
            VertexCircle vertexShape = vertexShapeFactory.get();
            vertexShape.setPosition(graphVertex.xPos(), graphVertex.yPos());
            vertexCircles.add(vertexShape);
            anchorPane.getChildren().add(vertexShape);
        }

        for (Edge graphEdge : newGraph.getEdges()) {

            EdgeLine edgeLine = new EdgeLine(this, graph);
            edgeLine.prepareLooks();

            edgeLine.setStartVertex(vertexCircles.get(graphEdge.vert1().id() - 1));
            vertexCircles.get(graphEdge.vert1().id() - 1).addOutcomingEdge(edgeLine);

            anchorPane.getChildren().add(0, edgeLine);

            edgeLine.setEndVertex(vertexCircles.get(graphEdge.vert2().id() - 1));
            vertexCircles.get(graphEdge.vert2().id() - 1).addOutcomingEdge(edgeLine);
        }
        currentEdge = null;
        sourceVertex = null;
    }
}
