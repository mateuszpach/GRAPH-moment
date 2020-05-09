package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TreeGeneratorTest {

    public boolean dfs(Graph graph, Vector<Boolean> visited, int v, int prev) {
        visited.set(v, true);
        for (Edge e : graph.getEdges()) {
            if (e.vert1().id() == v) {
                if (e.vert2().id() != prev && visited.get(e.vert2().id()))
                    return true;
                else if (e.vert2().id() != prev && dfs(graph, visited, e.vert2().id(), v))
                    return true;
            }
            if (e.vert2().id() == v) {
                if (e.vert1().id() != prev && visited.get(e.vert1().id()))
                    return true;
                else if (e.vert1().id() != prev && dfs(graph, visited, e.vert1().id(), v))
                    return true;
            }
        }
        return false;
    }

    public boolean hasCycle(Graph graph) {
        Vector<Boolean> visited = new Vector<>(graph.size() + 1);
        for (int i = 0; i <= graph.size(); i++) visited.add(false);
        for (Vertex v : graph.getVertices()) {
            if (!visited.get(v.id()) && dfs(graph, visited, v.id(), 0))
                return true;
        }
        return false;
    }

    @Test
    public void basic() {

        for (int i = 0; i < 100; i++) {
            Graph tree = new TreeGenerator(1, 1000).generate();
            assertEquals(tree.getVertices().size(), tree.getEdges().size() + 1);
            assertFalse(hasCycle(tree));
        }

    }
}