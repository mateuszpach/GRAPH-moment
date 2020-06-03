package com.github.mtdrewski.GRAPH_moment.model.graphs;

public class DirectedGraph extends Graph {

    @Override
    public DirectedEdge addEdge(int vertexId1, int vertexId2) throws LoopEdgeException, NonExistingVertexException {
        checkEdge(vertexId1, vertexId2);
        if (contains(vertexId1, vertexId2))
            return null;
        DirectedEdge edge = new DirectedEdge(vertices.get(vertexId1 - 1), vertices.get(vertexId2 - 1));
        edges.add(edge);
        return edge;
    }

    @Override
    public void removeEdge(int id1, int id2) {
        DirectedEdge e = new DirectedEdge(vertices.get(id1-1), vertices.get(id2-1));
        edges.remove(e);
    }
}
