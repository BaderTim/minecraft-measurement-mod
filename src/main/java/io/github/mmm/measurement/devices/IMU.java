package io.github.mmm.measurement.devices;

public class IMU {

    private static final double G = 9.80665;

    private double accX = 0;
    private double accY = 0;
    private double accZ = 0;

    private double gyroX = 0;
    private double gyroY = 0;
    private double gyroZ = 0;

    private double magX = 0;
    private double magY = 0;
    private double magZ = 0;

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public double getGyroX() {
        return gyroX;
    }

    public void setGyroX(double gyroX) {
        this.gyroX = gyroX;
    }

    public double getGyroY() {
        return gyroY;
    }

    public void setGyroY(double gyroY) {
        this.gyroY = gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(double gyroZ) {
        this.gyroZ = gyroZ;
    }

    public double getMagX() {
        return magX;
    }

    public void setMagX(double magX) {
        this.magX = magX;
    }

    public double getMagY() {
        return magY;
    }

    public void setMagY(double magY) {
        this.magY = magY;
    }

    public double getMagZ() {
        return magZ;
    }

    public void setMagZ(double magZ) {
        this.magZ = magZ;
    }
}
