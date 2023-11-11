package io.github.mmm.measurement.utils;

public class Scan3D {

    private int scansPerHorizontalRadius;
    private int scansPerVerticalRadius;
    private Scan2D[] distances;

    public Scan3D(int scansPerHorizontalRadius, int scansPerVerticalRadius) {
        if (scansPerHorizontalRadius < 1) {
            throw new IllegalArgumentException("scansPerHorizontalRadius must be greater than 0");
        } else if (scansPerVerticalRadius < 1) {
            throw new IllegalArgumentException("scansPerVerticalRadius must be greater than 0");
        }
        this.scansPerHorizontalRadius = scansPerHorizontalRadius;
        this.scansPerVerticalRadius = scansPerVerticalRadius;
        this.distances = new Scan2D[scansPerVerticalRadius];
    }

    @Override
    public String toString() {
        return "Scan3D{" +
                "scansPerHorizontalRadius=" + scansPerHorizontalRadius +
                ",scansPerVerticalRadius=" + scansPerVerticalRadius +
                ",distances=" + java.util.Arrays.toString(distances) +
                '}';
    }

    public void setScan2D(int scanNumber, Scan2D scan2D) {
        this.distances[scanNumber] = scan2D;
    }

    public Scan2D getScan2D(int scanNumber) {
        return this.distances[scanNumber];
    }

    public Scan2D[] getDistances() {
        return this.distances;
    }

    public int getScansPerHorizontalRadius() {
        return this.scansPerHorizontalRadius;
    }

    public int getScansPerVerticalRadius() {
        return this.scansPerVerticalRadius;
    }

}
