package io.github.mmm.measurement.devices.lidar.multithread;

import io.github.mmm.measurement.devices.lidar.LiDAR;
import io.github.mmm.measurement.objects.LidarScan;

public class SubLiDARPipeline implements Runnable {

    private final LiDAR[] subLidars;
    private volatile LidarScan[] scans;

    public SubLiDARPipeline(LiDAR[] subLidars) {
        this.subLidars = subLidars;
        this.scans = new LidarScan[this.subLidars.length];
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

    public LidarScan[] getScans() {
        return this.scans;
    }
}
