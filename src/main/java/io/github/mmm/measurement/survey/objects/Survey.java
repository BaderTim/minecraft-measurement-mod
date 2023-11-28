package io.github.mmm.measurement.survey.objects;

import io.github.mmm.measurement.survey.objects.graph.Edge;
import io.github.mmm.measurement.survey.objects.graph.Vertex;

import java.util.ArrayList;

public class Survey {

    private ArrayList<Edge> edges;
    private ArrayList<Vertex> vertices;

    private int positionVertexIndex;

    public Survey() {
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
        positionVertexIndex = 0;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public Edge getEdge(int index) {
        return edges.get(index);
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertex(int index) {
        return vertices.get(index);
    }

    public int getPositionVertexIndex() {
        return positionVertexIndex;
    }

    public void setPositionVertexIndex(int index) {
        positionVertexIndex = index;
    }

}
