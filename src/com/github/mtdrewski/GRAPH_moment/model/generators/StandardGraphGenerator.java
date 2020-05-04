package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import javafx.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StandardGraphGenerator extends IntervalConstrainedGenerator {

    public StandardGraphGenerator(int minV, int maxV, int minE, int maxE) {
        super(minV, maxV, minE, maxE);
    }

    public StandardGraphGenerator(int v, int e) {
        super(v, v, e, e);
    }

    @Override
    public Graph generate() {
        Random random = new Random();
        int numOfVertices = random.nextInt(maxNumOfVertices - minNumOfVertices + 1) + minNumOfVertices;
        int numOfEdges = random.nextInt(maxNumOfEdges - minNumOfEdges + 1) + minNumOfEdges;
        numOfEdges = Math.min(numOfEdges, numOfVertices * (numOfVertices - 1) / 2);

        Graph graph = new Graph();
        for (int i = 0; i < numOfVertices; i++)
            graph.addVertex();

        List<Pair<Integer, Integer>> possibleEdges = Stream.iterate(new Pair<>(1, 2), p -> {
            if (p.getValue() == numOfVertices)
                return new Pair<>(p.getKey() + 1, p.getKey() + 2);
            else
                return new Pair<>(p.getKey(), p.getValue() + 1);
        }).limit(numOfVertices * (numOfVertices - 1) / 2).collect(Collectors.toList());

        Collections.shuffle(possibleEdges);

        for (int i = 0; i < numOfEdges; i++) {
            graph.addEdge(possibleEdges.get(i).getKey(), possibleEdges.get(i).getValue());
        }
        return graph;
    }
}
