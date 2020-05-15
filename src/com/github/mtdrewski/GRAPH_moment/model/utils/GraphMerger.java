package com.github.mtdrewski.GRAPH_moment.model.utils;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;

public interface GraphMerger {

    public enum Type {
        UNION, DISJOINT_UNION
    }

    ;

    public static Graph union(Graph graph1, Graph graph2) {
        //TODO check if graphs are simple or directed (or checking can be done inside Graph)
        Graph united = new Graph();

        for (Vertex vertex : graph1.getVertices()) {
            united.addVertex(vertex);
        }
        for (int i = graph1.size(); i < graph2.size(); i++) {
            united.addVertex(graph2.getVertices().get(i));
        }

        for (Edge edge : graph1.getEdges()) {
            if (!united.contains(edge))
                united.addEdge(edge);
        }
        for (Edge edge : graph2.getEdges()) {
            if (!united.contains(edge))
                united.addEdge(edge);
        }
        return united;
    }

    public static Graph disjointUnion(Graph graph1, Graph graph2) {
        //TODO check if graphs are simple or directed (or checking can be done inside Graph)
        Graph united = new Graph();
        for (Vertex vertex : graph1.getVertices()) {
            united.addVertex(vertex);
        }
        for (Vertex vertex : graph2.getVertices()) {
            united.addVertex(vertex);
        }
        for (Edge edge : graph1.getEdges()) {
            united.addEdge(edge.vert1().id(), edge.vert2().id());
        }
        for (Edge edge : graph2.getEdges()) {
            united.addEdge(edge.vert1().id() + graph1.size(), edge.vert2().id() + graph1.size());
        }
        return united;
    }
}
