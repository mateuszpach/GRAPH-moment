package com.github.mateuszpach.GRAPH_moment.model.generators;

import com.github.mateuszpach.GRAPH_moment.controller.GraphDrawerController;

public abstract class IntervalConstrainedGenerator implements GraphGenerator {

    public static enum type {STANDARD, MULTICOMPONENT, TREE}

    ;
    protected static GraphDrawerController graphDrawerController;

    protected int minNumOfVertices;
    protected int minNumOfEdges;

    protected int maxNumOfVertices;
    protected int maxNumOfEdges;

    public IntervalConstrainedGenerator(int minV, int maxV, int minE, int maxE) throws IllegalArgumentException {
        if (minV < 1 || minE < 0 || maxV < minV || maxE < minE)
            throw new IllegalArgumentException();
        minNumOfVertices = minV;
        minNumOfEdges = minE;
        maxNumOfVertices = maxV;
        maxNumOfEdges = maxE;
    }

    public static void setGraphDrawerController(GraphDrawerController controller) {
        graphDrawerController = controller;
    }
}