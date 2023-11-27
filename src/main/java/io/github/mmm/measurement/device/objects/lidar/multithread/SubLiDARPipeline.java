package io.github.mmm.measurement.device.objects.lidar.multithread;

import io.github.mmm.measurement.device.objects.lidar.LiDAR;
import io.github.mmm.measurement.device.scans.LidarScan;

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
                this.scans[i] = this.subLidars[i].performScan();
            } else {
                this.scans[i] = null;
            }
        }
    }

    public LidarScan[] getScans() {
        return this.scans;
    }
}
