package com.github.mtdrewski.GRAPH_moment.model.utils;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;

public interface GraphMerger {

    public enum Type {
        UNION, DISJOINT_UNION
    }

    public static Graph union(Graph graph1, Graph graph2) {
        //TODO check if graphs are simple or directed (or checking can be done inside Graph)
        Graph united = new Graph();
        for (int i = 0; i < Math.max(graph1.size(), graph2.size()); i++) {
            united.addVertex();
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
        for (int i = 0; i < graph1.size() + graph2.size(); i++) {
            united.addVertex();
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
