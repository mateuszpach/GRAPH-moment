package com.github.mtdrewski.GRAPH_moment.controller;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;


public class DirectedEdgeLine extends EdgeLine{

    private Polygon triangle;
    private DoubleBinding dx;
    private DoubleBinding dy;

    public DirectedEdgeLine(GraphDrawerController drawer) {
        super(drawer);
        dx = endXProperty().add(startXProperty().negate());
        dy = endYProperty().add(startYProperty().negate());
        triangle = new Polygon(getEndX()+5, getEndY(), getEndX() - 24, getEndY() + 12, getEndX() - 24, getEndY() - 12);
        var rotate = new Rotate(0,0,0,1,Rotate.Z_AXIS);

        triangle.getTransforms().add(rotate);
        dx.addListener((observable, oldValue, newValue) -> {
            rotate.setAngle(getAngle(dy.doubleValue(), newValue.doubleValue()));
        });
        dy.addListener((observable, oldValue, newValue) -> {
            rotate.setAngle(getAngle(newValue.doubleValue(), dx.doubleValue()));
        });

        triangle.layoutXProperty().bindBidirectional(endXProperty());
        triangle.layoutYProperty().bindBidirectional(endYProperty());
    }

    @Override
    public void followVertex(VertexCircle vertex) {
        super.followVertex(vertex);
        double height = getEndY() - getStartY();
        double width = getEndX() - getStartX();
        double length = Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));
        double subtractX = 20 * width / length;
        double subtractY = 20 * height / length;
        setEndX(endVertex.getCenterX()-subtractX);
        setEndY(endVertex.getCenterY()-subtractY);
    }

    private double getAngle(double dy , double dx){
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    public Polygon getTriangle() { return triangle; }

    @Override
    public void appearOnScene() {
        graphDrawerController.getRoot().getChildren().add(0, triangle);
        graphDrawerController.getRoot().getChildren().add(0, this);
    }

    @Override
    public void disappearFromScene() {
        graphDrawerController.getRoot().getChildren().remove(triangle);
        graphDrawerController.getRoot().getChildren().remove(this);
    }
}