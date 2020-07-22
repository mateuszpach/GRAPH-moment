package com.github.mateuszpach.GRAPH_moment.model.graphs;

import java.util.Objects;

public class DirectedEdge extends Edge {

    protected DirectedEdge(Vertex u, Vertex v) {
        super(u, v);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectedEdge edge = (DirectedEdge) o;
        return Objects.equals(vertex1, edge.vertex1) &&
                Objects.equals(vertex2, edge.vertex2);
    }
}
