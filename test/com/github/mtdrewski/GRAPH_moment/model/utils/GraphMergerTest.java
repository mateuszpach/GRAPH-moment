package com.github.mtdrewski.GRAPH_moment.model.utils;

import com.github.mtdrewski.GRAPH_moment.model.generators.StandardGraphGenerator;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import javafx.util.Pair;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class GraphMergerTest {
    @Test
    public void unionTest() {

        StandardGraphGenerator stdGen = new StandardGraphGenerator(10, 100, 0, 200*199/2);
        for (int i = 0; i < 50; i++) {

            Graph gr1 = stdGen.generate();
            Graph gr2 = stdGen.generate();
            Graph united = GraphMerger.union(gr1, gr2);

            assertEquals(Math.max(gr1.size(), gr2.size()), united.size());
            List<Pair<Integer, Integer>> pairs = OrderedPairGenerator.lexicographicalPairs(Math.max(gr1.size(), gr2.size()));
            for (Pair<Integer, Integer> pair : pairs) {
                assertEquals((gr1.contains(pair.getKey(), pair.getValue()) || gr2.contains(pair.getKey(), pair.getValue())),
                              united.contains(pair.getKey(), pair.getValue()));
            }
        }
    }

    @Test
    public void disjointUnionTest() {

        StandardGraphGenerator stdGen = new StandardGraphGenerator(10, 100, 0, 100*99/2);
        for (int i = 0; i < 50; i++) {

            Graph gr1 = stdGen.generate();
            Graph gr2 = stdGen.generate();
            Graph united = GraphMerger.disjointUnion(gr1, gr2);

            assertEquals(gr1.size() + gr2.size(), united.size());
            assertEquals(gr1.getEdges().size() + gr2.getEdges().size(), united.getEdges().size());
            List<Pair<Integer, Integer>> pairs = OrderedPairGenerator.lexicographicalPairs(Math.max(gr1.size(), gr2.size()));
            for (Pair<Integer, Integer> pair : pairs) {
                if (pair.getKey() > gr1.size() || pair.getValue() > gr1.size()) {
                    assertEquals(gr2.contains(pair.getKey() - gr1.size(), pair.getValue() - gr1.size()),
                                 united.contains(pair.getKey(), pair.getValue()));
                }
                else {
                    assertEquals(gr1.contains(pair.getKey(), pair.getValue()),
                            united.contains(pair.getKey(), pair.getValue()));
                }
            }
        }
    }
}