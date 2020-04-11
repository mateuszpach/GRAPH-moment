package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.HashSet;
import java.util.function.Supplier;

public class GraphDrawer {

    @FXML
    AnchorPane anchorPane;

    private static boolean cursorOverVertex = false;
    private static boolean inEdgeMode = false;

    private static EdgeLine currentEdge;
    private static VertexCircle sourceVertex;

    private static Graph graph;

    private Supplier<VertexCircle> vertexShapeFactory = () -> {

        VertexCircle vertexShape = new VertexCircle();
        vertexShape.setStrokeWidth(VertexCircle.vertexStrokeDashOffset);
        vertexShape.setStroke(Color.BLACK);
        vertexShape.setFill(Color.WHITE); //TODO maybe add variety of colors
        vertexShape.setRadius(VertexCircle.vertexRadius);

        vertexShape.setOnMouseEntered(e -> {
            cursorOverVertex = true;
        });
        vertexShape.setOnMouseExited(e -> {
            cursorOverVertex = false;
        });
        vertexShape.setOnMousePressed(e -> {

            if (e.getClickCount() == 1)
                vertexShape.clearClickCount();

            vertexShape.incrementClickCount();

            if (e.getButton().equals(MouseButton.PRIMARY)) {

                if (!inEdgeMode && e.getClickCount() > 1 && vertexShape.getClickCount() > 1) {
                    enterEdgeMode(vertexShape);
                } else if (inEdgeMode) {
                    if (sourceVertex != vertexShape) {
                        currentEdge.setEndVertex(vertexShape);
                        vertexShape.outcomingEdges.add(currentEdge);
                        exitEdgeMode(true);
                        vertexShape.clearClickCount();
                    }
                }

                if (vertexShape.getClickCount() > 1)
                    vertexShape.clearClickCount();
            }
        });
        vertexShape.setOnMouseDragged(e -> {
            vertexShape.setPosition(e);
        });

        return vertexShape;
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

        anchorPane.setOnMouseMoved(this::edgeFollowCursor);
        anchorPane.setOnMouseDragged(this::edgeFollowCursor);
    }

    private void edgeFollowCursor(MouseEvent e) {
        if (!inEdgeMode)
            return;
        currentEdge.setEndX(e.getX());
        currentEdge.setEndY(e.getY());
    }

    private void enterEdgeMode(VertexCircle vertexShape) {

        EdgeLine edgeShape = new EdgeLine();
        edgeShape.setStrokeWidth(EdgeLine.thickness);
        edgeShape.setStartVertex(vertexShape);
        vertexShape.outcomingEdges.add(edgeShape);

        sourceVertex = vertexShape;

        edgeShape.setEndX(edgeShape.getStartX());
        edgeShape.setEndY(edgeShape.getStartY());

        currentEdge = edgeShape;
        anchorPane.getChildren().add(0, edgeShape);
        inEdgeMode = true;
    }

    private void exitEdgeMode(boolean success) {
        if (!success) {
            anchorPane.getChildren().remove(currentEdge);
            sourceVertex.outcomingEdges.remove(currentEdge);
        }
        currentEdge = null;
        sourceVertex = null;
        inEdgeMode = false;
    }

    private static class VertexCircle extends Circle {

        private static double vertexRadius = 20.0;
        private static double vertexStrokeDashOffset = 4.0;

        private int clickCount = 0;

        public int getClickCount() { return clickCount; }
        public void incrementClickCount() { clickCount++; }
        public void clearClickCount() { clickCount = 0; }

        private HashSet<EdgeLine> outcomingEdges = new HashSet<>();

        private void setPosition(MouseEvent e) {
            setCenterX(e.getX());
            setCenterY(e.getY());
            for (EdgeLine edge : outcomingEdges) {
                edge.followVertex(this);
            }
        }
    }

    private static class EdgeLine extends Line {

        private static double thickness = 4.0;

        public enum Orientation {BEGIN, END};

        private VertexCircle startVertex;
        private VertexCircle endVertex;

        public VertexCircle getStartVertex() { return startVertex; }
        public VertexCircle getEndVertex() { return endVertex; }
        public void setStartVertex(VertexCircle vertex) {
            startVertex = vertex;
            setStartX(vertex.getCenterX());
            setStartY(vertex.getCenterY());
        }
        public void setEndVertex(VertexCircle vertex) {
            endVertex = vertex;
            setEndX(vertex.getCenterX());
            setEndY(vertex.getCenterY());
        }

        public void followVertex(VertexCircle vertex) {
            if (vertex == startVertex) {
                setStartX(vertex.getCenterX());
                setStartY(vertex.getCenterY());
            }
            else if (vertex == endVertex) {
                setEndX(vertex.getCenterX());
                setEndY(vertex.getCenterY());
            }
        }
    }
}
