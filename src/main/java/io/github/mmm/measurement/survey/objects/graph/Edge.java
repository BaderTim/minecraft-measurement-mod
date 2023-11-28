package io.github.mmm.measurement.survey.objects.graph;

public class Edge {

    private Vertex startVertex;
    private Vertex endVertex;
    private int index;

    public Edge(Vertex startVertex, Vertex endVertex, int index) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.index = index;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public int getIndex() {
        return index;
    }
}
