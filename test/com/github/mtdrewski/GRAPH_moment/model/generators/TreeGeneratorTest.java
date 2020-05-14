package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.auxiliary.GraphAlgorithms;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TreeGeneratorTest {

    @Test
    public void basic() { // test took 1.5s

        for (int i = 1; i <= 100; i++) {
            Graph tree = new TreeGenerator(1, 1000).generate();
            assertEquals(tree.getVertices().size(), tree.getEdges().size() + 1);
            assertFalse(GraphAlgorithms.hasCycle(tree));
            assertEquals(1, GraphAlgorithms.numberOfComponents(tree));
            System.out.println(i + " ok");
        }

    }
}