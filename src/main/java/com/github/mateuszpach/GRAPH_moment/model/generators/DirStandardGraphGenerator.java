package com.github.mateuszpach.GRAPH_moment.model.generators;

import com.github.mateuszpach.GRAPH_moment.model.graphs.DirectedGraph;
import com.github.mateuszpach.GRAPH_moment.model.graphs.Graph;
import com.github.mateuszpach.GRAPH_moment.model.utils.OrderedPairGenerator;
import javafx.util.Pair;

import java.util.List;

public class DirStandardGraphGenerator extends StandardGraphGenerator {

    public DirStandardGraphGenerator(int minV, int maxV, int minE, int maxE) {
        super(minV, maxV, minE, maxE);
    }

    public DirStandardGraphGenerator(int minV, int maxV) {
        super(minV, maxV);
    }

    @Override
    protected int maxEdgesPossible(int numOfV) {
        return numOfV * numOfV - numOfV;
    }

    @Override
    protected List<Pair<Integer, Integer>> possibleEdges(Graph graph) {
        return OrderedPairGenerator.allPairsNonLoop(graph.size());
    }

    @Override
    public DirectedGraph generate() {
        DirectedGraph graph = new DirectedGraph();
        prepareVertices(graph);
        prepareEdges(graph);
        return graph;
    }
}
