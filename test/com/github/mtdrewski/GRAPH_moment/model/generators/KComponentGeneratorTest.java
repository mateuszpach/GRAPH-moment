package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.auxiliary.GraphAlgorithms;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KComponentGeneratorTest {

    @Test
    public void basicTest() { // test took 2m 3s

        for (int i = 1; i <= 100; i++) {
            Graph graph = new KComponentGenerator(1, 100, 0, 100*99/2, i).generate();
            assertEquals(GraphAlgorithms.numberOfComponents(graph), i);
            System.out.println(i + " ok");
        }
    }
}