package io.github.mmm.measurement.device.scans;

public class LidarScan {

    private LidarScan2D scan2D;
    private LidarScan3D scan3D;
    private boolean is2D;

    public LidarScan(LidarScan2D scan2D) {
        this.scan2D = scan2D;
        this.is2D = true;
    }

    public LidarScan(LidarScan3D scan3D) {
        this.scan3D = scan3D;
        this.is2D = false;
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

}
