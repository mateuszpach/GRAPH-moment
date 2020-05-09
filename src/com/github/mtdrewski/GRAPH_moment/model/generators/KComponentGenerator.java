package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import javafx.util.Pair;

import java.util.List;

/*
Interval constraints passed to constructors are suposed to constrain a single component.
 */
public class KComponentGenerator extends StandardGraphGenerator {

    private int numOfComponents;

    public KComponentGenerator(int minV, int maxV, int minE, int maxE, int comp) {
        super(minV, maxV, minE, maxE);
        if (maxE < minV - 1 || comp <= 0)
            throw new IllegalArgumentException();
        numOfComponents = comp;
    }

    public KComponentGenerator(int v, int e, int comp) {
        super(v, e);
        if (e < v - 1 || comp <= 0)
            throw new IllegalArgumentException();
        numOfComponents = comp;
    }

    public KComponentGenerator(int minV, int maxV, int minE, int maxE) {
        super(minV, maxV, minE, maxE);
        if (maxE < minV - 1)
            throw new IllegalArgumentException();
        numOfComponents = 1;
    }

    public KComponentGenerator(int v, int e) {
        super(v, e);
        if (e < v - 1)
            throw new IllegalArgumentException();
        numOfComponents = 1;
    }

    @Override
    public void addEdges(Graph component, List<Pair<Integer, Integer>> possibleEdges, int numOfEdges) {
        int i = 0;
        while (component.getEdges().size() < numOfEdges) {
            if (!component.contains(possibleEdges.get(i).getKey(), possibleEdges.get(i).getValue())) {
                component.addEdge(possibleEdges.get(i).getKey(), possibleEdges.get(i).getValue());
            }
            i++;
        }
    }

    public Graph generateComponent() {
        Graph component = new TreeGenerator(minNumOfVertices, maxNumOfVertices).generate();
        prepareEdges(component);
        return component;
    }

    @Override
    public Graph generate() { //TODO finish when disjoint union will be ready and make unit tests
        Graph graph = new Graph();
        for (int i = 0; i < numOfComponents; i++) {
            Graph component = generateComponent();
            // disjoint union (graph <-- component)
        }
        graph.randomShuffle();
        return graph;
    }
}
