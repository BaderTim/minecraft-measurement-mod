package io.github.mmm.measurement.utils;

public class Scan2D {

    private double[] distances;
    private int scansPerRadius;

    public Scan2D(int scansPerRadius) {
        if (scansPerRadius < 1) {
            throw new IllegalArgumentException("scansPerRadius must be greater than 0");
        }
        this.scansPerRadius = scansPerRadius;
        this.distances = new double[scansPerRadius];
    }

    @Override
    public String toString() {
        return "Scan2D{" +
                "scansPerRadius=" + scansPerRadius +
                ",distances=" + java.util.Arrays.toString(distances) +
                '}';
    }

    public void setDistance(int scan, double distance) {
        this.distances[scan] = distance;
    }

    public double getDistance(int scan) {
        return this.distances[scan];
    }

    public double[] getDistances() {
        return this.distances;
    }

    public int getScansPerRadius() {
        return this.scansPerRadius;
    }

}
