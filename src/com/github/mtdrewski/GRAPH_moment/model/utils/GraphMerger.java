package com.github.mtdrewski.GRAPH_moment.model.utils;

import com.github.mtdrewski.GRAPH_moment.controller.GraphDrawerController;
import com.github.mtdrewski.GRAPH_moment.model.graphs.DirectedGraph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;

public interface GraphMerger {

    enum Type {
        UNION, DISJOINT_UNION, REPLACE
    }

    static Graph union(Graph graph1, Graph graph2, boolean directed) {
        if (graph2 == null)
            return graph1;

        Graph united;
        if (!directed)
            united = new Graph();
        else
            united = new DirectedGraph();

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

    static Graph disjointUnion(Graph graph1, Graph graph2, boolean directed) {
        if (graph2 == null)
            return graph1;

        Graph united;
        if (!directed)
            united = new Graph();
        else
            united = new DirectedGraph();

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

    public static void drawAccordingToMergeType(GraphDrawerController graphDrawerController, Type mergeType,
                                                Graph oldGraph, Graph newGraph) {
        switch (mergeType) {
            case UNION:
                graphDrawerController.drawNewGraph(GraphMerger.union(oldGraph, newGraph, graphDrawerController.isDirected()));
                break;
            case DISJOINT_UNION:
                graphDrawerController.drawNewGraph(GraphMerger.disjointUnion(oldGraph, newGraph, graphDrawerController.isDirected()));
                break;
            case REPLACE:
                graphDrawerController.drawNewGraph(newGraph);
                break;
            default:
                break;
        }
    }

    public static Type recognizeMergeType(String text) {
        Type mergeType = null;
        switch (text) {
            case "Union graph":
                mergeType = GraphMerger.Type.UNION;
                break;
            case "Renumber graph":
                mergeType = GraphMerger.Type.DISJOINT_UNION;
                break;
            case "Replace with graph":
                mergeType = GraphMerger.Type.REPLACE;
                break;
        }
        return mergeType;
    }
}
