package graphs;

import java.util.Objects;

public class Edge {

    private Vertice vertice1;
    private Vertice vertice2;

    protected Edge(Vertice u, Vertice v) {

        // Only the Graph class can use this constructor. The graph class assures provided vertices are not null.
        vertice1 = u;
        vertice2 = v;
    }

    public Vertice getVertice1() {
        return vertice1;
    }

    public Vertice getVertice2() {
        return vertice2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(vertice1, edge.vertice1) &&
                Objects.equals(vertice2, edge.vertice2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertice1, vertice2);
    }
}
