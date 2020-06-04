package com.github.mtdrewski.GRAPH_moment.model.graphs;

import com.github.mtdrewski.GRAPH_moment.model.utils.Coords;

import java.util.Objects;

public class Vertex implements Comparable<Vertex> {

    private int id;

    private Coords posCoords;
    private Coords offsetCoords; //it's for Force-directed graph drawing

    protected Vertex(int id) {
        this.id = id;
        posCoords=new Coords(0,0);
        offsetCoords=new Coords(0,0);
    }

    protected Vertex(int id, double x, double y) {
        this.id = id;
        posCoords=new Coords(x,y);
        offsetCoords=new Coords(0,0);
    }

    public int id() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public void setPos(double x, double y) {
        posCoords.setX(x);
        posCoords.setY(y);
    }

    public void setPos(Coords pos) {
        posCoords=pos;
    }

    public double xPos() {
        return posCoords.getX();
    }

    public double yPos() {
        return posCoords.getY();
    }

    public Coords getPosCoords(){ return posCoords;}


    public void setOffset(double x,double y){
        offsetCoords.setX(x);
        offsetCoords.setY(y);
    }

    public void setOffset(Coords offset){
        offsetCoords=offset;
    }

    public Coords getOffset(){
        return offsetCoords;
    }


    @Override
    public String toString() {
        return Integer.toString(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Vertex o) {
        return id - o.id();
    }
}
