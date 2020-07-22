package com.github.mateuszpach.GRAPH_moment.controller;

import com.github.mateuszpach.GRAPH_moment.model.graphs.Edge;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EdgeLine extends Line {

    protected double thickness = 5.0;
    protected Color color = Color.BLACK;

    protected Edge underlyingEdge;
    protected VertexCircle startVertex;
    protected VertexCircle endVertex;
    boolean selected = false;

    protected Line shadow;
    protected TextField label;

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
        } else if (vertex == endVertex) {
            setEndX(vertex.getCenterX());
            setEndY(vertex.getCenterY());
        }
        moveShadow();
        moveLabel();
    }

    public void prepareLooks() {
        setStrokeWidth(thickness);
        setStroke(Color.BLACK);
        makeShadow();
        makeLabel();
    }

    public void followCursor(MouseEvent e) {
        setEndX(e.getX());
        setEndY(e.getY());
        moveShadow();
        moveLabel();
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

    public void makeLabel() {
        label = new TextField("");
        label.setMouseTransparent(true);

        label.setAlignment(Pos.CENTER);
        label.setBackground(Background.EMPTY);

        label.setStyle("-fx-text-fill: white");

        label.setPrefWidth(60);
        label.setPrefHeight(10);
        label.setScaleX(2);
        label.setScaleY(2);

        label.setOnKeyTyped(t -> {

            if (label.getText().length() > 5) {
                int pos = label.getCaretPosition();
                label.setText(label.getText(0, 5));
                label.positionCaret(pos);
            }

        });
    }

    public void editLabel(boolean value) {
        label.setMouseTransparent(!value);
        if (!value) {
            label.getParent().requestFocus();
        }
    }

    public void moveLabel() {
        double x = getStartX() + (getEndX() - getStartX()) / 2;
        double y = getStartY() + (getEndY() - getStartY()) / 2;
        x -= label.getBoundsInLocal().getWidth() / 2;
        y -= label.getBoundsInLocal().getHeight() / 2;
        label.setTranslateX(x);
        label.setTranslateY(y);
    }

    public void hideShadow() {
        graphDrawerController.getRoot().getChildren().remove(shadow);
    }

    public void hideLabel() {
        graphDrawerController.getRoot().getChildren().remove(label);
    }

    public void showShadow() {
        int index = graphDrawerController.getRoot().getChildren().indexOf(this);
        graphDrawerController.getRoot().getChildren().add(index, shadow);
    }

    public void showLabel() {
        int index = graphDrawerController.getRoot().getChildren().indexOf(this);
        graphDrawerController.getRoot().getChildren().add(index + 1, label);
    }

    public void appearOnScene() {
        graphDrawerController.getRoot().getChildren().add(0, this);
        graphDrawerController.edgesOnScene.add(this);
        showLabel();
    }

    public void disappearFromScene() {
        hideShadow();
        hideLabel();
        graphDrawerController.getRoot().getChildren().remove(this);
        graphDrawerController.edgesOnScene.remove(this);
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