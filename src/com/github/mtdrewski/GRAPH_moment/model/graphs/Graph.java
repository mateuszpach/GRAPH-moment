package com.github.mtdrewski.GRAPH_moment.model.graphs;

import java.util.Vector;

public class Graph {

    private Vector<Vertex> vertices;
    private Vector<Edge> edges;

    public Vector<Vertex> getVertices(){
        return vertices;
    }
    public Vector<Edge> getEdges(){
        return edges;
    }

    public Graph() {
        vertices = new Vector<Vertex>();
        edges = new Vector<Edge>();
    }

    public int size() {
        return vertices.size();
    }

    public Vertex addVertex() {
        int newId = size() + 1;
        Vertex vertex = new Vertex(newId);
        vertices.add(vertex);
        return vertex;
    }

    public Vertex addVertex(double x, double y) {
        int newId = size() + 1;
        Vertex vertex = new Vertex(newId, x, y);
        vertices.add(vertex);
        return vertex;
    }

    public Edge addEdge(int vertexId1, int vertexId2) throws LoopEdgeException, NonExistingVertexException {

        if (vertexId1 < 1 || vertexId1 > size() ||
                vertexId2 < 1 || vertexId2 > size()) {

            throw new NonExistingVertexException(vertexId1, vertexId2, size());
        }

        if (vertexId1 == vertexId2) {
            throw new LoopEdgeException(vertexId1);
        }

        //TODO maybe change not intuitive indexing
        Edge edge = new Edge(vertices.get(vertexId1 - 1), vertices.get(vertexId2 - 1));
        edges.add(edge);
        return edge;
    }

    public boolean contains(int vertexId) {
        for (Vertex vertex : vertices) {
            if (vertex.id() == vertexId)
                return true;
        }
        return false;
    }

    public boolean contains(int vertexId1, int vertexId2) {
        for (Edge edge : edges) {
            if ((edge.vert1().id() == vertexId1 && edge.vert2().id() == vertexId2) ||
                    (edge.vert1().id() == vertexId2 && edge.vert2().id() == vertexId1)) {

                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return vertices.toString() + ", " + edges.toString();
    }

    public static class LoopEdgeException extends RuntimeException { //TODO maybe create other file for this?

        int loopedVertex = 0;

        LoopEdgeException(int vertID) {
            loopedVertex = vertID;
        }

        @Override
        public String toString() {
            return "Tried to add a loop edge to " + loopedVertex + ".";
        }
    }

    public static class NonExistingVertexException extends RuntimeException { //TODO maybe create other file for this?

        int vertexId1;
        int vertexId2;
        int limit;

        NonExistingVertexException(int u, int v, int limit) {
            vertexId1 = u;
            vertexId2 = v;
            this.limit = limit;
        }

        @Override
        public String toString() {
            return "Tried to create an edge between non existing pair of vertices. Wrong Ids: " +
                    vertexId1 + " " + vertexId2 + ". Max available Id is: " + limit;
        }
    }
}
