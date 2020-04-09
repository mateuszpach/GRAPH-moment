package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.HashSet;
import java.util.function.Supplier;

public class GraphDrawer {

    @FXML
    AnchorPane anchorPane;

    private boolean cursorOverVertex = false;
    private boolean inEdgeMode = false;

    private EdgeLine currentEdge;
    private VertexCircle sourceVertex;

    private static Graph graph;

    private Supplier<VertexCircle> vertexShapeFactory = () -> {

        VertexCircle vertexShape = new VertexCircle();
        vertexShape.setStrokeDashOffset(VertexCircle.vertexStrokeDashOffset);
        vertexShape.setStroke(Color.BLACK);
        vertexShape.setFill(Color.WHITE); //TODO maybe add variety of colors
        vertexShape.setRadius(VertexCircle.vertexRadius);

        vertexShape.setOnMouseEntered(e -> {
            cursorOverVertex = true;
        });
        vertexShape.setOnMouseExited(e -> {
            cursorOverVertex = false;
        });
        vertexShape.setOnMouseClicked(e -> {
            if (!inEdgeMode) {
                enterEdgeMode(vertexShape);
            }
            else {
                if (sourceVertex != vertexShape) {
                    currentEdge.setEndX(vertexShape.getCenterX());
                    currentEdge.setEndY(vertexShape.getCenterY());
                    exitEdgeMode(true);
                }
            }
        });

        return vertexShape;
    };

    public void initialize() {

        anchorPane.setOnMouseClicked(e -> {

            if (!inEdgeMode) {
                if (cursorOverVertex)
                    return;

                double x = e.getX();
                double y = e.getY();
                VertexCircle vertexShape = vertexShapeFactory.get();
                vertexShape.setCenterX(x);
                vertexShape.setCenterY(y);

                anchorPane.getChildren().add(vertexShape);
            }
            else {
                if (!cursorOverVertex) {
                    exitEdgeMode(false);
                }
            }
        });

        anchorPane.setOnMouseMoved(e -> {
            if (!inEdgeMode)
                return;
            currentEdge.setEndX(e.getX());
            currentEdge.setEndY(e.getY());
        });
    }

    private void enterEdgeMode(VertexCircle vertexShape) {

        EdgeLine edgeShape = new EdgeLine();
        edgeShape.setStartX(vertexShape.getCenterX());
        edgeShape.setStartY(vertexShape.getCenterY());
        edgeShape.setEndX(edgeShape.getStartX());
        edgeShape.setEndY(edgeShape.getStartY());
        currentEdge = edgeShape;
        anchorPane.getChildren().add(0, edgeShape);
        inEdgeMode = true;
    }

    private void exitEdgeMode(boolean success) {
        if (!success)
            anchorPane.getChildren().remove(currentEdge);
        currentEdge = null;
        sourceVertex = null;
        inEdgeMode = false;
    }

    private static class VertexCircle extends Circle {

        private static double vertexRadius = 20;
        private static double vertexStrokeDashOffset = 5;

        private HashSet<EdgeLine> outcomingEdges;
    }

    private static class EdgeLine extends Line {

        private VertexCircle startVertex;
        private VertexCircle endVertex;

        public VertexCircle getStartVertex() { return startVertex; }
        public VertexCircle getEndVertex() { return endVertex; }
    }
}
