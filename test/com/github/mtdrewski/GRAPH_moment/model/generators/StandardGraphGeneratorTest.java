package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import org.junit.Test;

import static org.junit.Assert.*;

public class StandardGraphGeneratorTest {

    @Test
    public void graphCreationThrowing() {

        // should not throw
        try {
            Graph g0 = new StandardGraphGenerator(1, 1, 0, 100).generate();
            Graph g1 = new StandardGraphGenerator(10, 100, 1, 3000).generate();
            Graph g2 = new StandardGraphGenerator(15, 30, 0, 0).generate();
            Graph g3 = new StandardGraphGenerator(4,6).generate();
            Graph g4 = new StandardGraphGenerator(100, 100).generate();

            System.out.println(g0);
            System.out.println(g1);
            System.out.println(g2);
            System.out.println(g3);
            System.out.println(g4);

            assertTrue(g0.getVertices().size() == 1 && g0.getEdges().size() == 0);
            assertTrue(g1.getVertices().size() >= 10 && g1.getEdges().size() >= 1);
            assertTrue(g1.getVertices().size() <= 100 && g1.getEdges().size() <= 100*99/2);
            assertTrue(g2.getVertices().size() >= 15 && g2.getVertices().size() <= 30);
            assertTrue(g2.getEdges().size() == 0);
            assertTrue(g3.getVertices().size() == 4 && g3.getEdges().size() == 6);
            assertTrue(g4.getVertices().size() == 100 && g4.getEdges().size() == 100);

        }
        catch (IllegalArgumentException e) {
            assertTrue(false);
        }

        // should throw
        try {
            Graph g0 = new StandardGraphGenerator(1, 1, 1, 1).generate();
            assertTrue(false);
        }
        catch (IllegalArgumentException e) {
        }
        try {
            Graph g0 = new StandardGraphGenerator(0, 100, 0, 100).generate();
            assertTrue(false);
        }
        catch (IllegalArgumentException e) {
        }
        try {
            Graph g0 = new StandardGraphGenerator(5, 4, 1, 1).generate();
            assertTrue(false);
        }
        catch (IllegalArgumentException e) {
        }
        try {
            Graph g0 = new StandardGraphGenerator(1, 112, 40, 41).generate();
            assertTrue(false);
        }
        catch (IllegalArgumentException e) {
        }
        try {
            Graph g0 = new StandardGraphGenerator(1, 1, -1, 1).generate();
            assertTrue(false);
        }
        catch (IllegalArgumentException e) {
        }
    }
}