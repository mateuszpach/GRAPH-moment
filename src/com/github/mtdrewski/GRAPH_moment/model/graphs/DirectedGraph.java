package com.github.mtdrewski.GRAPH_moment.model.graphs;

public class DirectedGraph extends Graph {

    @Override
    public boolean contains(int vertexId1, int vertexId2) {
        for (Edge edge : edges) {
            if (edge.vert1().id() == vertexId1 && edge.vert2().id() == vertexId2)
                return true;
        }
        return false;
    }


}
