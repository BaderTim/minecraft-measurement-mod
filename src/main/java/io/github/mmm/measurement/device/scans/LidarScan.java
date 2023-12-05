package io.github.mmm.measurement.device.scans;

public class LidarScan {

    private LidarScan2D scan2D;
    private LidarScan3D scan3D;
    private boolean is2D;

    private final double posX; // position
    private final double posY;
    private final double posZ;

    private final double viewX; // view direction
    private final double viewY;
    private final double viewZ;

    public LidarScan(LidarScan2D scan2D) {
        this.scan2D = scan2D;
        this.is2D = true;
        this.posX = scan2D.getPosX();
        this.posY = scan2D.getPosY();
        this.posZ = scan2D.getPosZ();
        this.viewX = scan2D.getViewX();
        this.viewY = scan2D.getViewY();
        this.viewZ = scan2D.getViewZ();
    }

    public LidarScan(LidarScan3D scan3D) {
        this.scan3D = scan3D;
        this.is2D = false;
        this.posX = scan2D.getPosX();
        this.posY = scan2D.getPosY();
        this.posZ = scan2D.getPosZ();
        this.viewX = scan2D.getViewX();
        this.viewY = scan2D.getViewY();
        this.viewZ = scan2D.getViewZ();
    }

    @Override
    public String toString() {
        if (is2D) {
            return scan2D.toString();
        } else {
            return scan3D.toString();
        }
    }

    public LidarScan2D getScan2D() {
        return scan2D;
    }

    public LidarScan3D getScan3D() {
        return scan3D;
    }

    public boolean is2D() {
        return is2D;
    }

    public long getTimestamp() {
        if (is2D) {
            return scan2D.getTimestamp();
        } else {
            return scan3D.getTimestamp();
        }
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
