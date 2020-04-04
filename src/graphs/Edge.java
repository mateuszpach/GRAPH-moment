package graphs;

import java.util.Objects;

public class Edge {

    private Vertex vertex1;
    private Vertex vertex2;

    protected Edge(Vertex u, Vertex v) {

        // Only the Graph class can use this constructor. The graph class assures provided vertices are not null.
        vertex1 = u;
        vertex2 = v;
    }

    public Vertex vert1() {
        return vertex1;
    }

    public Vertex vert2() {
        return vertex2;
    }

    @Override
    public String toString() {
        return "{" + vertex1.toString() + ", " + vertex2.toString() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(vertex1, edge.vertex1) &&
                Objects.equals(vertex2, edge.vertex2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex1, vertex2);
    }
}
