package com.github.mtdrewski.GRAPH_moment.controller;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashSet;

public class VertexCircle extends Circle {

    private double vertexRadius = 20.0;
    private double thickness = 4.0;
    private Color strokeColor = Color.BLACK;
    private Color fillColor = Color.WHITE;

    private int clickCount = 0;

    private HashSet<EdgeLine> outcomingEdges = new HashSet<>();

    private GraphDrawer graphDrawer;


    public VertexCircle(GraphDrawer drawer) {
        graphDrawer = drawer;
    }

    public VertexCircle(GraphDrawer drawer, Color strokeColor, Color fillColor, double thickness) {
        graphDrawer = drawer;
        this.thickness = thickness;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
    }

    public void addOutcomingEdge(EdgeLine edge) {
        outcomingEdges.add(edge);
    }

    public void removeOutcomingEdge(EdgeLine edge) {
        outcomingEdges.remove(edge);
    }

    public void setPosition(MouseEvent e) {
        setCenterX(e.getX());
        setCenterY(e.getY());
        for (EdgeLine edge : outcomingEdges) {
            edge.followVertex(this);
        }
    }

    public void prepareLooks() {

        setStrokeWidth(thickness);
        setStroke(strokeColor);
        setFill(fillColor);
        setRadius(vertexRadius);
    }

    public void createBehaviour() {

        setOnMouseEntered(e -> {
            graphDrawer.setCursorOverVertex(true);
        });
        setOnMouseExited(e -> {
            graphDrawer.setCursorOverVertex(false);
        });
        setOnMousePressed(e -> {

            if (e.getClickCount() == 1)
                clearClickCount();

            incrementClickCount();

            if (e.getButton().equals(MouseButton.PRIMARY)) {

                if (!graphDrawer.isInEdgeMode() && e.getClickCount() > 1 && getClickCount() > 1) {
                    graphDrawer.enterEdgeMode(this);
                }
                else if (graphDrawer.isInEdgeMode()) {
                    if (graphDrawer.getSourceVertex() != this) {
                        graphDrawer.getCurrentEdge().setEndVertex(this);
                        outcomingEdges.add(graphDrawer.getCurrentEdge());
                        graphDrawer.exitEdgeMode(true);
                        clearClickCount();
                    }
                }

                if (getClickCount() > 1)
                    clearClickCount();
            }
        });
        setOnMouseDragged(this::setPosition);

    }

    public int getClickCount() { return clickCount; }
    public void incrementClickCount() { clickCount++; }
    public void clearClickCount() { clickCount = 0; }
}