package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectedGraphGeneratorTest {

    @Test
    public void repeatableEdges() {

        DirStandardGraphGenerator gen = new DirStandardGraphGenerator(50, 50, 5000, 5000);
        for (int i = 0; i < 100; i++) {
            Graph g = gen.generate();
            for (Edge e1 : g.getEdges()) {
                for (Edge e2 : g.getEdges()) {
                    if (e1.equals(e2) && e1 != e2) {
                        assertTrue(false);
                    }
                }
            }
        }

        DirTreeGenerator gen1 = new DirTreeGenerator(50, 50);
        for (int i = 0; i < 100; i++) {
            Graph g = gen1.generate();
            for (Edge e1 : g.getEdges()) {
                for (Edge e2 : g.getEdges()) {
                    if (e1.equals(e2) && e1 != e2) {
                        assertTrue(false);
                    }
                }
            }
        }

        DirKComponentGenerator gen2 = new DirKComponentGenerator(25, 25, 5000, 5000, 3, 3);
        for (int i = 0; i < 100; i++) {
            Graph g = gen2.generate();
            for (Edge e1 : g.getEdges()) {
                for (Edge e2 : g.getEdges()) {
                    if (e1.equals(e2) && e1 != e2) {
                        assertTrue(false);
                    }
                }
            }
        }
    }
}