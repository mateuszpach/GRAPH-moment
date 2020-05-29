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
        setTriangle();
    }

    private void setTriangle(){
        dx = endXProperty().add(startXProperty().negate());
        dy = endYProperty().add(startYProperty().negate());
        triangle = new Polygon(getEndX(), getEndY(), getEndX() - 16, getEndY() + 8, getEndX() - 16, getEndY() - 8);
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
        graphDrawerController.root.getChildren().add(triangle);
    }

    private double getAngle(double dy ,double dx){
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    @Override
    public void clear(){
        graphDrawerController.root.getChildren().remove(triangle);
        super.clear();
    }
}
