package graphs;

import java.util.Arrays;
import java.util.Vector;

public class Graph {

    private Vector<Vertice> vertices;
    private Vector<Edge> edges;

    public Graph() {
        vertices = new Vector<Vertice>();
        edges = new Vector<Edge>();
    }

    public int size() {
        return vertices.size();
    }

    public void addVertice() {
        int newId = size() + 1;
        vertices.add(new Vertice(newId));
    }

    public void addVertice(float x, float y) {
        int newId = size() + 1;
        vertices.add(new Vertice(newId, x, y));
    }

    public void addEdge(int verticeID_1, int verticeID_2) throws LoopEdgeException, NonExistingVerticeException {

        if (verticeID_1 < 1 || verticeID_1 > size() ||
            verticeID_2 < 1 || verticeID_2 > size()   ) {

            throw new NonExistingVerticeException(verticeID_1, verticeID_2, size());
        }

        if (verticeID_1 == verticeID_2) {
            throw new LoopEdgeException(verticeID_1);
        }

        //TODO maybe change not intuitive indexing
        edges.add(new Edge(vertices.get(verticeID_1 - 1), vertices.get(verticeID_2 - 1)));
    }

    public boolean contains(int vertId) {
        for (Vertice vert : vertices) {
            if (vert.id() == vertId)
                return true;
        }
        return false;
    }

    public boolean contains(int vertId1, int vertId2) {
        for (Edge edge : edges) {
            if ((edge.vert1().id() == vertId1 && edge.vert2().id() == vertId2) ||
                    (edge.vert1().id() == vertId2 && edge.vert2().id() == vertId1)) {

                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return vertices.toString() + ", " +edges.toString();
    }

    public static class LoopEdgeException extends RuntimeException { //TODO maybe create other file for this?

        int loopedVertice = 0;

        LoopEdgeException(int vertID) {
            loopedVertice = vertID;
        }

        @Override
        public String toString() {
            return "Tried to add a loop edge to " + loopedVertice + ".";
        }
    }

    public static class NonExistingVerticeException extends RuntimeException { //TODO maybe create other file for this?

        int vertId1;
        int vertId2;
        int limit;

        NonExistingVerticeException(int u, int v, int limit) {
            vertId1 = u;
            vertId2 = v;
            this.limit = limit;
        }

        @Override
        public String toString() {
            return "Tried to create an edge between non existing pair of vertices. Wrong Ids: " +
                    vertId1 + " " + vertId2 + ". Max available Id is: " + limit;
        }
    }
}
