package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.utils.OrderedPairGenerator;
import javafx.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StandardGraphGenerator extends IntervalConstrainedGenerator {

    public StandardGraphGenerator(int minV, int maxV, int minE, int maxE) {
        super(minV, maxV, minE, maxE);
    }

    public StandardGraphGenerator(int v, int e) { super(v, v, e, e); }

    public StandardGraphGenerator(int v) { super(v, v, 0, v * (v - 1) / 2); }

    public void prepareVertices(Graph graph) {
        Random random = new Random();
        int numOfVertices = random.nextInt(maxNumOfVertices - minNumOfVertices + 1) + minNumOfVertices;
        for (int i = 0; i < numOfVertices; i++)
            graph.addVertex();
    }

    public void prepareEdges(Graph graph) {
        int numOfEdges = new Random().nextInt(maxNumOfEdges - minNumOfEdges + 1) + minNumOfEdges;
        numOfEdges = Math.min(numOfEdges, graph.size() * (graph.size() - 1) / 2);
        List<Pair<Integer, Integer>> possibleEdges = possibleEdges(graph);
        orderPossibleEdges(possibleEdges);
        addEdges(graph, possibleEdges, numOfEdges);
    }

    public List<Pair<Integer, Integer>> possibleEdges(Graph graph) {
        return OrderedPairGenerator.lexicographicalPairs(graph.size());
    }

    public void orderPossibleEdges(List<Pair<Integer, Integer>> possibleEdges) {
        Collections.shuffle(possibleEdges);
    }

    public void addEdges(Graph graph, List<Pair<Integer, Integer>> possibleEdges, int numOfEdges) {
        for (int i = 0; i < numOfEdges; i++) {
            graph.addEdge(possibleEdges.get(i).getKey(), possibleEdges.get(i).getValue());
        }
    }

    @Override
    public Graph generate() {
        Graph graph = new Graph();
        prepareVertices(graph);
        prepareEdges(graph);
        return graph;
    }
}
