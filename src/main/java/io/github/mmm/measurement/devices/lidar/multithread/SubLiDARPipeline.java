package io.github.mmm.measurement.devices.lidar.multithread;

import io.github.mmm.measurement.devices.lidar.LiDAR;
import io.github.mmm.measurement.objects.Scan;

public class SubLiDARPipeline implements Runnable {

    private final LiDAR[] subLidars;
    private volatile Scan[] scans;

    public SubLiDARPipeline(LiDAR[] subLidars) {
        this.subLidars = subLidars;
        this.scans = new Scan[this.subLidars.length];
    }

    @Override
    public void run() {
        for (int i = 0; i < this.subLidars.length; i++) {
            if (this.subLidars[i] != null) {
                this.scans[i] = this.subLidars[i].scanFromPOVToBlocks();
            } else {
                this.scans[i] = null;
            }
        }
    }

    public Scan[] getScans() {
        return this.scans;
    }
}
