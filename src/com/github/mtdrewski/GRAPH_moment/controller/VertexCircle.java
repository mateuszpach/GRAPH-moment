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

    private GraphDrawerController graphDrawerController;


    public VertexCircle(GraphDrawerController drawer) {
        graphDrawerController = drawer;
        underlyingVertex = graphDrawerController.getGraph().addVertex();
        idText = new Text(Integer.toString(underlyingVertex.id()));
        idText.setBoundsType(TextBoundsType.VISUAL);
        idText.setMouseTransparent(true);
        makeShadow();
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
        makeShadow();
    }

    public int id() { return underlyingVertex.id(); }

    public void addOutcomingEdge(EdgeLine edge) {
        graphDrawerController.isUnsaved = true;
        outcomingEdges.add(edge);
    }

    public void removeOutcomingEdge(EdgeLine edge) {
        graphDrawerController.isUnsaved = true;
        outcomingEdges.remove(edge);
    }

    public ArrayList<EdgeLine> getOutcomingEdges() { return outcomingEdges; }

    public void setPosition(MouseEvent e) {
        graphDrawerController.isUnsaved = true;
        underlyingVertex.setPos(e.getX(), e.getY());
        setCenterX(e.getX());
        setCenterY(e.getY());
        setLabelPosition(e.getX(), e.getY());
        setShadowPosition(e.getX(), e.getY());
        for (EdgeLine edge : outcomingEdges) {
            edge.followVertex(this);
        }
    }

    public void setPosition(double x, double y) {
        graphDrawerController.isUnsaved = true;
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

                if (graphDrawerController.isInStandardMode() && e.getClickCount() > 1 && getClickCount() > 1) {
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
                else if (graphDrawerController.isInSelectMode()) {
                    if (!selected) {
                        graphDrawerController.selectVertex(this);
                        selected = true;
                    }
                    else {
                        graphDrawerController.deselect(this);
                        selected = false;
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

    protected void deselect() {
        selected = false;
    }

    protected void refresh() {
        idText.setText(Integer.toString(id()));
    }

    public int getClickCount() { return clickCount; }
    public void incrementClickCount() { clickCount++; }
    public void clearClickCount() { clickCount = 0; }
    public Text getIdText() { return idText; }
}