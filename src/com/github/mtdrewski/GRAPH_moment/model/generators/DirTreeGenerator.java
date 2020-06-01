package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.DirectedGraph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;

import java.util.Random;

public class DirTreeGenerator extends TreeGenerator {

    public DirTreeGenerator(int minV, int maxV) {
        super(minV, maxV);
    }

    public void randomizeEdgeDirection(DirectedGraph graph) {
        Random random = new Random();
        for (Edge edge : graph.getEdges()) {
            if (random.nextBoolean())
                edge.swap();
        }
    }

    @Override
    public DirectedGraph generate() {
        DirectedGraph graph = new DirectedGraph();
        prepareVertices(graph);
        prepareEdges(graph);
        randomizeEdgeDirection(graph);
        return graph;
    }
}
