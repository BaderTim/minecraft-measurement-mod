package io.github.mmm.measurement.survey.objects.graph;

import org.joml.Vector3f;

public class Vertex {

    private Vector3f position;
    private int index;

    public Vertex(Vector3f vec, int index) {
        this.position = vec;
        this.index = index;
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getIndex() {
        return index;
    }
}
