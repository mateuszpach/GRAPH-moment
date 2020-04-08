package com.github.mtdrewski.GRAPH_moment.model.graphs;

import java.util.Objects;

public class Vertex {

    private int id;
    private float xPos;
    private float yPos;

    protected Vertex(int id) {
        this.id = id;
        xPos = 0f;
        yPos = 0f;
    }

    protected Vertex(int id, float x, float y) {
        this.id = id;
        xPos = x;
        yPos = y;
    }

    public int id() {
        return id;
    }

    public float xPos() {
        return xPos;
    }

    public float yPos() {
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
