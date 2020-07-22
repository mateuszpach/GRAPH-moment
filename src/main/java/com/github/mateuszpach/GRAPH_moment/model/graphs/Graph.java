package com.github.mateuszpach.GRAPH_moment.model.graphs;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {

    protected Vector<Vertex> vertices;
    protected Vector<Edge> edges;

    public Vector<Vertex> getVertices() {
        return vertices;
    }

    public Vector<Edge> getEdges() {
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

    public void addVertex(double x, double y) {
        int newId = size() + 1;
        Vertex vertex = new Vertex(newId, x, y);
        vertices.add(vertex);
    }

    public void addVertex(Vertex vert) {
        int newId = size() + 1;
        Vertex vertex = new Vertex(newId, vert.xPos(), vert.yPos());
        vertices.add(vertex);
    }

    public void removeVertex(int id) {
        Collection<Edge> toRem = edges.stream().filter(e -> e.vert2().id() == id || e.vert1().id() == id).collect(Collectors.toList());
        edges.removeAll(toRem);
        vertices.remove(id - 1);
        reNumber(id - 1);
    }

    public void removeEdge(int id1, int id2) {
        Edge e = new Edge(vertices.get(id1 - 1), vertices.get(id2 - 1));
        edges.remove(e);
    }

    public void reNumber(int id) {
        for (int i = id; i < vertices.size(); i++) {
            vertices.get(i).setId(i + 1);
        }
    }

    public Edge addEdge(int vertexId1, int vertexId2) throws LoopEdgeException, NonExistingVertexException {

        checkEdge(vertexId1, vertexId2);

        if (contains(vertexId1, vertexId2))
            return null;
        Edge edge = new Edge(vertices.get(vertexId1 - 1), vertices.get(vertexId2 - 1));
        edges.add(edge);
        return edge;
    }

    protected void checkEdge(int vertexId1, int vertexId2) throws LoopEdgeException, NonExistingVertexException {
        if (vertexId1 < 1 || vertexId1 > size() ||
                vertexId2 < 1 || vertexId2 > size()) {

            throw new NonExistingVertexException(vertexId1, vertexId2, size());
        }

        if (vertexId1 == vertexId2) {
            throw new LoopEdgeException(vertexId1);
        }
    }

    public void addEdge(Edge edge) {
        addEdge(edge.vert1().id(), edge.vert2().id());
    }

    public boolean contains(int vertexId) {
        for (Vertex vertex : vertices) {
            if (vertex.id() == vertexId)
                return true;
        }
        return false;
    }

    public boolean contains(int vertexId1, int vertexId2) {
        if (vertexId1 < 1 || vertexId1 > size() ||
                vertexId2 < 1 || vertexId2 > size())
            return false;
        Edge e = new Edge(vertices.get(vertexId1 - 1), vertices.get(vertexId2 - 1));
        for (Edge edge : edges) {
            if (edge.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Edge edge) {
        return contains(edge.vert1().id(), edge.vert2().id());
    }

    public void randomShuffle() {
        List<Integer> permutation = Stream.iterate(1, x -> x + 1).limit(size()).collect(Collectors.toList());
        Collections.shuffle(permutation);
        for (int i = 1; i <= size(); i++) {
            vertices.get(i - 1).setId(permutation.get(i - 1));
        }
        vertices.sort(Vertex::compareTo);
    }

    @Override
    public String toString() {
        return vertices.toString() + ", " + edges.toString();
    }

    public static class LoopEdgeException extends RuntimeException {

        int loopedVertex = 0;

        LoopEdgeException(int vertID) {
            loopedVertex = vertID;
        }

        @Override
        public String toString() {
            return "Tried to add a loop edge to " + loopedVertex + ".";
        }
    }

    public static class NonExistingVertexException extends RuntimeException {

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
