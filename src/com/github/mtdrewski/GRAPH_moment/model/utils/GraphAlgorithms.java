package com.github.mtdrewski.GRAPH_moment.model.utils;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;

import java.util.Vector;

/*
Interface meant to use in testing, not in application itself.
Some methods may be ineffective.
 */

public interface GraphAlgorithms {

    public static boolean hasCycle(Graph graph) {
        Vector<Boolean> visited = new Vector<>(graph.size() + 1);
        for (int i = 0; i <= graph.size(); i++) visited.add(false);
        for (Vertex v : graph.getVertices()) {
            if (!visited.get(v.id()) && isInCycle(graph, visited, v.id(), 0))
                return true;
        }
        return false;
    }

    public static boolean isInCycle(Graph graph, Vector<Boolean> visited, int v, int prev) {
        visited.set(v, true);
        for (Edge e : graph.getEdges()) {
            if (e.vert1().id() == v) {
                if (e.vert2().id() != prev && visited.get(e.vert2().id()))
                    return true;
                else if (e.vert2().id() != prev && isInCycle(graph, visited, e.vert2().id(), v))
                    return true;
            }
            if (e.vert2().id() == v) {
                if (e.vert1().id() != prev && visited.get(e.vert1().id()))
                    return true;
                else if (e.vert1().id() != prev && isInCycle(graph, visited, e.vert1().id(), v))
                    return true;
            }
        }
        return false;
    }

    public static int numberOfComponents(Graph graph) {
        int num = 0;
        Vector<Boolean> visited = new Vector<>(graph.size() + 1);
        for (int i = 0; i <= graph.size(); i++) visited.add(false);
        for (Vertex v : graph.getVertices()) {
            if (!visited.get(v.id())) {
                num++;
                dfsDoNothing(graph, visited, v.id());
            }
        }
        return num;
    }

    public static void dfsDoNothing(Graph graph, Vector<Boolean> visited, int v) {
        visited.set(v, true);
        for (Edge e : graph.getEdges()) {
            if (e.vert1().id() != v && e.vert2().id() == v && !visited.get(e.vert1().id()))
                dfsDoNothing(graph, visited, e.vert1().id());
            else if (e.vert1().id() == v && e.vert2().id() != v && !visited.get(e.vert2().id()))
                dfsDoNothing(graph, visited, e.vert2().id());
        }
    }
}
