package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;

public class EdgeLine extends Line {

    protected double thickness = 4.0;
    protected Color color = Color.BLACK;

    public enum Orientation {BEGIN, END}

    protected Edge underlyingEdge;
    protected VertexCircle startVertex;
    protected VertexCircle endVertex;

    protected GraphDrawerController graphDrawerController;

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
    }

    public void setEndVertex(VertexCircle vertex) {
        endVertex = vertex;
        followWithOffset();
        underlyingEdge = graphDrawerController.getGraph().addEdge(startVertex.id(), endVertex.id());
    }

    public void followVertex(VertexCircle vertex) {

        if (vertex == startVertex) {   System.out.println("start");
            setStartX(vertex.getCenterX());
            setStartY(vertex.getCenterY());
        }
        followWithOffset();
    }

    void followWithOffset(){
        double height = getEndY() - getStartY();
        double width = getEndX() - getStartX();
        double length = Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));
        double subtractX = 20 * width / length;
        double subtractY = 20 * height / length;
        setEndX(endVertex.getCenterX()-subtractX);
        setEndY(endVertex.getCenterY()-subtractY);
    }

    public void prepareLooks() {
        setStrokeWidth(thickness);
        setStroke(Color.BLACK);
    }

    public void followCursor(MouseEvent e) {
        setEndX(e.getX());
        setEndY(e.getY());
    }

    public void clear(){
        graphDrawerController.root.getChildren().remove(this);
    }

}