package com.github.mtdrewski.GRAPH_moment.model.processors;

import com.github.mtdrewski.GRAPH_moment.model.graphs.Edge;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Graph;
import com.github.mtdrewski.GRAPH_moment.model.graphs.Vertex;

import java.util.Random;

//TODO: consider changing methods to static and then adjusting import/export controllers
//TODO: (IMPORTANT) let user have spaces before endl
public class DataProcessor {
    //TODO: when implemented differentiate between directed and undirected,weighted and unweighted edges
    //TODO implement writing output out of given graph
    public enum Type {
        ADJACENCY_MATRIX, EDGE_LIST, INCIDENCE_MATRIX;
    }

    private Graph inputGraph;
    private Type inputType;

    //textInput is in form of regular representation of graph such as in algorithm tasks
    public Graph makeGraphFromInput(String textInput, Type tempType) throws IncorrectInputFormatException {
        if (textInput == null || tempType == null)
            throw new IncorrectInputFormatException(tempType, textInput);
        inputType = tempType;
        inputGraph = new Graph();
        switch (inputType) {
            case EDGE_LIST:
                readEdgeList(textInput);
                break;
            case ADJACENCY_MATRIX:
                readAdjacencyMatrix(textInput);
                break;
            case INCIDENCE_MATRIX:
                readIncidenceMatrix(textInput);
                break;
        }

        Random r = new Random();
        double max = 800.0;       //TODO: make it change with the Panel size
        double min = 100.0;
        for (Vertex v1 : inputGraph.getVertices())
            v1.setPos(min + r.nextDouble() * (max - min), min + r.nextDouble() * (max - min));


        return inputGraph;
    }

    //TODO: implement this
    public String makeOutputFromGraph(Graph graph, Type graphType) {
        String output = "";
        switch (graphType) {
            case EDGE_LIST:
                output = createEdgeList(graph);
                break;
            case ADJACENCY_MATRIX:
                output = createAdjacencyMatrix(graph);
                break;
            //TODO case for Incidence matrix
        }

        return output;
    }

    private String createEdgeList(Graph graph) {
        StringBuilder edgeList = new StringBuilder("");
        edgeList.append(graph.size()).append("\n");
        edgeList.append(graph.getEdges().size()).append("\n");

        for (Edge edge : graph.getEdges()) {
            edgeList.append(edge.vert1().id()).append(" ").append(edge.vert2().id()).append("\n");
        }

        return edgeList.toString();
    }

    private String createAdjacencyMatrix(Graph graph) {
        StringBuilder matrix = new StringBuilder("");

        for (int i = 0; i < graph.size(); i++) {
            for (int j = 0; j < graph.size() - 1; j++) {
                matrix.append(0).append(" ");
            }
            matrix.append(0).append("\n");
        }

        for (Edge edge : graph.getEdges()) {
            matrix.setCharAt(2 * (edge.vert1().id() - 1) * graph.size() + 2 * edge.vert2().id() - 2, '1');
            matrix.setCharAt(2 * (edge.vert2().id() - 1) * graph.size() + 2 * edge.vert1().id() - 2, '1');
        }

        return matrix.toString();
    }

    void readEdgeList(String textInput) throws IncorrectInputFormatException { //1-st line ->n,2-nd line-> m, rest: 2 edges and 1-indexed
        try {
            String[] lines = textInput.split(System.getProperty("line.separator"));
            int n = Integer.parseInt(lines[0]);
            int m = Integer.parseInt(lines[1]);
            for (int i = 0; i < n; i++)
                inputGraph.addVertex();

            for (int i = 0; i < m; i++) {
                String[] edge = lines[i + 2].split(" ");
                int v1 = Integer.parseInt(edge[0]);
                int v2 = Integer.parseInt(edge[1]);
                inputGraph.addEdge(v1, v2);
            }
        } catch (Exception e) {
            throw new IncorrectInputFormatException(inputType, textInput);
        }
    }

    String[][] getMatrix(String textInput) {
        String[] lines = textInput.split(System.getProperty("line.separator"));
        String[][] matrix = new String[lines.length][];
        int r = 0;
        for (String line : lines) {
            matrix[r++] = line.split(" ");
        }
        return matrix;
    }


    void readAdjacencyMatrix(String textInput) throws IncorrectInputFormatException {
        try {
            String[][] matrix = getMatrix(textInput);
            int n = matrix.length;
            for (int i = 0; i < n; i++)
                inputGraph.addVertex();

            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    if (matrix[i][j].equals("1"))
                        inputGraph.addEdge(i + 1, j + 1);
                    if (!matrix[i][j].equals(matrix[j][i]))
                        throw new IncorrectInputFormatException(inputType, textInput);
                }
            }
        } catch (Exception e) {
            throw new IncorrectInputFormatException(inputType, textInput);
        }
    }

    void readIncidenceMatrix(String textInput) throws IncorrectInputFormatException {
        try {
            String[][] matrix = getMatrix(textInput);
            int n = matrix.length;
            for (int i = 0; i < n; i++)
                inputGraph.addVertex();

            int m = matrix[0].length;
            for (int i = 0; i < m; i++) {
                int v1 = -1, v2 = -1;
                for (int j = 0; j < n; j++) {
                    if (matrix[j][i].equals("1")) {
                        if (v2 != -1)
                            throw new IncorrectInputFormatException(inputType, textInput);
                        v2 = v1;
                        v1 = j + 1;
                    }
                }
                inputGraph.addEdge(v1, v2);
            }
        } catch (Exception e) {
            throw new IncorrectInputFormatException(inputType, textInput);
        }
    }

    public static class IncorrectInputFormatException extends Exception {
        String textInput;
        Type inputType;

        IncorrectInputFormatException(Type type, String text) {
            inputType = type;
            textInput = text;
        }

        @Override
        public String toString() {
            return "Error, given input: " + textInput + " of type:" + inputType.toString() + " is in incorrect format!";
        }
    }
}