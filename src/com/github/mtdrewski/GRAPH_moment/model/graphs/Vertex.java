package com.github.mtdrewski.GRAPH_moment.model.graphs;

import java.util.Objects;

public class Vertex {

    private int id;
    private double xPos;
    private double yPos;

    protected Vertex(int id) {
        this.id = id;
        xPos = 0f;
        yPos = 0f;
    }

    protected Vertex(int id, double x, double y) {
        this.id = id;
        xPos = x;
        yPos = y;
    }

    public int id() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public void setPos(double x, double y) {
        xPos = x;
        yPos = y;
    }

    public double xPos() {
        return xPos;
    }

    public double yPos() {
        return yPos;
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
}
