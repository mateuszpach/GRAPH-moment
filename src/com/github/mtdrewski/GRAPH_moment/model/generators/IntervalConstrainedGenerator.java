package com.github.mtdrewski.GRAPH_moment.model.generators;

public abstract class IntervalConstrainedGenerator implements GraphGenerator {

    public static enum type { STANDARD, MULTICOMPONENT, TREE};

    protected int minNumOfVertices;
    protected int minNumOfEdges;

    protected int maxNumOfVertices;
    protected int maxNumOfEdges;

    public IntervalConstrainedGenerator(int minV, int maxV, int minE, int maxE) throws IllegalArgumentException {
        if (minV < 1 || minE < 0 || maxV < minV || maxE < minE || minE > minV * (minV - 1) / 2)
            throw new IllegalArgumentException();
        minNumOfVertices = minV;
        minNumOfEdges = minE;
        maxNumOfVertices = maxV;
        maxNumOfEdges = maxE;
    }
}