package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class TreeGenerator extends StandardGraphGenerator {

    public TreeGenerator(int v) {
        super(v, v-1);
    }

    public TreeGenerator(int minV, int maxV) {
        super(minV, maxV, minV-1, maxV-1);
    }

    private void dfs(Vector<Boolean> visited, Vector<Integer> neighbours, Graph tree, int v) {
        visited.set(v, true);
        Collections.shuffle(neighbours);
        for (Integer neighbour : neighbours) {
            if (!visited.get(neighbour)) {
                tree.addEdge(v, neighbour);
                dfs(visited, neighbours, tree, neighbour);
            }
        }
    }

    @Override
    public void prepareEdges(Graph tree) {
        Vector<Integer> neighbours = new Vector<>(tree.size());
        for (int i = 1; i <= tree.size(); i++) neighbours.add(i);
        Vector<Boolean> visited = new Vector<>(tree.size() + 1);
        for (int i = 0; i <= tree.size(); i++) visited.add(false);
        int source = new Random().nextInt(tree.size()) + 1;
        dfs(visited, neighbours, tree, source);
    }
}
