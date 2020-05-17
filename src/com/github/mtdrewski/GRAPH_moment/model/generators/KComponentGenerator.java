package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.utils.GraphMerger;
import javafx.util.Pair;

import java.util.List;
import java.util.Random;

/*
Interval constraints passed to constructors are suposed to constrain a single component.
 */
public class KComponentGenerator extends StandardGraphGenerator {

    private final int minNumOfComponents;
    private final int maxNumOfComponents;

    public KComponentGenerator(int minV, int maxV, int minE, int maxE, int minComp, int maxComp) {
        super(minV, maxV, minE, maxE);
        if (maxE < minV - 1 || minComp <= 0 || maxComp < minComp)
            throw new IllegalArgumentException();
        minNumOfComponents = minComp;
        maxNumOfComponents = maxComp;
    }

    public KComponentGenerator(int minV, int maxV, int minE, int maxE) {
        super(minV, maxV, minE, maxE);
        if (maxE < minV - 1)
            throw new IllegalArgumentException();
        minNumOfComponents = 1;
        maxNumOfComponents = 1;
    }

    public KComponentGenerator(int minV, int maxV, int minComp, int maxComp, boolean dummy) {
        super(minV, maxV, minV - 1, maxV*(maxV-1)/2);
        minNumOfComponents = minComp;
        maxNumOfComponents = maxComp;
    }

    public KComponentGenerator(int minV, int maxV) {
        super(minV, maxV, minV - 1, maxV*(maxV-1)/2);
        minNumOfComponents = 1;
        maxNumOfComponents = 1;
    }

    @Override
    protected void addEdges(Graph component, List<Pair<Integer, Integer>> possibleEdges, int numOfEdges) {
        int i = 0;
        while (component.getEdges().size() < numOfEdges) {
            if (!component.contains(possibleEdges.get(i).getKey(), possibleEdges.get(i).getValue())) {
                component.addEdge(possibleEdges.get(i).getKey(), possibleEdges.get(i).getValue());
            }
            i++;
        }
    }

    private Graph generateComponent() {
        Graph component = new TreeGenerator(minNumOfVertices, maxNumOfVertices).generate();
        prepareEdges(component);
        return component;
    }

    @Override
    public Graph generate() { //TODO finish when disjoint union will be ready and make unit tests
        Graph graph = new Graph();
        Random random = new Random();
        int numOfComponents = random.nextInt(maxNumOfComponents - minNumOfComponents + 1) + minNumOfComponents;
        for (int i = 0; i < numOfComponents; i++) {
            Graph component = generateComponent();
            graph = GraphMerger.disjointUnion(graph, component);
        }
        graph.randomShuffle();
        return graph;
    }
}
