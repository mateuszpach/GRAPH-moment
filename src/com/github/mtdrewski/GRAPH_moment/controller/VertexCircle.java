package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.HashSet;

public class VertexCircle extends Circle {

    private double vertexRadius = 20.0;
    private double thickness = 4.0;
    private Color strokeColor = Color.BLACK;
    private Color fillColor = Color.WHITE;

    private int clickCount = 0;
    private Vertex underlyingVertex;
    private Text idText;

    private HashSet<EdgeLine> outcomingEdges = new HashSet<>();

    private GraphDrawerController graphDrawerController;


    public VertexCircle(GraphDrawerController drawer) {
        graphDrawerController = drawer;
        underlyingVertex = graphDrawerController.getGraph().addVertex();
        idText = new Text(Integer.toString(underlyingVertex.id()));
        idText.setBoundsType(TextBoundsType.VISUAL);
        idText.setMouseTransparent(true);
    }

    public VertexCircle(GraphDrawerController drawer, Color strokeColor, Color fillColor, double thickness) {
        underlyingVertex = graphDrawerController.getGraph().addVertex();
        graphDrawerController = drawer;
        this.thickness = thickness;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        idText = new Text(Integer.toString(underlyingVertex.id()));
        idText.setBoundsType(TextBoundsType.VISUAL);
        idText.setMouseTransparent(true);
    }

    public int id() { return underlyingVertex.id(); }

    public void addOutcomingEdge(EdgeLine edge) {
        outcomingEdges.add(edge);
    }

    public void removeOutcomingEdge(EdgeLine edge) {
        outcomingEdges.remove(edge);
    }

    public void setPosition(MouseEvent e) {
        underlyingVertex.setPos(e.getX(), e.getY());
        setCenterX(e.getX());
        setCenterY(e.getY());
        setLabelPosition(e.getX(), e.getY());
        for (EdgeLine edge : outcomingEdges) {
            edge.followVertex(this);
        }
    }

    public void setPosition(double x, double y) {
        setCenterX(x);
        setCenterY(y);
        setLabelPosition(x, y);
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
            graphDrawerController.setCursorOverVertex(true);
        });
        setOnMouseExited(e -> {
            graphDrawerController.setCursorOverVertex(false);
        });
        setOnMousePressed(e -> {

            if (e.getClickCount() == 1)
                clearClickCount();

            incrementClickCount();

            if (e.getButton().equals(MouseButton.PRIMARY)) {

                if (!graphDrawerController.isInEdgeMode() && e.getClickCount() > 1 && getClickCount() > 1) {
                    graphDrawerController.enterEdgeMode(this);
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

                if (getClickCount() > 1)
                    clearClickCount();
            }
        });
        setOnMouseDragged(this::setPosition);

    }

    public int getClickCount() { return clickCount; }
    public void incrementClickCount() { clickCount++; }
    public void clearClickCount() { clickCount = 0; }
    public Text getIdText() { return idText; }
}