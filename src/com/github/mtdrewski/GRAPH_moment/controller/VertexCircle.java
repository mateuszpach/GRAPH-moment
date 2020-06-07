package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;

public class VertexCircle extends Circle {

    private double vertexRadius = 20.0;
    private double thickness = 4.0;
    private Color strokeColor = Color.BLACK;
    private Color fillColor = Color.WHITE;

    private int clickCount = 0;
    private Vertex underlyingVertex;
    private Text idText;
    private Circle shadow;
    private boolean selected = false;


    private ArrayList<EdgeLine> outcomingEdges = new ArrayList<>();

    private final GraphDrawerController graphDrawerController;


    public VertexCircle(GraphDrawerController drawer) {
        graphDrawerController = drawer;
        underlyingVertex = graphDrawerController.getGraph().addVertex();
        idText = new Text(Integer.toString(underlyingVertex.id()));
        idText.setBoundsType(TextBoundsType.VISUAL);
        idText.setMouseTransparent(true);
        createBehaviour();
        makeShadow();
    }

    public int id() { return underlyingVertex.id(); }

    public void addOutcomingEdge(EdgeLine edge) {
        graphDrawerController.setUnsaved(true);
        outcomingEdges.add(edge);
    }

    public void removeOutcomingEdge(EdgeLine edge) {
        graphDrawerController.setUnsaved(true);
        outcomingEdges.remove(edge);
    }

    public ArrayList<EdgeLine> getOutcomingEdges() { return outcomingEdges; }

    public void setPosition(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();

        x = Math.min(x, graphDrawerController.getMaxX());
        x = Math.max(x, vertexRadius + 5);
        y = Math.min(y, graphDrawerController.getMaxY());
        y = Math.max(y, vertexRadius + 5);
        setPosition(x, y);
    }

    public void setPosition(double x, double y) {
        graphDrawerController.setUnsaved(true);
        setCenterX(x);
        setCenterY(y);
        setLabelPosition(x, y);
        setShadowPosition(x, y);
        underlyingVertex.setPos(x, y);
        for (EdgeLine edge : outcomingEdges) {
            edge.followVertex(this);
        }
    }

    public void setLabelPosition(double x, double y) {
        x -= idText.getBoundsInLocal().getWidth() / 2;
        y += idText.getBoundsInLocal().getHeight() / 2;
        idText.setX(x);
        idText.setY(y);
    }

    public void prepareLooks() {
        setStrokeWidth(thickness);
        setStroke(strokeColor);
        setFill(fillColor);
        setRadius(vertexRadius);
    }

    public void createBehaviour() {

        setOnMouseEntered(e -> {
            graphDrawerController.cursorOverVertex = true;
        });
        setOnMouseExited(e -> {
            graphDrawerController.cursorOverVertex = false;
        });
        setOnMousePressed(e -> {

            if (e.getClickCount() == 1)
                clearClickCount();

            incrementClickCount();

            if (e.getButton().equals(MouseButton.PRIMARY)) {

                if (graphDrawerController.isInStandardMode() && e.getClickCount() > 1 && getClickCount() > 1) {
                    graphDrawerController.enterEdgeMode(this, e);
                } else if (graphDrawerController.isInEdgeMode()) {
                    if (graphDrawerController.getSourceVertex() != this &&
                        !graphDrawerController.getGraph().contains( graphDrawerController.getSourceVertex().id(),
                                                                    underlyingVertex.id())) {

                        graphDrawerController.getCurrentEdge().setEndVertex(this);
                        outcomingEdges.add(graphDrawerController.getCurrentEdge());
                        graphDrawerController.exitEdgeMode(true);
                        clearClickCount();
                    }
                }
                else if (graphDrawerController.isInSelectMode()) {
                    if (!selected) {
                        select();
                    }
                    else {
                        deselect();
                    }
                }

                if (getClickCount() > 1)
                    clearClickCount();
            }
        });
        setOnMouseDragged(e -> {
            if (graphDrawerController.isInStandardMode() && e.isPrimaryButtonDown()) {
                setPosition(e);
            }
        });
    }

    public void makeShadow() {
        this.shadow = new Circle();
        this.shadow.setOpacity(0.5);
        this.shadow.setRadius(vertexRadius * 1.5);
    }

    public void setShadowPosition(double x, double y) {
        shadow.setCenterX(x);
        shadow.setCenterY(y);
    }

    public Circle getShadow() { return shadow; }

    protected void refresh() {
        idText.setText(Integer.toString(id()));
    }

    public int getClickCount() { return clickCount; }
    public void incrementClickCount() { clickCount++; }
    public void clearClickCount() { clickCount = 0; }
    public Text getIdText() { return idText; }

    public void appearOnScene() {
        graphDrawerController.getRoot().getChildren().add(this);
        graphDrawerController.getRoot().getChildren().add(idText);
        graphDrawerController.verticesOnScene.add(this);
    }

    public void disappearFromScene() {
        hideShadow();
        graphDrawerController.getRoot().getChildren().remove(idText);
        graphDrawerController.getRoot().getChildren().remove(this);
        graphDrawerController.verticesOnScene.remove(this);
    }

    public void hideShadow()  {
        graphDrawerController.getRoot().getChildren().remove(shadow);
    }

    public void showShadow() {
        int index = graphDrawerController.getRoot().getChildren().indexOf(this);
        graphDrawerController.getRoot().getChildren().add(index, shadow);
    }

    public void select() {
        selected = true;
        showShadow();
        graphDrawerController.selectedVertices.add(this);
    }

    public void deselect() {
        selected = false;
        hideShadow();
        graphDrawerController.selectedVertices.remove(this);
    }
}