package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EdgeLine extends Line {

    private double thickness = 4.0;
    private Color color = Color.BLACK;

    public enum Orientation {BEGIN, END};

    private Edge underlyingEdge;
    private VertexCircle startVertex;
    private VertexCircle endVertex;

    private GraphDrawerController graphDrawerController;

    public EdgeLine(GraphDrawerController drawer) {
        graphDrawerController = drawer;
    }

    public EdgeLine(GraphDrawerController drawer, double thickness, Color color) {
        graphDrawerController = drawer;
        this.thickness = thickness;
        this.color = color;
    }

    public VertexCircle getStartVertex() {
        return startVertex;
    }

    public VertexCircle getEndVertex() {
        return endVertex;
    }

    public void setStartVertex(VertexCircle vertex) {

        startVertex = vertex;
        setStartX(vertex.getCenterX());
        setStartY(vertex.getCenterY());
        setEndX(vertex.getCenterX());
        setEndY(vertex.getCenterY());
    }

    public void setEndVertex(VertexCircle vertex) {

        endVertex = vertex;
        setEndX(vertex.getCenterX());
        setEndY(vertex.getCenterY());
        underlyingEdge = graphDrawerController.getGraph().addEdge(startVertex.id(), endVertex.id());
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

    public void prepareLooks() {
        setStrokeWidth(thickness);
        setStroke(Color.BLACK);
    }

    public void followCursor(MouseEvent e) {
        setEndX(e.getX());
        setEndY(e.getY());
    }
}