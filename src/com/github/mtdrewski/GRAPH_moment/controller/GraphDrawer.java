package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.dataProcessor.DataProcessor;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class GraphDrawer {

    @FXML
    AnchorPane anchorPane;

    private boolean cursorOverVertex = false;
    private boolean inEdgeMode = false;

    private EdgeLine currentEdge;
    private VertexCircle sourceVertex;

    public Graph graph=null;

    public void setCursorOverVertex(boolean value) { cursorOverVertex = value; }
    public boolean isInEdgeMode() { return inEdgeMode; }
    protected EdgeLine getCurrentEdge() { return currentEdge; }
    protected VertexCircle getSourceVertex() { return sourceVertex; }


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

    public void initialize() {

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

    public void passGraphAndDraw(Graph graph1){
        graph=graph1;
        reDrawGraph();
    }

    private void reDrawGraph(){

        anchorPane.getChildren().clear();
        ArrayList<VertexCircle> vertexCircles=new ArrayList<>();

        for(Vertex v1: graph.getVertices()) {
            VertexCircle vertexShape= vertexShapeFactory.get();
            vertexShape.setCenterX(v1.xPos());
            vertexShape.setCenterY(v1.yPos());
            anchorPane.getChildren().add(vertexShape);
            vertexCircles.add(vertexShape);
        }

        for(Edge e1:graph.getEdges()){
            EdgeLine edgeLine = edgeLineFactory.get();
            edgeLine.setStartVertex(vertexCircles.get(e1.vert1().id()-1));
            edgeLine.setEndVertex(vertexCircles.get(e1.vert2().id()-1));
        }
    }
}
