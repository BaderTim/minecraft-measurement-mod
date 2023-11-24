package io.github.mmm.measurement.device.objects;

public class ImuScan {

    private final double accX; // linear acceleration
    private final double accY;
    private final double accZ;

    private final double gyroX; // angular velocity
    private final double gyroY;
    private final double gyroZ;

    private final long timestamp;

    public ImuScan(double accX, double accY, double accZ, double gyroX, double gyroY, double gyroZ) {
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
}
