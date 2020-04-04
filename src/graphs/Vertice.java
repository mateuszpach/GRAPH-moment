package graphs;

import java.util.Objects;

public class Vertice {

    private static int numberOfVerticesOnScene = 0;

    public static int getNumberOfVerticesOnScene() {
        return numberOfVerticesOnScene;
    }

    private int id;
    private float xPos;
    private float yPos;

    Vertice() {
        numberOfVerticesOnScene++;
        id = numberOfVerticesOnScene;
        xPos = 0f;
        yPos = 0f;
    }

    Vertice(float x, float y) {
        numberOfVerticesOnScene++;
        id = numberOfVerticesOnScene;
        xPos = x;
        yPos = y;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertice vertice = (Vertice) o;
        return id == vertice.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
