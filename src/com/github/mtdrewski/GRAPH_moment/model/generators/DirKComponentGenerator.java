package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.DirectedGraph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.utils.GraphMerger;
import com.github.mtdrewski.GRAPH_moment.model.utils.OrderedPairGenerator;
import javafx.util.Pair;

import java.util.List;

public class DirKComponentGenerator extends KComponentGenerator {

    public DirKComponentGenerator(int minV, int maxV, int minE, int maxE, int minComp, int maxComp) {
        super(minV, maxV, minE, maxE, minComp, maxComp);
    }

    public DirKComponentGenerator(int minV, int maxV, int minE, int maxE) {
        super(minV, maxV, minE, maxE);
    }

    public DirKComponentGenerator(int minV, int maxV, int minComp, int maxComp, boolean dummy) {
        super(minV, maxV, minComp, maxComp, dummy);
    }

    public DirKComponentGenerator(int minV, int maxV) {
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
    protected Graph generateComponentAndMerge(Graph graph) {
        DirectedGraph component = new DirTreeGenerator(minNumOfVertices, maxNumOfVertices).generate();
        prepareEdges(component);
        return GraphMerger.disjointUnion(graph, component, true);
    }
}
