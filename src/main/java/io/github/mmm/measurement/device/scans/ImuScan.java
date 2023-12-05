package io.github.mmm.measurement.device.scans;

public class ImuScan {

    private final double posX; // position
    private final double posY;
    private final double posZ;

    private final double viewX; // view direction
    private final double viewY;
    private final double viewZ;

    private final double accX; // linear acceleration
    private final double accY;
    private final double accZ;

    private final double gyroX; // angular velocity
    private final double gyroY;
    private final double gyroZ;

    private final long timestamp;

    public ImuScan(double posX, double posY, double posZ, double viewX, double viewY, double viewZ, double accX, double accY, double accZ, double gyroX, double gyroY, double gyroZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.viewX = viewX;
        this.viewY = viewY;
        this.viewZ = viewZ;
        this.timestamp = System.currentTimeMillis();
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyroX = gyroX;
        this.gyroY = gyroY;
        this.gyroZ = gyroZ;
    }

    public double getAccX() {
        return accX;
    }

    public double getAccY() {
        return accY;
    }

    public double getAccZ() {
        return accZ;
    }

    public double getGyroX() {
        return gyroX;
    }

    public double getGyroY() {
        return gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
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
