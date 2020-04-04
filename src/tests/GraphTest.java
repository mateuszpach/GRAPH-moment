package tests;

import graphs.Graph;
import org.junit.Test;

import static org.junit.Assert.*;

public class GraphTest {

    @Test
    public void basicVertexEdgeAdding() {

        Graph graph = new Graph();

        graph.addVertex(1f, 1f);
        assertTrue(graph.contains(1));
        graph.addVertex(1f, 1f);
        graph.addVertex(1f, 1f);
        assertTrue(graph.contains(2));
        assertTrue(graph.contains(3));
        assertFalse(graph.contains(4));
        assertFalse(graph.contains(-1));

        assertEquals("[1, 2, 3], []", graph.toString());

        graph.addEdge(1, 2);
        assertEquals("[1, 2, 3], [{1, 2}]", graph.toString());

        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        assertTrue(graph.contains(1, 2));
        assertTrue(graph.contains(1, 3));
        assertTrue(graph.contains(2, 3));

        assertTrue(graph.contains(3, 2));
        assertTrue(graph.contains(3, 1));
        assertTrue(graph.contains(2, 1));

        assertEquals("[1, 2, 3], [{1, 2}, {1, 3}, {2, 3}]", graph.toString());
    }

    @Test
    public void exceptionsWhenAddingEdge() {

        Graph graph = new Graph();

        boolean flag = false;

        try {
            graph.addEdge(0, 1);
        }
        catch (Graph.NonExistingVertexException e) {
            flag = true;
        }
        finally {
            assertTrue(flag);
            flag = false;
        }

        graph.addVertex();
        graph.addVertex();
        graph.addVertex();
        graph.addVertex();

        try {
            graph.addEdge(1, 1);
        }
        catch (Graph.LoopEdgeException e) {
            flag = true;
        }
        finally {
            assertTrue(flag);
            flag = false;
        }

        try {
            graph.addEdge(1, -1);
        }
        catch (Graph.NonExistingVertexException e) {
            flag = true;
        }
        finally {
            assertTrue(flag);
            flag = false;
        }

        try {
            graph.addEdge(2, 5);
        }
        catch (Graph.NonExistingVertexException e) {
            flag = true;
        }
        finally {
            assertTrue(flag);
            flag = false;
        }

        try {
            graph.addEdge(1, 4);
        }
        catch (Graph.NonExistingVertexException e) {
            flag = true;
        }
        finally {
            assertFalse(flag);
            flag = false;
        }
    }
}