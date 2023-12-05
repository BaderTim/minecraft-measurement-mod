package io.github.mmm.measurement.device.scans;

public class LidarScan3D {

    private int scansPerHorizontalRadius;
    private int scansPerVerticalRadius;
    private long timestamp;
    private LidarScan2D[] distances;

    private final double posX; // position
    private final double posY;
    private final double posZ;

    private final double viewX; // view direction
    private final double viewY;
    private final double viewZ;

    public LidarScan3D(int scansPerHorizontalRadius, int scansPerVerticalRadius, double posX, double posY, double posZ, double viewX, double viewY, double viewZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.viewX = viewX;
        this.viewY = viewY;
        this.viewZ = viewZ;
        if (scansPerHorizontalRadius < 1) {
            throw new IllegalArgumentException("scansPerHorizontalRadius must be greater than 0");
        } else if (scansPerVerticalRadius < 1) {
            throw new IllegalArgumentException("scansPerVerticalRadius must be greater than 0");
        }
        this.scansPerHorizontalRadius = scansPerHorizontalRadius;
        this.scansPerVerticalRadius = scansPerVerticalRadius;
        this.distances = new LidarScan2D[scansPerVerticalRadius];
        this.timestamp = System.currentTimeMillis();
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

    public void setScan2D(int scanNumber, LidarScan2D scan2D) {
        this.distances[scanNumber] = scan2D;
    }

    public LidarScan2D getScan2D(int scanNumber) {
        return this.distances[scanNumber];
    }

    public LidarScan2D[] getDistances() {
        return this.distances;
    }

    public int getScansPerHorizontalRadius() {
        return this.scansPerHorizontalRadius;
    }

    public int getScansPerVerticalRadius() {
        return this.scansPerVerticalRadius;
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
