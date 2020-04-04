package graphs;

import java.util.Arrays;
import java.util.Vector;

public class Graph {

    private Vector<Vertice> vertices;
    private Vector<Edge> edges;

    Graph() {
        vertices = new Vector<Vertice>();
        edges = new Vector<Edge>();
    }

    public void addVertice() {
        Vertice vertice = new Vertice();
        vertices.add(vertice);
    }

    public void addVertice(float x, float y) {
        Vertice vertice = new Vertice(x, y);
        vertices.add(vertice);
    }

    public void addEdge(int verticeID_1, int verticeID_2) throws LoopEdgeException, NonExistingVerticeException {

        if (verticeID_1 < 1 || verticeID_1 > Vertice.getNumberOfVerticesOnScene() ||
            verticeID_2 < 1 || verticeID_2 > Vertice.getNumberOfVerticesOnScene()   ) {

            throw new NonExistingVerticeException(verticeID_1, verticeID_2);
        }

        if (verticeID_1 == verticeID_2) {
            throw new LoopEdgeException(verticeID_1);
        }
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

        NonExistingVerticeException(int u, int v) {
            vertId1 = u;
            vertId2 = v;
        }

        @Override
        public String toString() {
            return "Tried to create an edge between non existing pair of vertices. Wrong Ids: " +
                    vertId1 + " " + vertId2 + ". Max available Id is: " + Vertice.getNumberOfVerticesOnScene();
        }
    }
}
