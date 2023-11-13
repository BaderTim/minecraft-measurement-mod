package io.github.mmm.measurement.objects;

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
        String distancesString = "";
        for (int i = 0; i < distances.length - 1; i++) {
            distancesString += java.util.Arrays.toString(distances[i].getDistances()) + ",";
        }
        distancesString += java.util.Arrays.toString(distances[distances.length - 1].getDistances());
        return "Scan3D{" +
                "scansPerHorizontalRadius=" + scansPerHorizontalRadius +
                ",scansPerVerticalRadius=" + scansPerVerticalRadius +
                ",distances=[" + distancesString +
                "]}";
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
