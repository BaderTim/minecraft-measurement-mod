package io.github.mmm.measurement.device.scans;

public class LidarScan2D {

    private double[] distances;
    private int scansPerRadius;
    private long timestamp;

    private final double posX; // position
    private final double posY;
    private final double posZ;

    private final double viewX; // view direction
    private final double viewY;
    private final double viewZ;

    public LidarScan2D(int scansPerRadius, double posX, double posY, double posZ, double viewX, double viewY, double viewZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.viewX = viewX;
        this.viewY = viewY;
        this.viewZ = viewZ;
        if (scansPerRadius < 1) {
            throw new IllegalArgumentException("scansPerRadius must be greater than 0");
        }
        this.scansPerRadius = scansPerRadius;
        this.distances = new double[scansPerRadius];
        this.timestamp = System.currentTimeMillis();
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

    public long getTimestamp() {
        return timestamp;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public double getViewX() {
        return viewX;
    }

    public double getViewY() {
        return viewY;
    }

    public double getViewZ() {
        return viewZ;
    }
}
