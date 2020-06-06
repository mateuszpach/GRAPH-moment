package com.github.mtdrewski.GRAPH_moment.controller;

import com.github.mtdrewski.GRAPH_moment.Main;
import com.github.mtdrewski.GRAPH_moment.model.generators.IntervalConstrainedGenerator;
import com.github.mtdrewski.GRAPH_moment.model.graphs.*;
import com.github.mtdrewski.GRAPH_moment.model.processors.DataProcessor;
import com.github.mtdrewski.GRAPH_moment.model.utils.Coords;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class GraphDrawerController {

    @FXML
    private AnchorPane root;

    private File file = null;

    protected boolean cursorOverVertex = false;
    protected boolean cursorOverEdge = false;
    private boolean isUnsaved = false;

    private enum Mode {EDGE, SELECT, STANDARD, TYPING}
    protected boolean isDirected = false;

    private Mode mode;
    private EdgeLine currentEdge;
    private EdgeLine typingOnEdge;

    private VertexCircle sourceVertex = null;
    protected ArrayList<VertexCircle> selectedVertices;
    protected ArrayList<EdgeLine> selectedEdges;

    private Graph graph;

    public boolean isUnsaved() {
        return isUnsaved;
    }

    public void setUnsaved(boolean b) {
        isUnsaved = b;
    }

    public void setDirected(boolean value) { isDirected = value; }
    public boolean getDirected() { return isDirected; }

    public boolean isInEdgeMode() {
        return mode == Mode.EDGE;
    }
    public boolean isInSelectMode() {
        return mode == Mode.SELECT;
    }
    public boolean isInStandardMode() {
        return mode == Mode.STANDARD;
    }
    public boolean isInTypingMode() { return mode == Mode.TYPING; }

    protected EdgeLine getCurrentEdge() {
        return currentEdge;
    }
    protected VertexCircle getSourceVertex() {
        return sourceVertex;
    }

    protected AnchorPane getRoot() { return root; }

    private Supplier<VertexCircle> vertexShapeFactory = () -> {
        VertexCircle vertex = new VertexCircle(this);
        vertex.prepareLooks();
        return vertex;
    };

    public Supplier<EdgeLine> edgeLineFactory = () -> {
        EdgeLine edge;
        if (!isDirected)
            edge = new EdgeLine(this);
        else
            edge = new DirectedEdgeLine(this);
        edge.setStartVertex(sourceVertex);
        edge.prepareLooks();
        return edge;
    };

    public void initialize() {
        //TODO: Maybe GraphDrawer could remember own instance in some static field
        Main.setGraphDrawerController(this);
        MainController.setGraphDrawerController(this);
        NewProjectOptionsController.setGraphDrawerController(this);
        ReaderSaver.setGraphDrawerController(this);
        GenerateController.setGraphDrawerController(this);
        ImportController.setGraphDrawerController(this);
        ExportController.setGraphDrawerController(this);
        DataProcessor.setGraphDrawerController(this);
        IntervalConstrainedGenerator.setGraphDrawerController(this);
        graph = new Graph();
        mode = Mode.STANDARD;
        selectedVertices = new ArrayList<>();
        selectedEdges = new ArrayList<>();

        root.setOnMousePressed(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {

                if (mode == Mode.EDGE && !cursorOverVertex) {
                    exitEdgeMode(false);
                }

                if (mode == Mode.SELECT && !cursorOverVertex && !cursorOverEdge) {
                    deselectAll();
                }

                if (mode == Mode.STANDARD && e.getClickCount() == 2 && !cursorOverVertex) {
                    VertexCircle vertexShape = vertexShapeFactory.get();
                    vertexShape.setPosition(e);
                    vertexShape.appearOnScene();
                }
            }
        });
        root.setOnMouseMoved(e -> {
            if (currentEdge != null && mode == Mode.EDGE)
                currentEdge.followCursor(e);
        });
        root.setOnMouseDragged(e -> {
            if (currentEdge != null && mode == Mode.EDGE)
                currentEdge.followCursor(e);
        });
        root.setFocusTraversable(true);
        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.CONTROL && mode == Mode.STANDARD) {
                mode = Mode.SELECT;
            }
            else if (e.getCode() == KeyCode.DELETE) {
                deleteAll();
            }
            else if (e.getCode() == KeyCode.ALT && mode == Mode.STANDARD) {
                mode = Mode.TYPING;
                for (Node edge : root.getChildren()) {
                    if (edge.getClass().isAssignableFrom(DirectedEdgeLine.class)) {
                        ((EdgeLine) edge).editLabel(true);
                    }
                }
            }
            else if (e.getCode() == KeyCode.ALT && mode == Mode.TYPING) {
                mode = Mode.STANDARD;
                for (Node edge : root.getChildren()) {
                    if (edge.getClass().isAssignableFrom(DirectedEdgeLine.class)) {
                        ((EdgeLine) edge).editLabel(false);
                        ((EdgeLine) edge).underlyingEdge.setLabel(((EdgeLine) edge).label.getText());
                    }
                }
            }

        });
        root.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.CONTROL && mode == Mode.SELECT) {
                mode = Mode.STANDARD;
            }
        });
    }

    protected void enterEdgeMode(VertexCircle vertex, MouseEvent event) {
        sourceVertex = vertex;
        EdgeLine edge = edgeLineFactory.get();
        sourceVertex.addOutcomingEdge(edge);
        edge.followCursor(event);
        edge.appearOnScene();
        currentEdge = edge;
        mode = Mode.EDGE;
    }

    protected void exitEdgeMode(boolean success) {
        if (!success) {
            sourceVertex.removeOutcomingEdge(currentEdge);
            currentEdge.disappearFromScene();
        }

        currentEdge = null;
        sourceVertex = null;
        mode = Mode.STANDARD;
    }


    protected void deselectAll() {
        ArrayList<VertexCircle> toDeselectV = new ArrayList<>(selectedVertices);
        toDeselectV.stream().forEach(VertexCircle::deselect);
        ArrayList<EdgeLine> toDeselectE = new ArrayList<>(selectedEdges);
        toDeselectE.stream().forEach(EdgeLine::deselect);
    }

    private void deleteAll() {
        isUnsaved = true;

        selectedEdges.stream().forEach(e -> {
            graph.removeEdge(e.startVertex.id(), e.endVertex.id());
            e.startVertex.getOutcomingEdges().remove(e);
            e.endVertex.getOutcomingEdges().remove(e);
            e.disappearFromScene();
        });

        selectedVertices.stream().forEach(v -> {
            v.getOutcomingEdges().stream().forEach(EdgeLine::disappearFromScene);
            v.disappearFromScene();
            graph.removeVertex(v.id());
        });

        root.getChildren().stream().forEach(n -> {
            if (n.getClass().equals(VertexCircle.class)) {
                ((VertexCircle) n).refresh();
            }
        });

        selectedVertices.clear();
        selectedEdges.clear();
    }

    public void drawNewGraph(Graph newGraph) {
        isUnsaved = true;

        root.getChildren().clear();
        if (isDirected)
            graph = new DirectedGraph();
        else
            graph = new Graph();
        ArrayList<VertexCircle> vertexCircles = new ArrayList<>();


        for (Vertex graphVertex : newGraph.getVertices()) {
            VertexCircle vertexShape = vertexShapeFactory.get();
            vertexShape.setPosition(graphVertex.xPos(), graphVertex.yPos());
            vertexCircles.add(vertexShape);
            vertexShape.appearOnScene();
        }

        for (Edge graphEdge : newGraph.getEdges()) {

            EdgeLine edgeLine;
            if (isDirected)
                edgeLine = new DirectedEdgeLine(this);
            else
                edgeLine = new EdgeLine(this);

            edgeLine.prepareLooks();
            edgeLine.setStartVertex(vertexCircles.get(graphEdge.vert1().id() - 1));
            vertexCircles.get(graphEdge.vert1().id() - 1).addOutcomingEdge(edgeLine);

            edgeLine.appearOnScene();

            edgeLine.setEndVertex(vertexCircles.get(graphEdge.vert2().id() - 1));
            vertexCircles.get(graphEdge.vert2().id() - 1).addOutcomingEdge(edgeLine);
        }

        fruchtermanReingoldLayout(vertexCircles);

        currentEdge = null;
        sourceVertex = null;
    }


    public void fruchtermanReingoldLayout(ArrayList<VertexCircle> vertexCircles){

        double w = root.getWidth(), h = root.getHeight();
        double area = w*h;
        int nVertices=graph.getVertices().size();
        if(nVertices==0)
            return;

        double k = Math.sqrt(area/nVertices);
        double temperature = 1000;

        for(int m = 0; m< 900; m++){    //number of iterations

            for(int i = 0; i < graph.getVertices().size(); i++){    //first phase: calculate repulsive force between nodes
                Vertex v = graph.getVertices().get(i);
                v.setOffset(0,0);

                for(int j = 0; j< nVertices; j++){
                    Vertex u = graph.getVertices().get(j);
                    if(i!= j){
                        Coords delta = v.getPosCoords().subtract(u.getPosCoords());
                        double myFr = repulsiveForce(u,v,k);
                        v.setOffset(v.getOffset().add(delta.unit().scale(myFr)));
                    }
                }
            }


            for(Edge edge : graph.getEdges()){ //second phase: calculate attractive force in edges
                Vertex v = edge.vert1();
                Vertex u = edge.vert2();
                Coords delta = v.getPosCoords().subtract(u.getPosCoords());
                double myFa = attractiveForce(u,v,k);
                v.setOffset(v.getOffset().subtract(delta.unit().scale(myFa)));
                u.setOffset(u.getOffset().add(delta.unit().scale(myFa)));
            }


            for(int i = 0; i< nVertices; i++){ //third phase: limit displacement based on temperature
                Vertex v = graph.getVertices().get(i);
                Coords newCoord = (v.getPosCoords().add(v.getOffset().unit().scale(Math.min(v.getOffset().length(), temperature))));
                v.setPos(newCoord.getX(), newCoord.getY());
            }
            temperature *= 0.9;
        }

        //scale everything to fit the screen

        double scale=1;
        for(int i=0;i<nVertices;i++){
            Vertex v =graph.getVertices().get(i);
            double xC=v.xPos();
            double yC=v.yPos();
            if(xC>w/2)
                scale=Math.max(scale,(xC-(w/2))/(w/2-40));
            else
                scale=Math.max(scale,-(xC-(w/2))/(w/2-40));
            if(yC>h/2)
                scale=Math.max(scale,(yC-(h/2))/(h/2-40));
            else
                scale=Math.max(scale,-(yC-(h/2))/(h/2-40));
        }

        scale=1/scale;
        for(int i=0;i<nVertices;i++) {
            Vertex v = graph.getVertices().get(i);
            double xC = v.xPos();
            double yC = v.yPos();
            v.setPos(xC*scale+w/2,yC*scale+h/2);
        }

        for(int i=0;i<nVertices;i++)
            vertexCircles.get(i).setPosition(graph.getVertices().get(i).xPos(),graph.getVertices().get(i).yPos());

    }

    private double attractiveForce(Vertex ni, Vertex nj, double k){
        double  distance = ni.getPosCoords().distance(nj.getPosCoords());
        return distance*distance/k;
    }

    private double repulsiveForce(Vertex ni, Vertex nj, double k){
        double  distance = ni.getPosCoords().distance(nj.getPosCoords());
        return k*k/(distance+0.000001);
    }

    public Graph getGraph() {
        return graph;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        //TODO: after adding directed graphs title should look eg. MyGraph [directed/undirected] - GRAPH Moment
        ((Stage) root.getScene().getWindow()).setTitle(file.getName() + " [directed] - GRAPH Moment");
        this.file = file;
    }

    public Pair<Double, Double> getRandomCoords() {
        Random random = new Random();
        return new Pair<>(
                random.nextDouble() * (root.getWidth()), random.nextDouble() * (root.getHeight())
        );
    }
}
