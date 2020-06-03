package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EdgeLine extends Line {

    protected double thickness = 5.0;
    protected Color color = Color.BLACK;

    public enum Orientation {BEGIN, END};

    protected Edge underlyingEdge;
    protected VertexCircle startVertex;
    protected VertexCircle endVertex;
    boolean selected = false;

    private Line shadow;

    protected final GraphDrawerController graphDrawerController;

    public EdgeLine(GraphDrawerController drawer) {
        graphDrawerController = drawer;
        createBehaviour();
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
        followVertex(vertex);
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
        moveShadow();
    }

    public void prepareLooks() {
        setStrokeWidth(thickness);
        setStroke(Color.BLACK);
        makeShadow();
    }

    public void followCursor(MouseEvent e) {
        setEndX(e.getX());
        setEndY(e.getY());
        moveShadow();
    }

    public void makeShadow() {
        shadow = new Line();
        shadow.setStrokeWidth(thickness * 3);
        shadow.setStroke(Color.GREY);
        moveShadow();
    }

    public void moveShadow() {
        shadow.setStartX(getStartX());
        shadow.setStartY(getStartY());
        shadow.setEndX(getEndX());
        shadow.setEndY(getEndY());
    }

    public void hideShadow()  {
        graphDrawerController.getRoot().getChildren().remove(shadow);
    }

    public void showShadow() {
        int index = graphDrawerController.getRoot().getChildren().indexOf(this);
        graphDrawerController.getRoot().getChildren().add(index, shadow);
    }

    public void appearOnScene() {
        graphDrawerController.getRoot().getChildren().add(0, this);
    }

    public void disappearFromScene() {
        hideShadow();
        graphDrawerController.getRoot().getChildren().remove(this);
    }

    public void createBehaviour() {
        setOnMouseEntered(e -> {
            graphDrawerController.cursorOverEdge = true;
        });
        setOnMouseExited(e -> {
            graphDrawerController.cursorOverEdge = false;
        });
        setOnMousePressed(e -> {
            if (graphDrawerController.isInSelectMode()) {
                if (!selected)
                    select();
                else
                    deselect();
            }
        });
    }

    public void select() {
        selected = true;
        showShadow();
        graphDrawerController.selectedEdges.add(this);
    }

    public void deselect() {
        selected = false;
        hideShadow();
        graphDrawerController.selectedEdges.remove(this);
    }
}