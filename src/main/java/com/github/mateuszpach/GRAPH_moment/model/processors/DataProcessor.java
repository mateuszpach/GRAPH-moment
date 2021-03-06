package com.github.mateuszpach.GRAPH_moment.model.processors;

import com.github.mateuszpach.GRAPH_moment.controller.GraphDrawerController;
import com.github.mateuszpach.GRAPH_moment.model.graphs.DirectedGraph;
import com.github.mateuszpach.GRAPH_moment.model.graphs.Edge;
import com.github.mateuszpach.GRAPH_moment.model.graphs.Graph;
import com.github.mateuszpach.GRAPH_moment.model.graphs.Vertex;
import javafx.util.Pair;

public class DataProcessor {
    public enum Type {
        ADJACENCY_MATRIX, EDGE_LIST
    }

    private Graph inputGraph;
    private Type inputType;
    private static GraphDrawerController graphDrawerController;

    //textInput is in form of regular representation of graph such as in algorithm tasks
    public Graph makeGraphFromInput(String textInput, Type tempType) throws IncorrectInputFormatException {
        if (textInput == null || tempType == null)
            throw new IncorrectInputFormatException(tempType, textInput);
        inputType = tempType;

        inputGraph = new Graph();
        if (graphDrawerController.isDirected())
            inputGraph = new DirectedGraph();
        else
            inputGraph = new Graph();

        switch (inputType) {
            case EDGE_LIST:
                readEdgeList(textInput);
                break;
            case ADJACENCY_MATRIX:
                readAdjacencyMatrix(textInput);
                break;
        }

        for (Vertex v1 : inputGraph.getVertices()) {
            Pair<Double, Double> coords = graphDrawerController.getRandomCoords();
            v1.setPos(coords.getKey(), coords.getValue());
        }

        return inputGraph;
    }

    //TODO: implement this
    public String makeOutputFromGraph(Graph graph, Type graphType) throws NullPointerException {
        String output = "";
        if (graph == null || graphType == null) throw new NullPointerException();
        switch (graphType) {
            case EDGE_LIST:
                output = createEdgeList(graph);
                break;
            case ADJACENCY_MATRIX:
                output = createAdjacencyMatrix(graph);
                break;
        }
        return output;
    }

    public String makeFullOutputFromGraph(Graph graph) {
        StringBuilder output = new StringBuilder();
        output.append("**Autogenerated GRAPH-moment project file. Do not edit manually.**\n");
        output.append("------------------------------BEGIN-------------------------------\n");
        if (graphDrawerController.isDirected()) {
            output.append("TYPE:directed\n");
        } else {
            output.append("TYPE:undirected\n");
        }
        output.append("N:").append(graph.size()).append("\n");
        output.append("M:").append(graph.getEdges().size()).append("\n");
        output.append("VERTICES:\n");
        for (Vertex vertex : graph.getVertices()) {
            output.append(vertex.xPos()).append(" ").append(vertex.yPos()).append("\n");
        }
        output.append("EDGES:\n");
        for (Edge edge : graph.getEdges()) {
            output.append(edge.vert1().id()).append(" ").append(edge.vert2().id()).append(" ");
            //output.append(edge.label)??? //TODO: IMPORTANT adjust it according to labels implementation
            output.append("\n");
        }
        output.append("-------------------------------END--------------------------------\n");
        return output.toString();
    }

    public Graph makeFullGraphFromInput(String textInput) {
        Graph graph;

        String[] lines = textInput.split(System.getProperty("line.separator"));
        String type = lines[2].substring(5);
        if (type.equals("directed")) {
            graph = new DirectedGraph();
            graphDrawerController.setDirected(true);
        } else {
            graph = new Graph();
            graphDrawerController.setDirected(false);
        }
        int n = Integer.parseInt(lines[3].substring(2));
        int m = Integer.parseInt(lines[4].substring(2));
        for (int i = 6; i < 6 + n; i++) {
            String[] line = lines[i].split(" ");
            double x = Double.parseDouble(line[0]);
            double y = Double.parseDouble(line[1]);
            graph.addVertex(x, y);
        }
        for (int i = 7 + n; i < 7 + n + m; i++) {
            String[] line = lines[i].split(" ");
            int v1 = Integer.parseInt(line[0]);
            int v2 = Integer.parseInt(line[1]);
            //String = line[2] ??? //TODO: IMPORTANT adjust it according to labels implementation
            graph.addEdge(v1, v2);
        }
        return graph;
    }

    private String createEdgeList(Graph graph) {
        StringBuilder edgeList = new StringBuilder();
        edgeList.append(graph.size()).append("\n");
        edgeList.append(graph.getEdges().size()).append("\n");

        for (Edge edge : graph.getEdges()) {
            edgeList.append(edge.vert1().id()).append(" ").append(edge.vert2().id()).append("\n");
        }

        return edgeList.toString();
    }

    private String createAdjacencyMatrix(Graph graph) {
        StringBuilder matrix = new StringBuilder();

        for (int i = 0; i < graph.size(); i++) {
            for (int j = 0; j < graph.size() - 1; j++) {
                matrix.append(0).append(" ");
            }
            matrix.append(0).append("\n");
        }

        for (Edge edge : graph.getEdges()) {
            matrix.setCharAt(2 * (edge.vert1().id() - 1) * graph.size() + 2 * edge.vert2().id() - 2, '1');
            if (!graphDrawerController.isDirected())
                matrix.setCharAt(2 * (edge.vert2().id() - 1) * graph.size() + 2 * edge.vert1().id() - 2, '1');
        }

        return matrix.toString();
    }

    public void readEdgeList(String textInput) throws IncorrectInputFormatException { //1-st line ->n,2-nd line-> m, rest: 2 edges and 1-indexed
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

    public String[][] getMatrix(String textInput) {
        String[] lines = textInput.split(System.getProperty("line.separator"));
        String[][] matrix = new String[lines.length][];
        int r = 0;
        for (String line : lines) {
            matrix[r++] = line.split(" ");
        }
        return matrix;
    }


    public void readAdjacencyMatrix(String textInput) throws IncorrectInputFormatException {
        try {
            if (textInput.length() == 0)
                throw new IncorrectInputFormatException(inputType, textInput);
            String[][] matrix = getMatrix(textInput);
            int n = matrix.length;
            for (int i = 0; i < n; i++)
                inputGraph.addVertex();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (matrix[i][j].equals("1"))
                        inputGraph.addEdge(i + 1, j + 1);
                }
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

    public static void setGraphDrawerController(GraphDrawerController graphDrawer) {
        graphDrawerController = graphDrawer;
    }
}