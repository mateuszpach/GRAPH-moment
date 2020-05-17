package com.github.mtdrewski.GRAPH_moment.model.generators;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;

import java.util.Random;
import java.util.Vector;

public class TreeGenerator extends StandardGraphGenerator {

    public TreeGenerator(int minV, int maxV) {
        super(minV, maxV, minV-1, maxV-1);
    }

    private Vector<Integer> pruferCode(int n) {
        Vector<Integer> code = new Vector<>();
        Random random = new Random();
        for (int i = 0; i < n - 2; i++) {
            code.add(Math.abs(random.nextInt(n) + 1));
        }
        return code;
    }

    /* Generating with using Prufer method. */
    @Override
    protected void prepareEdges(Graph tree) {
        if (tree.size() == 1 || tree.size() == 0)
            return;

        Vector<Integer> code = pruferCode(tree.size());
        Vector<Integer> ocurrences = new Vector<>();
        for (int i = 0; i <= tree.size(); i++) {
            ocurrences.add(0);
        }
        for (int i = 0; i < code.size(); i++) {
            ocurrences.setElementAt(ocurrences.get(code.get(i)) + 1, code.get(i));
        }

        for (int i = 0; i < code.size(); i++) {
            for (int j = 1; j <= tree.size(); j++) {
                if (ocurrences.get(j) == 0) {
                    ocurrences.setElementAt(-1, j);
                    ocurrences.setElementAt(ocurrences.get(code.get(i)) - 1, code.get(i));
                    tree.addEdge(j, code.get(i));
                    break;
                }
            }
        }

        boolean found = false;
        int v1 = -1, v2 = -1;
        for (int i = 1; i <= tree.size(); i++) {
            if (!found && ocurrences.get(i) == 0) {
                v1 = i;
                found = true;
            }
            else if (found && ocurrences.get(i) == 0) {
                v2 = i;
            }
        }
        tree.addEdge(v1, v2);
    }
}
