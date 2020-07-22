package com.github.mateuszpach.GRAPH_moment.model.utils;

import com.github.mateuszpach.GRAPH_moment.model.graphs.Edge;
import com.github.mateuszpach.GRAPH_moment.model.graphs.Graph;
import com.github.mateuszpach.GRAPH_moment.model.graphs.Vertex;
import javafx.scene.layout.AnchorPane;

public interface GraphEmbedder {

    static void fruchtermanReingoldLayout(Graph graph, AnchorPane root) {

        if (graph.size() < 2 || graph.size() > 50)
            return;

        double w = root.getWidth() - 40, h = root.getHeight() - 40;
        double area = w * h;
        int nVertices = graph.getVertices().size();
        if (nVertices == 0)
            return;

        double radius = 20.0;

        double k = Math.sqrt(area / nVertices) * (graph.getEdges().size() + 1);
        double temperature = 1000;
        for (int m = 0; m < Math.max(3, 40 - graph.size()); m++) {    //number of iterations

            for (int i = 0; i < graph.getVertices().size(); i++) {    //first phase: calculate repulsive force between nodes
                Vertex v = graph.getVertices().get(i);
                v.setOffset(0, 0);

                for (int j = 0; j < nVertices; j++) {
                    Vertex u = graph.getVertices().get(j);
                    if (i != j) {
                        Coords delta = v.getPosCoords().subtract(u.getPosCoords());
                        double myFr = repulsiveForce(u, v, k);
                        v.setOffset(v.getOffset().add(delta.unit().scale(myFr)));
                    }
                }
            }


            for (Edge edge : graph.getEdges()) { //second phase: calculate attractive force in edges
                Vertex v = edge.vert1();
                Vertex u = edge.vert2();
                Coords delta = v.getPosCoords().subtract(u.getPosCoords());
                double myFa = attractiveForce(u, v, k, radius);
                v.setOffset(v.getOffset().subtract(delta.unit().scale(myFa)));
                u.setOffset(u.getOffset().add(delta.unit().scale(myFa)));
            }


            for (int i = 0; i < nVertices; i++) { //third phase: limit displacement based on temperature
                Vertex v = graph.getVertices().get(i);
                Coords newCoord = (v.getPosCoords().add(v.getOffset().unit().scale(Math.min(v.getOffset().length(), temperature))));
                v.setPos(newCoord.getX(), newCoord.getY());
            }
            temperature *= 0.9;
        }

        //scale everything to fit the screen

        double scale = 1;
        for (int i = 0; i < nVertices; i++) {
            Vertex v = graph.getVertices().get(i);
            double xC = v.xPos();
            double yC = v.yPos();
            if (xC > w / 2)
                scale = Math.max(scale, (xC - (w / 2)) / (w / 2 - 40));
            else
                scale = Math.max(scale, -(xC - (w / 2)) / (w / 2 - 40));
            if (yC > h / 2)
                scale = Math.max(scale, (yC - (h / 2)) / (h / 2 - 40));
            else
                scale = Math.max(scale, -(yC - (h / 2)) / (h / 2 - 40));
        }

        scale = 1 / scale;
        for (int i = 0; i < nVertices; i++) {
            Vertex v = graph.getVertices().get(i);
            double xC = v.xPos();
            double yC = v.yPos();
            v.setPos(xC * scale + w / 2, yC * scale + h / 2);
        }
    }

    private static double attractiveForce(Vertex ni, Vertex nj, double k, double radius) {
        double distance = ni.getPosCoords().distance(nj.getPosCoords());
        if (distance < 4 * radius)
            return 0;
        else
            return distance * distance / k;
    }

    private static double repulsiveForce(Vertex ni, Vertex nj, double k) {
        double distance = ni.getPosCoords().distance(nj.getPosCoords());
        return k * k / (distance + 0.000001);
    }
}
